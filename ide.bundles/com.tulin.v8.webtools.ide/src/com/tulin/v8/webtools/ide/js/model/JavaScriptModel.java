package com.tulin.v8.webtools.ide.js.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.js.JsDocParser;
import com.tulin.v8.webtools.ide.js.JsDocParser.JsDoc;
import com.tulin.v8.webtools.ide.js.JsDocParser.JsDocParam;
import com.tulin.v8.webtools.ide.js.launch.JavaScriptLibPathTable;
import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction.JavaScriptArgument;
import com.tulin.v8.webtools.ide.rhino.javascript.CompilerEnvirons;
import com.tulin.v8.webtools.ide.rhino.javascript.ErrorReporter;
import com.tulin.v8.webtools.ide.rhino.javascript.EvaluatorException;
import com.tulin.v8.webtools.ide.rhino.javascript.FunctionNode;
import com.tulin.v8.webtools.ide.rhino.javascript.Node;
import com.tulin.v8.webtools.ide.rhino.javascript.Parser;
import com.tulin.v8.webtools.ide.rhino.javascript.ScriptOrFnNode;
import com.tulin.v8.webtools.ide.rhino.javascript.Token;
import com.tulin.v8.webtools.ide.rhino.javascript.Node.Symbol;
import com.tulin.v8.webtools.ide.utils.IOUtil;

public class JavaScriptModel extends JavaScriptContext {
	// child models
	protected List<JavaScriptModel> modelList = new ArrayList<JavaScriptModel>();

	// comments
	private List<JavaScriptComment> commentList = new ArrayList<JavaScriptComment>();

	// defined object type info
	protected Map<String, JavaScriptContext> objectTypeMap = new HashMap<String, JavaScriptContext>();

	private Object jsFile;

	private ScriptOrFnNode rhinoJsNode;

	private Deque<Node> nodeDeque;

	private String source;

	private Node callerArgNode = null;

	private JavaScriptElement global;

	private boolean isUpdate = false;

	private Map<Object, JavaScriptModel> includedModelMap;

	private List<RequirePathData> requirePathDataList = new ArrayList<RequirePathData>();

	public JavaScriptModel(Object file, String source) {
		this(file, source, null);
	}

	public JavaScriptModel(Object file, String source, List<JavaScriptModel> modelList) {
		this(file, source, modelList, new HashMap<Object, JavaScriptModel>());
	}

	public JavaScriptModel(Object file, String source, List<JavaScriptModel> modelList,
			Map<Object, JavaScriptModel> includedModelMap) {
		super(null, null, 0, source.length());
		model = this;
		if (modelList != null) {
			for (JavaScriptModel model : modelList) {
				addModel(model);
			}
		}
		this.jsFile = file;
		this.includedModelMap = includedModelMap;
		includedModelMap.put(file, this);
		update(source);
	}

	public void addModel(JavaScriptModel model) {
		modelList.add(model);
		objectTypeMap.putAll(model.objectTypeMap);
	}

	public JavaScriptContext getObjectTypeContext(String objectType) {
		return objectTypeMap.get(objectType);
	}

	/**
	 * Updates model structure by the specified source code.
	 *
	 * @param source JavaScript
	 */
	public void update(String source) {
		clear();
		this.nodeDeque = new Deque<Node>();
		this.source = source;
		end = source.length();
		commentList.clear();

		Parser parser = new Parser(new CompilerEnvirons(), new RhinoErrorReporter());
		rhinoJsNode = parser.parse(source, null, 0);

		buildModel(this, rhinoJsNode);
		for (Node node : rhinoJsNode.commentList) {
			JavaScriptComment jsComment = new JavaScriptComment(node.sourceStart, node.sourceEnd,
					source.substring(node.sourceStart, node.sourceEnd));
			commentList.add(jsComment);
		}

		// for debug
		// FileWriter writer = null;
		// try {
		// File tempFile = File.createTempFile("jsmodel", ".txt");
		// tempFile.deleteOnExit();
		// writer = new FileWriter(tempFile);
		// JavaScriptModelUtil.print(writer, rhinoJsNode, source, 0);
		// JavaScriptModelUtil
		// .print(new HashSet<JavaScriptContext>(), this, 0);
		// } catch (IOException e) {
		// } finally {
		// IOUtil.closeQuietly(writer);
		// }

		// clear
		this.nodeDeque = null;
		this.source = null;
		this.rhinoJsNode = null;
	}

