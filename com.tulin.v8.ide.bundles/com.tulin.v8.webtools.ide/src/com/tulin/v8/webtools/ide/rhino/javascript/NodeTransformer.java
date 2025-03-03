/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Norris Boyd
 *   Igor Bukanov
 *   Bob Jervis
 *   Roger Lawrence
 *   Mike McCabe
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

package com.tulin.v8.webtools.ide.rhino.javascript;

import java.util.ArrayList;
import java.util.List;

/**
 * This class transforms a tree to a lower-level representation for codegen.
 *
 * @see Node
 */
public class NodeTransformer {

	public NodeTransformer() {
	}

	public final void transform(ScriptOrFnNode tree) {
		transformCompilationUnit(tree);
		for (int i = 0; i != tree.getFunctionCount(); ++i) {
			FunctionNode fn = tree.getFunctionNode(i);
			transform(fn);
		}
	}

	private void transformCompilationUnit(ScriptOrFnNode tree) {
		loops = new ObjArray();
		loopEnds = new ObjArray();

		// to save against upchecks if no finally blocks are used.
		hasFinally = false;

		// Flatten all only if we are not using scope objects for block scope
		boolean createScopeObjects = tree.getType() != Token.FUNCTION || ((FunctionNode) tree).requiresActivation();
		tree.flattenSymbolTable(!createScopeObjects);

		// uncomment to print tree before transformation
		// if (Token.printTrees) System.out.println(tree.toStringTree(tree));
		transformCompilationUnit_r(tree, tree, tree, createScopeObjects);
	}

