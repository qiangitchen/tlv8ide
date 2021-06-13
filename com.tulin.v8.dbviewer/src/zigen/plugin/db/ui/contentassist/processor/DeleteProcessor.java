/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist.processor;

import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.ui.contentassist.ContentAssistUtil;
import zigen.plugin.db.ui.contentassist.ContentInfo;
import zigen.plugin.db.ui.contentassist.ProcessorInfo;
import zigen.plugin.db.ui.contentassist.SQLProposalCreator2;
import zigen.sql.parser.SqlParser;
import zigen.sql.parser.ast.ASTDeleteStatement;
import zigen.sql.parser.ast.ASTFrom;

public class DeleteProcessor extends DefaultProcessor {

	public DeleteProcessor(List proposals, ProcessorInfo info) {
		super(proposals, info);
	}

	public void createProposals(ASTDeleteStatement st) {

		String[] modifiers = rule.getKeywordNames();
		try {
			ContentInfo ci = new ContentInfo(ContentAssistUtil.getIDBConfig());

			if (ci.isConnected()) {

				TableInfo[] currentTableInfos = ci.getTableInfo();

				ASTFrom fromList = super.findASTFrom(st);
				int fromItemCount = fromList != null ? super.getSizeRemoveComma(fromList) : 0;

				switch (currentScope) {
				case SqlParser.SCOPE_DELETE:
					modifiers = new String[] {"from" //$NON-NLS-1$
					};
					break;

				case SqlParser.SCOPE_FROM:
					//SQLProposalCreator2.addProposal(proposals, currentTableInfos, pinfo);
					if (isAfterPeriod) {
						addTableProposalBySchema(ci, word);

					} else {
						int _offset = wordGroup.indexOf('.');
						if (_offset > 0) {
							String w_schema = wordGroup.substring(0, _offset);
							addTableProposalBySchema(ci, w_schema);
						} else {
							SQLProposalCreator2.addProposal(proposals, currentTableInfos, pinfo);
							SQLProposalCreator2.addProposal(proposals, ci.getSchemaInfos(), pinfo);
						}

					}

					break;

				case SqlParser.SCOPE_WHERE:
					if (fromItemCount == 0) {
						;
					} else if (fromItemCount == 1) {
						createColumnProposal(fromList.getChild(0));
						break;
					}
					break;
				}
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {
			SQLProposalCreator2.addProposal(proposals, modifiers, pinfo);
		}

	}

}