	private void buildModel(JavaScriptContext context, Node node) {
		int dequeCount = 0;
		while (node != null) {
			int nodeType = node.getType();
			boolean stepInFirstChild = true;
			switch (nodeType) {
			case Token.FUNCTION:
				if (node.fnNode != null) {
					boolean clearArgNode = false;
					if (nodeDeque.peekLast().getType() == Token.CALL) {
						callerArgNode = node.getNext();
						clearArgNode = true;
					}
					nodeDeque.offerLast(node);
					buildModel(context, node.fnNode);
					nodeDeque.pollLast();
					if (clearArgNode) {
						callerArgNode = null;
					}
					break;
				} else if (node instanceof FunctionNode) {
					JavaScriptFunction jsFunc = createFunction(context, (FunctionNode) node);
					context.addElement(jsFunc);
					return;
				}
			case Token.NAME:
				switch (nodeDeque.peekLast().getType()) {
				case Token.VAR:
					Node varNode = node;
					while (varNode != null) {
						addVariable(context, varNode);
						varNode = varNode.getNext();
					}
					stepInFirstChild = false;
					break;
				default:
					break;
				}
				break;
			case Token.SETPROP:
				if (parseSetProperty(context, node.getFirstChild()) != null) {
					return;
				}
				break;
			case Token.RETURN:
				if (node.getFirstChild() != null) {
					JavaScriptElement jsElement;
					switch (node.getFirstChild().getType()) {
					case Token.CALL:
						jsElement = parseCallStatement(context, node.getFirstChild());
						break;
					case Token.SETPROP:
						jsElement = parseSetProperty(context, node.getFirstChild().getFirstChild());
						break;
					case Token.NEW:
						jsElement = getProp(context, node.getFirstChild().getFirstChild(), false);
						break;
					case Token.THIS:
						jsElement = context.getTarget();
						break;
					default:
						jsElement = getProp(context, node.getFirstChild(), false);
						break;
					}
					if (jsElement != null) {
						context.setReturnElement(jsElement);
						if (context.getTarget() != null && context.getTarget().getFunction() != null) {
							JavaScriptFunction jsFunc = context.getTarget().getFunction();
							jsFunc.addReturnTypes(jsElement.getTypes());
						}
						return;
					}
				}
				break;
			case Token.OBJPROP:
				if (node.getFirstChild() != null) {
					parseObjectProperty(context, node);
				}
				stepInFirstChild = false;
				break;
			case Token.CALL:
				if (parseCallStatement(context, node) != null) {
					return;
				}
				break;
			case Token.SETNAME:
				if (node.getFirstChild() != null && node.getFirstChild().getType() == Token.BINDNAME) {
					if (parseGlobalObject(context, node) != null) {
						return;
					}
				}
				break;
			case Token.GETELEM:
				if (node.getFirstChild() != null) {
					JavaScriptElement jsElement = getProp(context, node.getFirstChild());
					if (jsElement != null) {
						boolean hasArray = false;
						for (String type : jsElement.getTypes()) {
							if ("Array".equals(type)) {
								hasArray = true;
							}
						}
						if (!hasArray) {
							jsElement.addTypes(new String[] { "Array" });
						}
						return;
					}
				}
				break;
			case Token.GETPROP:
				if (getProp(context, node) != null) {
					return;
				}
				break;
			default:
				break;
			}

			Node firstChild = node.getFirstChild();
			if (firstChild != null && firstChild.getType() != Token.OBJECTLIT && stepInFirstChild) {
				nodeDeque.offerLast(node);
				buildModel(context, firstChild);
				nodeDeque.pollLast();
			}

			Node next = node.getNext();
			if (next != null) {
				nodeDeque.offerLast(node);
				node = next;
				dequeCount++;
			} else {
				node = null;
			}
		}

		for (int i = 0; i < dequeCount; i++) {
			nodeDeque.pollLast();
		}
	}

	private JavaScriptVariable parseGlobalObject(JavaScriptContext context, Node node) {
		Node bnNode = node.getFirstChild();
		if (context.getElementByName(bnNode.getString(), false) != null) {
			return null;
		}

		JavaScriptVariable jsVar = createVariable(context, bnNode);

		Node nextNode = bnNode.getNext();
		if (nextNode != null && nextNode.getType() == Token.THISFN) {
			// THISFN: a variable for the function
			context.addElement(jsVar);
		} else {
			// global
			if (jsVar != null && "global".equals(bnNode.getString())) {
				global = jsVar;
			}
			if (global != null) {
				if (global.getContext() == null) {
					createNewContext(context, global);
				}
				global.getContext().addElement(jsVar);
			} else {
				addElement(jsVar);
			}
		}
		return jsVar;
	}

	private JavaScriptElement parseCallStatement(JavaScriptContext context, Node node) {
		Node gpNode = node.getFirstChild();
		if (gpNode != null && gpNode.getType() == Token.GETPROP) {
			if (gpNode.getFirstChild() != null && gpNode.getFirstChild().getType() == Token.STRING) {
				return null; // ex. "abc".split();
			}
			JavaScriptElement jsElement = getProp(context, gpNode);
			if (jsElement != null) {
				JavaScriptElement jsValue = null;
				if ("extend".equals(jsElement.getName())) {
					// jQuery
					JavaScriptElement jqueryElement = context.getElementByName("jQuery");
					jsValue = parseJQueryDynamicProperty(context, gpNode, jqueryElement);
				} else if ("__defineGetter__".equals(jsElement.getName())) {
					// __defineGetter__
					JavaScriptElement jsParent = getProp(context, gpNode.getFirstChild());
					if (jsParent != null) {
						jsValue = parseDynamicPropertyByDefineGetter(context, gpNode, jsParent);
					}
				}

				if (jsValue == null) {
					Node next = gpNode.getNext();
					if (next != null) {
						nodeDeque.offerLast(node);
						buildModel(context, next);
						nodeDeque.pollLast();
					}
				}
				return jsElement;
			}
		}
		return null;
	}