	private void transformCompilationUnit_r(final ScriptOrFnNode tree, final Node parent, Node.Scope scope,
			boolean createScopeObjects) {
		Node node = null;
		siblingLoop: for (;;) {
			Node previous = null;
			if (node == null) {
				node = parent.getFirstChild();
			} else {
				previous = node;
				node = node.getNext();
			}
			if (node == null) {
				break;
			}

			int type = node.getType();
			if (createScopeObjects && (type == Token.BLOCK || type == Token.LOOP || type == Token.ARRAYCOMP)
					&& (node instanceof Node.Scope)) {
				Node.Scope newScope = (Node.Scope) node;
				if (newScope.symbolTable != null) {
					// transform to let statement so we get a with statement
					// created to contain scoped let variables
					Node let = new Node(type == Token.ARRAYCOMP ? Token.LETEXPR : Token.LET);
					Node innerLet = new Node(Token.LET);
					let.addChildToBack(innerLet);
					for (String name : newScope.symbolTable.keySet()) {
						innerLet.addChildToBack(Node.newString(Token.NAME, name));
					}
					newScope.symbolTable = null; // so we don't transform again
					Node oldNode = node;
					node = replaceCurrent(parent, previous, node, let);
					type = node.getType();
					let.addChildToBack(oldNode);
				}
			}

			switch (type) {

			case Token.LABEL:
			case Token.SWITCH:
			case Token.LOOP:
				loops.push(node);
				loopEnds.push(((Node.Jump) node).target);
				break;

			case Token.WITH: {
				loops.push(node);
				Node leave = node.getNext();
				if (leave.getType() != Token.LEAVEWITH) {
					Kit.codeBug();
				}
				loopEnds.push(leave);
				break;
			}

			case Token.TRY: {
				Node.Jump jump = (Node.Jump) node;
				Node finallytarget = jump.getFinally();
				if (finallytarget != null) {
					hasFinally = true;
					loops.push(node);
					loopEnds.push(finallytarget);
				}
				break;
			}

			case Token.TARGET:
			case Token.LEAVEWITH:
				if (!loopEnds.isEmpty() && loopEnds.peek() == node) {
					loopEnds.pop();
					loops.pop();
				}
				break;

			case Token.YIELD:
				((FunctionNode) tree).addResumptionPoint(node);
				break;

			case Token.RETURN: {
				boolean isGenerator = tree.getType() == Token.FUNCTION && ((FunctionNode) tree).isGenerator();
				if (isGenerator) {
					node.putIntProp(Node.GENERATOR_END_PROP, 1);
				}
				/*
				 * If we didn't support try/finally, it wouldn't be necessary to put LEAVEWITH
				 * nodes here... but as we do need a series of JSR FINALLY nodes before each
				 * RETURN, we need to ensure that each finally block gets the correct scope...
				 * which could mean that some LEAVEWITH nodes are necessary.
				 */
				if (!hasFinally)
					break; // skip the whole mess.
				Node unwindBlock = null;
				for (int i = loops.size() - 1; i >= 0; i--) {
					Node n = (Node) loops.get(i);
					int elemtype = n.getType();
					if (elemtype == Token.TRY || elemtype == Token.WITH) {
						Node unwind;
						if (elemtype == Token.TRY) {
							Node.Jump jsrnode = new Node.Jump(Token.JSR);
							Node jsrtarget = ((Node.Jump) n).getFinally();
							jsrnode.target = jsrtarget;
							unwind = jsrnode;
						} else {
							unwind = new Node(Token.LEAVEWITH);
						}
						if (unwindBlock == null) {
							unwindBlock = new Node(Token.BLOCK, node.getLineno());
						}
						unwindBlock.addChildToBack(unwind);
					}
				}
				if (unwindBlock != null) {
					Node returnNode = node;
					Node returnExpr = returnNode.getFirstChild();
					node = replaceCurrent(parent, previous, node, unwindBlock);
					if (returnExpr == null || isGenerator) {
						unwindBlock.addChildToBack(returnNode);
					} else {
						Node store = new Node(Token.EXPR_RESULT, returnExpr);
						unwindBlock.addChildToFront(store);
						returnNode = new Node(Token.RETURN_RESULT);
						unwindBlock.addChildToBack(returnNode);
						// transform return expression
						transformCompilationUnit_r(tree, store, scope, createScopeObjects);
					}
					// skip transformCompilationUnit_r to avoid infinite loop
					continue siblingLoop;
				}
				break;
			}

			case Token.BREAK:
			case Token.CONTINUE: {
				Node.Jump jump = (Node.Jump) node;
				Node.Jump jumpStatement = jump.getJumpStatement();
				if (jumpStatement == null)
					Kit.codeBug();

				for (int i = loops.size();;) {
					if (i == 0) {
						// Parser/IRFactory ensure that break/continue
						// always has a jump statement associated with it
						// which should be found
						throw Kit.codeBug();
					}
					--i;
					Node n = (Node) loops.get(i);
					if (n == jumpStatement) {
						break;
					}

					int elemtype = n.getType();
					if (elemtype == Token.WITH) {
						Node leave = new Node(Token.LEAVEWITH);
						previous = addBeforeCurrent(parent, previous, node, leave);
					} else if (elemtype == Token.TRY) {
						Node.Jump tryNode = (Node.Jump) n;
						Node.Jump jsrFinally = new Node.Jump(Token.JSR);
						jsrFinally.target = tryNode.getFinally();
						previous = addBeforeCurrent(parent, previous, node, jsrFinally);
					}
				}

				if (type == Token.BREAK) {
					jump.target = jumpStatement.target;
				} else {
					jump.target = jumpStatement.getContinue();
				}
				jump.setType(Token.GOTO);

				break;
			}

			case Token.CALL:
				visitCall(node, tree);
				break;

			case Token.NEW:
				visitNew(node, tree);
				break;

			case Token.LETEXPR:
			case Token.LET: {
				Node child = node.getFirstChild();
				if (child.getType() == Token.LET) {
					// We have a let statement or expression rather than a
					// let declaration
					boolean createWith = tree.getType() != Token.FUNCTION || ((FunctionNode) tree).requiresActivation();
					node = visitLet(createWith, parent, previous, node);
					break;
				} else {
					// fall through to process let declaration...
				}
			}
			/* fall through */
			case Token.CONST:
			case Token.VAR: {
				Node result = new Node(Token.BLOCK);
				for (Node cursor = node.getFirstChild(); cursor != null;) {
					// Move cursor to next before createAssignment gets chance
					// to change n.next
					Node n = cursor;
					cursor = cursor.getNext();
					if (n.getType() == Token.NAME) {
						if (!n.hasChildren())
							continue;
						Node init = n.getFirstChild();
						n.removeChild(init);
						n.setType(Token.BINDNAME);
						n = new Node(type == Token.CONST ? Token.SETCONST : Token.SETNAME, n, init);
					} else {
						// May be a destructuring assignment already transformed
						// to a LETEXPR
						if (n.getType() != Token.LETEXPR)
							throw Kit.codeBug();
					}
					Node pop = new Node(Token.EXPR_VOID, n, node.getLineno());
					result.addChildToBack(pop);
				}
				node = replaceCurrent(parent, previous, node, result);
				break;
			}

			case Token.TYPEOFNAME: {
				Node.Scope defining = scope.getDefiningScope(node.getString());
				if (defining != null) {
					node.setScope(defining);
				}
			}
				break;

			case Token.TYPEOF:
			case Token.IFNE: {
				/*
				 * We want to suppress warnings for undefined property o.p for the following
				 * constructs: typeof o.p, if (o.p), if (!o.p), if (o.p == undefined), if
				 * (undefined == o.p)
				 */
				Node child = node.getFirstChild();
				if (type == Token.IFNE) {
					while (child.getType() == Token.NOT) {
						child = child.getFirstChild();
					}
					if (child.getType() == Token.EQ || child.getType() == Token.NE) {
						Node first = child.getFirstChild();
						Node last = child.getLastChild();
						if (first.getType() == Token.NAME && first.getString().equals("undefined"))
							child = last;
						else if (last.getType() == Token.NAME && last.getString().equals("undefined"))
							child = first;
					}
				}
				if (child.getType() == Token.GETPROP)
					child.setType(Token.GETPROPNOWARN);
				break;
			}

			case Token.NAME:
			case Token.SETNAME:
			case Token.SETCONST:
			case Token.DELPROP: {
				// Turn name to var for faster access if possible
				if (createScopeObjects) {
					break;
				}
				Node nameSource;
				if (type == Token.NAME) {
					nameSource = node;
				} else {
					nameSource = node.getFirstChild();
					if (nameSource.getType() != Token.BINDNAME) {
						if (type == Token.DELPROP) {
							break;
						}
						throw Kit.codeBug();
					}
				}
				if (nameSource.getScope() != null) {
					break; // already have a scope set
				}
				String name = nameSource.getString();
				Node.Scope defining = scope.getDefiningScope(name);
				if (defining != null) {
					nameSource.setScope(defining);
					if (type == Token.NAME) {
						node.setType(Token.GETVAR);
					} else if (type == Token.SETNAME) {
						node.setType(Token.SETVAR);
						nameSource.setType(Token.STRING);
					} else if (type == Token.SETCONST) {
						node.setType(Token.SETCONSTVAR);
						nameSource.setType(Token.STRING);
					} else if (type == Token.DELPROP) {
						// Local variables are by definition permanent
						Node n = new Node(Token.FALSE);
						node = replaceCurrent(parent, previous, node, n);
					} else {
						throw Kit.codeBug();
					}
				}
				break;
			}
			}

			transformCompilationUnit_r(tree, node, node instanceof Node.Scope ? (Node.Scope) node : scope,
					createScopeObjects);
		}
	}

