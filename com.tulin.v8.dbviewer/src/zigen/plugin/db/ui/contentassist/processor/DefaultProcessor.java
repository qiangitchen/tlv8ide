/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist.processor;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPluginFormatRule;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.parser.util.ASTUtil2;
import zigen.plugin.db.ui.contentassist.ContentAssistUtil;
import zigen.plugin.db.ui.contentassist.ContentInfo;
import zigen.plugin.db.ui.contentassist.ProcessorInfo;
import zigen.plugin.db.ui.contentassist.SQLProposalCreator2;
import zigen.plugin.db.ui.internal.Column;
import zigen.sql.parser.INode;
import zigen.sql.parser.ast.ASTAlias;
import zigen.sql.parser.ast.ASTColumn;
import zigen.sql.parser.ast.ASTComma;
import zigen.sql.parser.ast.ASTDeleteStatement;
import zigen.sql.parser.ast.ASTFrom;
import zigen.sql.parser.ast.ASTInsertStatement;
import zigen.sql.parser.ast.ASTInto;
import zigen.sql.parser.ast.ASTParentheses;
import zigen.sql.parser.ast.ASTSelect;
import zigen.sql.parser.ast.ASTSelectStatement;
import zigen.sql.parser.ast.ASTTable;
import zigen.sql.parser.ast.ASTUnion;
import zigen.sql.parser.ast.ASTUpdateStatement;

public class DefaultProcessor {

	protected List proposals;

	protected ProcessorInfo pinfo;

	protected String wordGroup;

	protected String word;

	protected int offset;

	protected boolean isAfterPeriod;

	protected int currentScope;

	protected INode currentNode;

	protected DbPluginFormatRule rule;

	public DefaultProcessor(List proposals, ProcessorInfo pinfo) {
		this.proposals = proposals;
		this.pinfo = pinfo;
		this.wordGroup = pinfo.getWordGroup();
		this.word = pinfo.getWord();
		this.offset = pinfo.getOffset();
		this.isAfterPeriod = pinfo.isAfterPeriod();
		this.currentScope = pinfo.getCurrentScope();
		this.currentNode = pinfo.getCurrentNode();
		this.rule = DbPluginFormatRule.getInstance();
	}

	protected ASTSelectStatement findParentASTSelectStatement(INode node) {
		return (ASTSelectStatement) ASTUtil2.findParent(node, "ASTSelectStatement"); //$NON-NLS-1$
	}

	protected ASTInsertStatement findParentASTInsertStatement(INode node) {
		return (ASTInsertStatement) ASTUtil2.findParent(node, "ASTInsertStatement"); //$NON-NLS-1$
	}

	protected ASTUpdateStatement findParentASTUpdateStatement(INode node) {
		return (ASTUpdateStatement) ASTUtil2.findParent(node, "ASTUpdateStatement"); //$NON-NLS-1$
	}

	protected ASTDeleteStatement findParentASTDeleteStatement(INode node) {
		return (ASTDeleteStatement) ASTUtil2.findParent(node, "ASTDeleteStatement"); //$NON-NLS-1$
	}

	protected ASTFrom findASTFrom(ASTSelectStatement node) {
		return (ASTFrom) node.getChild("ASTFrom");
	}

	protected ASTSelect findASTSelect(ASTSelectStatement node) {
		return (ASTSelect) node.getChild("ASTSelect");
	}

	protected ASTFrom findASTFrom(ASTDeleteStatement node) {
		return (ASTFrom) ASTUtil2.findFirstChild(node, "ASTFrom"); //$NON-NLS-1$
	}

	protected ASTSelectStatement findASTSelectStatement(ASTUnion node) {
		return (ASTSelectStatement) ASTUtil2.findFirstChild(node, "ASTSelectStatement"); //$NON-NLS-1$
	}

	protected ASTTable findASTTable(ASTInsertStatement node) {
		ASTInto into = (ASTInto) ASTUtil2.findFirstChild(node, "ASTInto"); //$NON-NLS-1$
		return (ASTTable) ASTUtil2.findFirstChild(into, "ASTTable"); //$NON-NLS-1$
	}

	protected ASTTable findASTTable(ASTUpdateStatement node) {
		if (node.getChildrenSize() > 0) {
			INode n = node.getChild(0);
			if (n != null) {
				if (n instanceof ASTTable) {
					return (ASTTable) n;
				}
			}
		}
		return null;
	}

	protected INode findFromNode(ASTFrom fromlist, String aliasName) {
		if (fromlist != null) {
			for (int i = 0; i < fromlist.getChildrenSize(); i++) {
				INode node = fromlist.getChild(i);
				if (node instanceof ASTAlias) {
					//System.out.println(((ASTAlias) node).getAliasName());
					//System.out.println(((ASTAlias) node).getName());
					if (aliasName.equalsIgnoreCase(((ASTAlias) node).getAliasName())) {
						return node;

					}else if (aliasName.equalsIgnoreCase(((ASTAlias) node).getName())) {	// schema.table
						return node;
					}
				}
			}
		}
		return null;
	}