	private JavaScriptElement parseDynamicPropertyByDefineGetter(JavaScriptContext context, Node gpNode,
			JavaScriptElement jsElement) {
		if (jsElement != null) {
			Node strNode = gpNode.getNext();
			if (strNode != null && strNode.getType() == Token.STRING && strNode.getNext() != null
					&& strNode.getNext().getType() == Token.FUNCTION && strNode.getNext().fnNode != null) {
				FunctionNode fnNode = strNode.getNext().fnNode;
				JavaScriptFunction jsFunc = createFunction(context, fnNode);
				if (jsFunc != null) {
					jsFunc.setName(strNode.getString());
					if (jsElement.getContext() == null) {
						createNewContext(context, jsElement);
					}
					JavaScriptVariable jsVariable = new JavaScriptVariable(jsFunc.getName(), jsFunc.getReturnTypes());
					jsVariable.setContext(jsFunc.getContext());
					jsElement.getContext().addElement(jsVariable);
					return jsFunc;
				}
			}
		}
		return null;
	}

	private JavaScriptElement parseJQueryDynamicProperty(JavaScriptContext context, Node gpNode,
			JavaScriptElement jqElement) {
		if (jqElement != null) {
			Node objNode = gpNode.getNext();
			if (objNode != null) {
				if (objNode.getNext() == null) {
					JavaScriptContext jqContext = jqElement.getContext();
					if (objNode.getType() == Token.OBJECTLIT) {
						Node dummyNode = Node.newString("__dummy_jquery_node__");
						dummyNode.next = objNode;
						JavaScriptVariable jsVar = createVariable(jqContext, dummyNode);
						if (jsVar != null) {
							for (JavaScriptElement child : jsVar.getContext().getElements()) {
								if (!(child instanceof JavaScriptPrototype)) {
									child.setParent(jqElement);
									jqContext.addElement(child);
									if (child.getContext() != null) {
										for (JavaScriptElement gChild : child.getContext().getElements()) {
											if (gChild.isTemporary()) {
												gChild.setParent(jqElement);
												jqContext.addElement(gChild);
											}
										}
									} else if (child.getFunction() != null
											&& child.getFunction().getContext() != null) {
										for (JavaScriptElement gChild : child.getFunction().getContext()
												.getElements()) {
											if (gChild.isTemporary()) {
												gChild.setParent(jqElement);
												jqContext.addElement(gChild);
											}
										}
									}
								}
							}
						}
					} else {
						JavaScriptElement targetElement = getProp(context, objNode);
						if (targetElement != null && targetElement.getContext() != null) {
							for (JavaScriptElement child : targetElement.getContext().getElements()) {
								if (!(child instanceof JavaScriptPrototype)) {
									jqContext.addElement(child);
								}
							}
						}
					}
					return jqElement;
				}
			}
		}
		return null;
	}

	private void parseObjectProperty(JavaScriptContext context, Node node) {
		Node firstNode = node.getFirstChild();
		switch (firstNode.getType()) {
		case Token.FUNCTION: {
			JavaScriptFunction jsFunc = createFunction(context, firstNode.fnNode);
			if (jsFunc != null) {
				JavaScriptVariable jsVar = createObjectProperty(context, node, jsFunc.getReturnTypes());
				jsVar.setFunction(jsFunc);
			} else {
				createObjectProperty(context, node, new String[] { "*" });
			}
		}
			break;
		case Token.GETPROP:
		case Token.NAME:
			JavaScriptElement jsElement = getProp(context, firstNode);
			if (jsElement != null) {
				JavaScriptVariable jsVar = createObjectProperty(context, node, jsElement.getReturnTypes());
				JavaScriptFunction jsFunc = jsElement.getFunction();
				if (jsFunc != null) {
					jsVar.setFunction(jsFunc);
				}
			} else {
				createObjectProperty(context, node, new String[] { "*" });
			}
			break;
		case Token.OBJECTLIT:
			Node objNode = firstNode;
			JavaScriptContext block = new JavaScriptContext(this, context, objNode.sourceStart, objNode.sourceEnd);
			buildModel(block, objNode);
			context.addContext(block);
			String baseType = "Object:" + node.getString();
			String returnType = baseType;
			int count = 0;
			while (objectTypeMap.containsKey(returnType)) {
				count++;
				returnType = baseType + "_" + count;
			}
			putObjectType(returnType, block);
			JavaScriptVariable jsVar = createObjectProperty(context, node, new String[] { returnType });
			jsVar.setContext(block);
			break;
		case Token.STRING:
			createObjectProperty(context, node, new String[] { "String" });
			break;
		case Token.NUMBER:
			createObjectProperty(context, node, new String[] { "Number" });
			break;
		case Token.TRUE:
		case Token.FALSE:
			createObjectProperty(context, node, new String[] { "Boolean" });
			break;
		case Token.REGEXP:
			createObjectProperty(context, node, new String[] { "RegExp" });
			break;
		case Token.ARRAYLIT:
			createObjectProperty(context, node, new String[] { "Array" });
			if (firstNode.getFirstChild() != null) {
				buildModel(context, firstNode.getFirstChild());
			}
			break;
		case Token.NULL:
			createObjectProperty(context, node, new String[] { "Null" });
			break;
		default:
			break;
		}
	}