	protected void visitNew(Node node, ScriptOrFnNode tree) {
	}

	protected void visitCall(Node node, ScriptOrFnNode tree) {
	}

	protected Node visitLet(boolean createWith, Node parent, Node previous, Node scopeNode) {
		Node vars = scopeNode.getFirstChild();
		Node body = vars.getNext();
		scopeNode.removeChild(vars);
		scopeNode.removeChild(body);
		boolean isExpression = scopeNode.getType() == Token.LETEXPR;
		Node result;
		Node newVars;
		if (createWith) {
			result = new Node(isExpression ? Token.WITHEXPR : Token.BLOCK);
			result = replaceCurrent(parent, previous, scopeNode, result);
			ArrayList<Object> list = new ArrayList<Object>();
			Node objectLiteral = new Node(Token.OBJECTLIT);
			for (Node v = vars.getFirstChild(); v != null; v = v.getNext()) {
				Node current = v;
				if (current.getType() == Token.LETEXPR) {
					// destructuring in let expr, e.g. let ([x, y] = [3, 4]) {}
					List<?> destructuringNames = (List<?>) current.getProp(Node.DESTRUCTURING_NAMES);
					Node c = current.getFirstChild();
					if (c.getType() != Token.LET)
						throw Kit.codeBug();
					// Add initialization code to front of body
					if (isExpression) {
						body = new Node(Token.COMMA, c.getNext(), body);
					} else {
						body = new Node(Token.BLOCK, new Node(Token.EXPR_VOID, c.getNext()), body);
					}
					// Update "list" and "objectLiteral" for the variables
					// defined in the destructuring assignment
					if (destructuringNames != null) {
						list.addAll(destructuringNames);
						for (int i = 0; i < destructuringNames.size(); i++) {
							objectLiteral.addChildToBack(new Node(Token.VOID, Node.newNumber(0.0)));
						}
					}
					current = c.getFirstChild(); // should be a NAME, checked below
				}
				if (current.getType() != Token.NAME)
					throw Kit.codeBug();
				list.add(ScriptRuntime.getIndexObject(current.getString()));
				Node init = current.getFirstChild();
				if (init == null) {
					init = new Node(Token.VOID, Node.newNumber(0.0));
				}
				objectLiteral.addChildToBack(init);
			}
			objectLiteral.putProp(Node.OBJECT_IDS_PROP, list.toArray());
			newVars = new Node(Token.ENTERWITH, objectLiteral);
			result.addChildToBack(newVars);
			result.addChildToBack(new Node(Token.WITH, body));
			result.addChildToBack(new Node(Token.LEAVEWITH));
		} else {
			result = new Node(isExpression ? Token.COMMA : Token.BLOCK);
			result = replaceCurrent(parent, previous, scopeNode, result);
			newVars = new Node(Token.COMMA);
			for (Node v = vars.getFirstChild(); v != null; v = v.getNext()) {
				Node current = v;
				if (current.getType() == Token.LETEXPR) {
					// destructuring in let expr, e.g. let ([x, y] = [3, 4]) {}
					Node c = current.getFirstChild();
					if (c.getType() != Token.LET)
						throw Kit.codeBug();
					// Add initialization code to front of body
					if (isExpression) {
						body = new Node(Token.COMMA, c.getNext(), body);
					} else {
						body = new Node(Token.BLOCK, new Node(Token.EXPR_VOID, c.getNext()), body);
					}
					// We're removing the LETEXPR, so move the symbols
					Node.Scope.joinScopes((Node.Scope) current, (Node.Scope) scopeNode);
					current = c.getFirstChild(); // should be a NAME, checked below
				}
				if (current.getType() != Token.NAME)
					throw Kit.codeBug();
				Node stringNode = Node.newString(current.getString());
				stringNode.setScope((Node.Scope) scopeNode);
				Node init = current.getFirstChild();
				if (init == null) {
					init = new Node(Token.VOID, Node.newNumber(0.0));
				}
				newVars.addChildToBack(new Node(Token.SETVAR, stringNode, init));
			}
			if (isExpression) {
				result.addChildToBack(newVars);
				scopeNode.setType(Token.COMMA);
				result.addChildToBack(scopeNode);
				scopeNode.addChildToBack(body);
			} else {
				result.addChildToBack(new Node(Token.EXPR_VOID, newVars));
				scopeNode.setType(Token.BLOCK);
				result.addChildToBack(scopeNode);
				scopeNode.addChildrenToBack(body);
			}
		}
		return result;
	}

	private static Node addBeforeCurrent(Node parent, Node previous, Node current, Node toAdd) {
		if (previous == null) {
			if (!(current == parent.getFirstChild()))
				Kit.codeBug();
			parent.addChildToFront(toAdd);
		} else {
			if (!(current == previous.getNext()))
				Kit.codeBug();
			parent.addChildAfter(toAdd, previous);
		}
		return toAdd;
	}

	private static Node replaceCurrent(Node parent, Node previous, Node current, Node replacement) {
		if (previous == null) {
			if (!(current == parent.getFirstChild()))
				Kit.codeBug();
			parent.replaceChild(current, replacement);
		} else if (previous.next == current) {
			// Check cachedPrev.next == current is necessary due to possible
			// tree mutations
			parent.replaceChildAfter(previous, replacement);
		} else {
			parent.replaceChild(current, replacement);
		}
		return replacement;
	}

	private ObjArray loops;
	private ObjArray loopEnds;
	private boolean hasFinally;
}