	protected INode[] getFromNodes(ASTFrom fromlist) {
		List list = new ArrayList();
		for (int i = 0; i < fromlist.getChildrenSize(); i++) {
			INode node = fromlist.getChild(i);
			if (node instanceof ASTAlias) {
				list.add(node);
			}
		}
		return (INode[]) list.toArray(new INode[0]);

	}

	protected int getSizeRemoveComma(INode target) {
		int cnt = 0;
		for (int i = 0; i < target.getChildrenSize(); i++) {
			INode node = target.getChild(i);
			if (!(node instanceof ASTComma)) {
				cnt++;
			}
		}
		return cnt;
	}

	protected void createTableProposal(TableInfo[] infos, ASTAlias[] target) {
		if (target != null) {
			List list = new ArrayList();
			for (int i = 0; i < target.length; i++) {
				INode node = target[i];
				if (node instanceof ASTTable) {
					ASTTable table = (ASTTable) node;
					TableInfo info = findTableInfo(infos, table.getTableName());
					if (table.hasAlias()) {
						String comment = info.getComment();
						if (comment == null)
							comment = info.getName();
						list.add(new TableInfo(table.getAliasName(), comment + Messages.getString("DefaultProcessor.10"))); //$NON-NLS-1$
					} else {
						list.add(new TableInfo(table.getTableName(), info.getComment()));
					}
				} else if (node instanceof ASTAlias) {
					ASTAlias alias = (ASTAlias) node;
					if (alias.getAliasName() != null) {
						list.add(new TableInfo(alias.getAliasName(), Messages.getString("DefaultProcessor.11"))); //$NON-NLS-1$
					}
				}
			}
			SQLProposalCreator2.addProposal(proposals, (TableInfo[]) list.toArray(new TableInfo[0]), pinfo);
		}
	}

	protected TableInfo findTableInfo(TableInfo[] info, String target) {
		for (int i = 0; i < info.length; i++) {
			TableInfo ti = info[i];
			if (ti.getName().equalsIgnoreCase(target)) {
				return ti;
			}
		}
		return null;
	}

	protected void createColumnProposal(INode target) {
		if (target != null) {
			if (target instanceof ASTTable) {
				createColumn((ASTTable) target);

			} else if (target instanceof ASTSelectStatement) {
				createColumn((ASTSelectStatement) target);

			} else if (target instanceof ASTParentheses) {
				ASTSelectStatement select = (ASTSelectStatement) ASTUtil2.findFirstChild(target, "ASTSelectStatement"); //$NON-NLS-1$
				if (select != null) {
					createColumnProposal(select);
				} else {
					throw new IllegalStateException(Messages.getString("DefaultProcessor.13")); //$NON-NLS-1$
				}
			}
		}

	}

	void createColumn(ASTTable target) {
		if (target != null) {
			ContentInfo ci = new ContentInfo(ContentAssistUtil.getIDBConfig());
			if (ci.isConnected()) {

				String schemaName = ((ASTTable) target).getSchemaName();
				if(schemaName == null) schemaName = ci.getCurrentSchema();
				String tableName = ((ASTTable) target).getTableName();
				Column[] cols = ci.getColumns(schemaName, tableName);
				SQLProposalCreator2.addProposal(proposals, cols, pinfo);
			}
		}

	}

	void createColumn(ASTSelectStatement target) {
		if (target != null) {
			String alias = target.getAliasName();
			ASTSelect selectList = findASTSelect(target);

			int count = getSizeRemoveComma(selectList);
			String[][] colInfo = new String[count][2];

			int index = 0;
			for (int i = 0; i < selectList.getChildrenSize(); i++) {
				INode node = selectList.getChild(i);
				StringBuffer sb = new StringBuffer();
				if (node instanceof ASTColumn) {
					ASTColumn column = (ASTColumn) node;
					String columnName = column.getAliasName();
					sb.append(columnName);

					if (alias != null) {
						sb.append(Messages.getString("DefaultProcessor.14")); //$NON-NLS-1$
						sb.append(alias);
						sb.append(Messages.getString("DefaultProcessor.15")); //$NON-NLS-1$
						sb.append(columnName);
					}

					colInfo[index][0] = columnName;
					colInfo[index][1] = sb.toString();

					index++;
				}
			}
			SQLProposalCreator2.addProposal(proposals, colInfo, pinfo);
		}

	}

	protected boolean addTableProposalBySchema(ContentInfo ci, String inputWord)throws Exception{
		String correctSchemaName = ci.findCorrectSchema(inputWord);
		if (correctSchemaName != null){
			SQLProposalCreator2.addProposal(proposals, ci.getTableInfo(correctSchemaName), pinfo);
			return true;
		}else{
			return false;
		}
	}
}