	private JavaScriptVariable createObjectProperty(JavaScriptContext context, Node node, String[] types) {
		JavaScriptVariable jsVar = new JavaScriptVariable(node.getString(), types);
		jsVar.setProperty(true);
		jsVar.setStart(node.getFirstChild().sourceStart);
		context.addElement(jsVar);
		if (context.getTarget() != null) {
			jsVar.setParent(context.getTarget());
		}
		return jsVar;
	}

	private JavaScriptElement parseSetProperty(JavaScriptContext context, Node node) {
		switch (node.getType()) {
		case Token.THIS: {
			Node nextNode = node.getNext();
			if (nextNode != null && nextNode.getType() == Token.STRING) {
				// Pattern: this.a = ...;
				JavaScriptVariable jsVar = createVariable(context, nextNode);
				jsVar.setProperty(true);

				if (context instanceof JavaScriptModel) {
					context.addElement(jsVar);
				} else {
					convertToClass(context);
					JavaScriptPrototype prototype = context.getTarget().createPrototype();
					prototype.getContext().addElement(jsVar);
				}
				return jsVar;
			}
			break;
		}
		case Token.GETPROP: {
			// Pattern: foo.bar... = ...;
			JavaScriptElement jsElement = getProp(context, node);
			return parseProperty(context, node, jsElement);
		}
		case Token.NAME: {
			// Pattern: foo.bar = ...;
			JavaScriptElement jsElement = getProp(context, node);
			return parseProperty(context, node, jsElement);
		}
		default:
			break;
		}
		return null;
	}

	private JavaScriptElement parseProperty(JavaScriptContext context, Node node, JavaScriptElement jsElement) {
		if (jsElement != null) {
			if (jsElement.getContext() == null) {
				createNewContext(context, jsElement);
			}
			Node nextNode = node.getNext();
			if (nextNode != null && nextNode.getType() == Token.STRING) {
				if ("prototype".equals(nextNode.getString())) {
					convertToClass(jsElement.getContext());
					JavaScriptPrototype prototype = jsElement.createPrototype();
					if (prototype != null) {
						Node nextNextNode = nextNode.getNext();
						if (nextNextNode != null) {
							switch (nextNextNode.getType()) {
							case Token.NEW:
								// Pattern: Foo.prototype = new Bar();
								Node clsNameNode = nextNextNode.getFirstChild();
								if (clsNameNode != null) {
									JavaScriptElement clsElement = null;
									if (clsNameNode.isString()) {
										clsElement = context.getElementByName(clsNameNode.getString());
									} else {
										clsElement = getProp(context, clsNameNode);
									}
									if (clsElement != null) {
										prototype.update(clsElement);
									}
									return clsElement;
								}
								break;
							case Token.STRING:
								// Pattern: Foo.prototype = Bar;
								JavaScriptElement clsJsElement = context.getElementByName(nextNextNode.getString());
								if (clsJsElement != null) {
									prototype.update(clsJsElement);
								}
								return clsJsElement;
							case Token.OBJECTLIT:
								JavaScriptContext block = new JavaScriptContext(this, context, nextNextNode.sourceStart,
										nextNextNode.sourceEnd);
								buildModel(block, nextNextNode);
								context.addContext(block);
								String baseType = "Object:" + nextNode.getString();
								String returnType = baseType;
								int count = 0;
								while (objectTypeMap.containsKey(returnType)) {
									count++;
									returnType = baseType + "_" + count;
								}
								putObjectType(returnType, block);
								JavaScriptVariable clsElement = new JavaScriptVariable("", new String[] { returnType });
								clsElement.setPrivate(true);
								clsElement.setStart(nextNextNode.sourceStart);
								if (context.getTarget() != null) {
									clsElement.setParent(context.getTarget());
								}
								clsElement.setContext(block);
								prototype.update(clsElement);
								return clsElement;
							default:
								break;
							}
						}
					}
				} else {
					// Pattern: Foo = ...;
					JavaScriptVariable jsVar = createVariable(context, nextNode);
					jsElement.getContext().addElement(jsVar);
					if (jsElement instanceof JavaScriptPrototype) {
						jsVar.setMethod(true);
						JavaScriptElement parent = jsElement.getParent();
						jsVar.setParent(parent);
						if (parent != null && parent instanceof JavaScriptFunction) {
							((JavaScriptFunction) parent).setClass(true);
						}
					} else {
						jsVar.setParent(jsElement);
						if (jsElement.getFunction() != null && jsElement.getFunction().isClass()) {
							jsVar.setStatic(true);
							JavaScriptFunction jsFunc = jsVar.getFunction();
							if (jsFunc != null && jsFunc.isAnonymous()) {
								jsFunc.setStatic(true);
							}
						}
					}
					return jsVar;
				}
			}
		}
		return null;
	}

	private void convertToClass(JavaScriptContext context) {
		JavaScriptElement parentElement = context.getTarget();
		if (parentElement != null) {
			JavaScriptFunction jsFunc = parentElement.getFunction();
			if (jsFunc != null) {
				jsFunc.setClass(true);
				putObjectType(jsFunc.getName(), context);
			}
		}
	}

	private JavaScriptVariable createVariable(JavaScriptContext context, Node node) {
		List<String> returnTypeList = new ArrayList<String>();
		JavaScriptElement jsElement = null;
		JavaScriptContext block = null;
		Node nextNode = node.getNext();
		if (nextNode != null) {
			switch (nextNode.getType()) {
			case Token.NAME:
			case Token.GETPROP:
				// Pattern: this.a = p;
				jsElement = getProp(context, nextNode);
				break;
			case Token.STRING:
				returnTypeList.add("String");
				break;
			case Token.NUMBER:
				returnTypeList.add("Number");
				break;
			case Token.TRUE:
			case Token.FALSE:
				returnTypeList.add("Boolean");
				break;
			case Token.REGEXP:
				returnTypeList.add("Regexp");
				break;
			case Token.ARRAYLIT:
				returnTypeList.add("Array");
				if (nextNode.getFirstChild() != null) {
					buildModel(context, nextNode.getFirstChild());
				}
				break;
			case Token.NULL:
				returnTypeList.add("Null");
				break;
			case Token.OBJECTLIT:
				Node objNode = nextNode;
				block = new JavaScriptContext(this, context, objNode.sourceStart, objNode.sourceEnd);
				buildModel(block, objNode);
				context.addContext(block);
				String baseType = "Object:" + node.getString();
				String returnType = baseType;
				int count = 0;
				while (objectTypeMap.containsKey(returnType)) {
					count++;
					returnType = baseType + "_" + count;
				}
				putObjectType(returnType, block);
				returnTypeList.add(returnType);
				break;
			case Token.FUNCTION:
				Node funcNode = nextNode;
				if (funcNode.fnNode != null) {
					nodeDeque.offerLast(funcNode);
					JavaScriptContext dummy = new JavaScriptContext(this, context, 0, 0);
					buildModel(dummy, funcNode.fnNode);
					nodeDeque.pollLast();

					context.addAllContext(dummy.getContexts());
					JavaScriptElement[] elements = dummy.getElements();
					if (elements.length > 0) {
						jsElement = elements[0];
					}
				}
				break;
			case Token.CALL:
				jsElement = getProp(context, nextNode.getFirstChild());
				break;
			case Token.SETPROP:
				jsElement = parseSetProperty(context, nextNode.getFirstChild());
				break;
			case Token.THIS:
				block = context;
				break;
			case Token.NEW:
				jsElement = getProp(context, nextNode.getFirstChild(), false);
				break;
			default:
				break;
			}
		}
		if (jsElement instanceof JavaScriptVariable) {
			JavaScriptAlias jsAlias = new JavaScriptAlias(node.getString(), (JavaScriptVariable) jsElement);
			jsAlias.setStart(node.sourceStart);
			jsAlias.setParent(context.getTarget());
			return jsAlias;
		} else {
			if (jsElement != null) {
				for (String type : jsElement.getReturnTypes()) {
					returnTypeList.add(type);
				}
			}
			JavaScriptVariable jsVar = new JavaScriptVariable(node.getString(), returnTypeList);
			jsVar.setStart(node.sourceStart);
			jsVar.setParent(context.getTarget());
			if (jsElement != null) {
				jsVar.setContext(jsElement.getContext());
				JavaScriptFunction func = jsElement.getFunction();
				if (func != null) {
					jsVar.setFunction(func);
					jsVar.setMethod(true);
				}
			} else if (block != null) {
				jsVar.setContext(block);
				jsVar.createPrototype();
				if (block.getTarget() != null) {
					jsVar.setParent(block.getTarget());
				}
			}
			return jsVar;
		}
	}

	private JavaScriptElement getProp(JavaScriptContext context, Node node) {
		return getProp(context, node, false);
	}

	private JavaScriptElement getProp(JavaScriptContext context, Node node, boolean autoCreate) {
		if (node == null) {
			return null;
		}

		JavaScriptElement jsElement;
		switch (node.getType()) {
		case Token.NAME:
			String name = node.getString();
			jsElement = context.getElementByName(name);
			if (jsElement != null && jsElement.isTemporary() && jsElement.getStart() < 0) {
				jsElement.setStart(node.sourceStart);
			}
			if (jsElement == null && context.getTarget() != null && name.equals(context.getTarget().getName())) {
				jsElement = context.getTarget();
			}
			return jsElement;
		case Token.THIS:
			if (node.getNext() != null) {
				Node nextNode = node.getNext();
				JavaScriptElement element = getProp(context, nextNode);
				if (element == null) {
					if (nextNode.getType() == Token.STRING) {
						// TODO what is the context?
						element = createTemporaryVariable(context, nextNode.getString());
					}
				}
				return element;
			}
			return context.getTarget();
		case Token.STRING:
			jsElement = context.getElementByName(node.getString(), autoCreate);
			if (jsElement != null && jsElement.isTemporary() && jsElement.getStart() < 0) {
				jsElement.setStart(node.sourceStart);
			}
			return jsElement;
		case Token.GETPROP:
			jsElement = getProp(context, node.getFirstChild(), true);
			if (jsElement != null) {
				if (jsElement.getContext() == null) {
					createNewContext(context, jsElement);
				}
				return getProp(jsElement.getContext(), node.getFirstChild().getNext(), true);
			}
			break;
		case Token.FUNCTION:
			FunctionNode fnNode = node.fnNode;
			if (fnNode != null) {
				return createFunction(context, fnNode);
			}
			break;
		case Token.CALL:
			if (node.getFirstChild() != null) {
				Node calledNode = node.getFirstChild();
				JavaScriptElement exportsElement = getExportElement(calledNode);

				if (exportsElement != null) {
					return exportsElement;
				} else {
					return getProp(context, calledNode);
				}
			}
			break;
		default:
			break;
		}

		return null;
	}

	private JavaScriptFunction createFunction(JavaScriptContext context, FunctionNode funcNode) {
		Node firstNode = funcNode.getFirstChild();
		JavaScriptContext block = null;
		String funcName = funcNode.getFunctionName();
		JavaScriptFunction jsFunc = new JavaScriptFunction(funcName);
		jsFunc.setStart(funcNode.sourceStart);
		parseArguments(context, jsFunc, funcNode);
		if (context.getTarget() != null) {
			jsFunc.setParent(context.getTarget());
		}

		if (firstNode != null) {
			switch (firstNode.getType()) {
			case Token.BLOCK:
				block = new JavaScriptContext(this, context, firstNode.sourceStart, firstNode.sourceEnd);
				if (funcName != null && funcName.length() > 0 && jsFunc.isClass()) {
					putObjectType(funcName, block);
				}
				jsFunc.setContext(block);
				jsFunc.createPrototype();
				block.setTarget(jsFunc);
				context.addContext(block);

				// arguments
				JavaScriptVariable argVar = new JavaScriptVariable("arguments", new String[] { "Arguments" });
				argVar.setPrivate(true);
				argVar.setParent(jsFunc);
				block.addElement(argVar);

				buildModel(block, firstNode);

				break;
			default:
				break;
			}
		}

		return jsFunc;
	}

	private void parseArguments(JavaScriptContext context, JavaScriptFunction func, FunctionNode funcNode) {
		// jsDoc
		JsDoc jsDoc = null;
		Map<String, JsDocParam> paramMap = new HashMap<String, JsDocParser.JsDocParam>();
		if (funcNode.jsDocNode != null) {
			String jsDocStr = source.substring(funcNode.jsDocNode.sourceStart, funcNode.jsDocNode.sourceEnd);
			jsDoc = JsDocParser.parse(jsDocStr);
			if (jsDoc != null) {
				for (JsDocParam jsDocParam : jsDoc.params) {
					paramMap.put(jsDocParam.name, jsDocParam);
				}
				if (jsDoc.className != null) {
					func.setClass(true);
				}
				if (jsDoc.isPrivate) {
					func.setPrivate(true);
				}
				if (jsDoc.isStatic) {
					func.setStatic(true);
				}
			}
		}

		// arguments
		if (funcNode.symbols != null) {
			for (Symbol symbol : funcNode.symbols) {
				if (symbol.getDeclType() == Token.LP) {
					String varName = symbol.getName();
					JavaScriptArgument jsArg = new JavaScriptArgument(varName);
					jsArg.setSourceStart(symbol.sourceStart);
					if (symbol.jsDocNode != null) {
						String[] types = JsDocParser.parseInline(
								source.substring(symbol.jsDocNode.sourceStart, symbol.jsDocNode.sourceEnd));
						for (String type : types) {
							jsArg.addType(type);
						}
					}
					JsDocParam jsDocParam = paramMap.get(varName);
					if (jsDocParam != null && !jsDocParam.typeList.isEmpty()) {
						for (String type : jsDocParam.typeList) {
							jsArg.addType(type);
						}
					}
					if (callerArgNode != null) {
						if (callerArgNode.getType() == Token.NAME) {
							JavaScriptElement jsElement = context.getElementByName(callerArgNode.getString());
							if (jsElement != null) {
								jsArg.setJavaScriptElement(jsElement);
							}
						}
						callerArgNode = callerArgNode.getNext();
					}
					func.addArgument(jsArg);
				}
			}
		}

		// returnTypes
		if (jsDoc != null) {
			func.setDescription(jsDoc.text);
			if (!jsDoc.returnTypeList.isEmpty()) {
				for (String returnType : jsDoc.returnTypeList) {
					func.addReturnType(returnType);
				}
			}
		}

	}

	private void addVariable(JavaScriptContext context, Node node) {
		if (!node.isString()) {
			return;
		}
		String name = node.getString();
		List<String> typeList = new ArrayList<String>();
		JavaScriptContext block = null;
		// JavaScriptFunction jsFunc = null;
		JavaScriptElement jsElement = null;
		Node firstNode = node.getFirstChild();
		if (firstNode != null) {
			switch (firstNode.getType()) {
			case Token.OBJECTLIT:
				block = new JavaScriptContext(this, context, firstNode.sourceStart, firstNode.sourceEnd);
				if (firstNode.getFirstChild() != null) {
					buildModel(block, firstNode.getFirstChild());
				}
				context.addContext(block);
				String baseType = "Object:" + name;
				String returnType = baseType;
				int count = 0;
				while (objectTypeMap.containsKey(returnType)) {
					count++;
					returnType = baseType + "_" + count;
				}
				putObjectType(returnType, block);
				typeList.add(returnType);
				break;
			case Token.FUNCTION:
				if (firstNode.fnNode != null) {
					jsElement = createFunction(context, firstNode.fnNode);
				}
				break;
			case Token.STRING:
				typeList.add("String");
				break;
			case Token.NUMBER:
				typeList.add("Number");
				break;
			case Token.TRUE:
			case Token.FALSE:
				typeList.add("Boolean");
				break;
			case Token.REGEXP:
				typeList.add("Regexp");
				break;
			case Token.ARRAYLIT:
				typeList.add("Array");
				if (firstNode.getFirstChild() != null) {
					buildModel(context, firstNode.getFirstChild());
				}
				break;
			case Token.NULL:
				typeList.add("Null");
				break;
			case Token.NEW:
				if (firstNode.getFirstChild() != null && firstNode.getFirstChild().getType() == Token.NAME) {
					Node clsNode = firstNode.getFirstChild();
					JavaScriptElement clsElement = context.getElementByName(clsNode.getString());
					if (clsElement != null) {
						if (clsElement.getFunction() != null && clsElement.getFunction().isClass()) {
							typeList.add(clsElement.getName());
						} else {
							for (String type : clsElement.getReturnTypes()) {
								typeList.add(type);
							}
						}
					}
				} else {// new 一个已知对象时处理 如：var data = new tlv8.Data();
					if (firstNode.getFirstChild().fnNode != null) {
						jsElement = createFunction(context, firstNode.getFirstChild().fnNode);
					} else {
						Node calledNode = firstNode.getFirstChild();
						jsElement = getExportElement(calledNode);
						if (jsElement == null) {
							jsElement = getProp(context, calledNode);
						}
					}
				}
				break;
			case Token.GETPROP:
			case Token.NAME:
				jsElement = getProp(context, firstNode);
				if (jsElement != null && jsElement.isTemporary()) {
					jsElement.setUndefined(true);
				}
				break;
			case Token.CALL:
				if (firstNode.getFirstChild() != null) {
					Node calledNode = firstNode.getFirstChild();
					jsElement = getExportElement(calledNode);
					if (jsElement == null) {
						jsElement = getProp(context, calledNode);
					}
				}
				break;
			default:
				typeList.add("*");
				break;
			}
		} else {
			typeList.add("*");
		}
		if (jsElement instanceof JavaScriptVariable) {
			JavaScriptAlias jsAlias = new JavaScriptAlias(name, (JavaScriptVariable) jsElement);
			jsAlias.setStart(node.sourceStart);
			context.addElement(jsAlias);
		} else {
			if (jsElement != null) {
				for (String type : jsElement.getReturnTypes()) {
					typeList.add(type);
				}
				if (jsElement instanceof JavaScriptFunction && name != null && name.length() > 0
						&& jsElement.getContext() != null) {
					putObjectType(name, jsElement.getContext());
				}
			}
			JavaScriptVariable jsVar = new JavaScriptVariable(name, typeList);
			jsVar.setStart(node.sourceStart);
			if (block != null) {
				jsVar.setContext(block);
				block.setTarget(jsVar);
			} else if (jsElement != null) {
				jsVar.setContext(jsElement.getContext());
				jsVar.setFunction(jsElement.getFunction());
			}
			if (context.getTarget() != null) {
				jsVar.setParent(context.getTarget());
			}
			context.addElement(jsVar);
		}
	}

	private JavaScriptElement getExportElement(Node calledNode) {
		if (calledNode.getType() == Token.NAME
				&& ("require".equals(calledNode.getString()) || "requireNative".equals(calledNode.getString()))
				&& calledNode.getNext() != null && calledNode.getNext().getType() == Token.STRING) {
			String moduleName = calledNode.getNext().getString().trim();
			if (moduleName.startsWith(".")) {
				if (jsFile instanceof IFile) {
					IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
					IFile f = (IFile) jsFile;
					IResource childFile = wsroot
							.findMember(f.getParent().getFullPath().toString() + "/" + moduleName + ".js");
					if (childFile != null && childFile.exists() && childFile instanceof IFile) {
						JavaScriptModel includedModel = includedModelMap.get(childFile);
						if (includedModel == null) {
							InputStream in = null;
							try {
								in = ((IFile) childFile).getContents();
								String source = new String(IOUtil.readStream(in));
								includedModel = new JavaScriptModel(childFile, source, modelList, includedModelMap);
							} catch (Exception e) {
								WebToolsPlugin.logException(e);
							} finally {
								IOUtil.closeQuietly(in);
							}
						}
						if (includedModel != null) {
							requirePathDataList.add(new RequirePathData(childFile, calledNode.getNext()));
							objectTypeMap.putAll(includedModel.objectTypeMap);
							return includedModel.getElementByName("exports", false);
						}
					}
				} else if (jsFile instanceof File) {
					File f = (File) jsFile;
					File childFile = new File(f, moduleName + ".js");
					if (childFile.isFile()) {
						JavaScriptModel includedModel = includedModelMap.get(childFile);
						if (includedModel == null) {
							InputStream in = null;
							try {
								in = new BufferedInputStream(new FileInputStream(childFile));
								String source = new String(IOUtil.readStream(in));
								includedModel = new JavaScriptModel(childFile, source, modelList, includedModelMap);
							} catch (Exception e) {
								WebToolsPlugin.logException(e);
							} finally {
								IOUtil.closeQuietly(in);
							}
						}
						if (includedModel != null) {
							requirePathDataList.add(new RequirePathData(childFile, calledNode.getNext()));
							objectTypeMap.putAll(includedModel.objectTypeMap);
							return includedModel.getElementByName("exports", false);
						}
					}
				}
			} else if (moduleName.length() > 0) {
				String[] libPaths = ModelManager.getInstance().getLibPaths(jsFile);
				IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
				for (String dir : libPaths) {
					if (dir.startsWith(JavaScriptLibPathTable.PREFIX)) {
						IResource resource = wsroot.findMember(
								dir.substring(JavaScriptLibPathTable.PREFIX.length()) + "/" + moduleName + ".js");
						if (resource != null && resource instanceof IFile && resource.exists()) {
							JavaScriptModel includedModel = includedModelMap.get(resource);
							if (includedModel == null) {
								InputStream in = null;
								try {
									in = ((IFile) resource).getContents();
									String source = new String(IOUtil.readStream(in));
									includedModel = new JavaScriptModel(resource, source, modelList, includedModelMap);
								} catch (Exception e) {
									WebToolsPlugin.logException(e);
								} finally {
									IOUtil.closeQuietly(in);
								}
							}
							if (includedModel != null) {
								requirePathDataList.add(new RequirePathData(resource, calledNode.getNext()));
								objectTypeMap.putAll(includedModel.objectTypeMap);
								return includedModel.getElementByName("exports", false);
							}
						}
					} else {
						File file = new File(dir, moduleName + ".js");
						if (file.isFile()) {
							JavaScriptModel includedModel = includedModelMap.get(file);
							if (includedModel == null) {
								InputStream in = null;
								try {
									in = new BufferedInputStream(new FileInputStream(file));
									String source = new String(IOUtil.readStream(in));
									includedModel = new JavaScriptModel(file, source, modelList, includedModelMap);
								} catch (Exception e) {
									WebToolsPlugin.logException(e);
								} finally {
									IOUtil.closeQuietly(in);
								}
							}
							if (includedModel != null) {
								requirePathDataList.add(new RequirePathData(file, calledNode.getNext()));
								objectTypeMap.putAll(includedModel.objectTypeMap);
								return includedModel.getElementByName("exports", false);
							}
						}
					}
				}
			}
		}
		return null;
	}

	public JavaScriptVariable createTemporaryVariable(JavaScriptContext context, String str) {
		String baseType = "Object:" + str;
		String returnType = baseType;
		int count = 0;
		while (objectTypeMap.containsKey(returnType)) {
			count++;
			returnType = baseType + "_" + count;
		}
		JavaScriptVariable jsVar = new JavaScriptVariable(str, new String[] { returnType });
		jsVar.setTemporary(true);
		jsVar.setParent(context.getTarget());
		JavaScriptContext jsContext = new JavaScriptContext(context.model, context, 0, 0);
		jsVar.setContext(jsContext);
		context.addElement(jsVar);
		putObjectType(returnType, jsContext);
		return jsVar;
	}

	private void createNewContext(JavaScriptContext context, JavaScriptElement jsElement) {
		JavaScriptContext jsContext = new JavaScriptContext(this, context, 0, 0);
		jsElement.setContext(jsContext);
		String[] types = jsElement.getTypes();
		if (types.length == 0 || (types.length == 1 && "*".equals(types[0]))) {
			String baseType = "Object:" + jsElement.getName();
			String returnType = baseType;
			int count = 0;
			while (objectTypeMap.containsKey(returnType)) {
				count++;
				returnType = baseType + "_" + count;
			}
			putObjectType(returnType, jsContext);
			jsElement.typeList.remove("*");
			jsElement.typeList.add(returnType);
		}
	}

	private static class RhinoErrorReporter implements ErrorReporter {

		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
			// TODO Auto-generated method stub
		}

		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
			// TODO Auto-generated method stub
		}

		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
				int lineOffset) {
			return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
		}

	}

	public List<JavaScriptComment> getCommentList() {
		return commentList;
	}

	public Object getJavaScriptFile() {
		return jsFile;
	}

	public JavaScriptElement getGlobalObject() {
		return global;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	private void putObjectType(String type, JavaScriptContext jsContext) {
		objectTypeMap.put(type, jsContext);
	}

	public RequirePathData getRequirePathData(int offset) {
		for (RequirePathData data : requirePathDataList) {
			if (data.getStart() < offset && offset <= data.getEnd()) {
				return data;
			}
		}
		return null;
	}

	public JavaScriptModel getIncludedModel(Object key) {
		return includedModelMap.get(key);
	}

	public static class RequirePathData {
		int start;
		int end;
		Object file;

		RequirePathData(Object file, Node node) {
			this.file = file;
			String str = node.getString();
			// TODO node.sourceStart is a position for an end of the word...
			start = node.sourceStart - str.length() - 1;
			end = node.sourceStart - 1;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public Object getFile() {
			return file;
		}
	}

	public static class Deque<E> {
		private LinkedList<E> deque = new LinkedList<E>();

		public E peekLast() {
			if (deque.size() == 0)
				return null;
			return deque.getLast();
		}

		public boolean offerLast(E e) {
			deque.addLast(e);
			return true;
		}

		public E pollLast() {
			if (deque.size() == 0)
				return null;
			return deque.removeLast();
		}
	}
}
