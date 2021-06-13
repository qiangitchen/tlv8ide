/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.DbPluginFormatRule;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.parser.util.ASTUtil2;
import zigen.plugin.db.parser.util.CurrentSql;
import zigen.plugin.db.preference.CodeAssistPreferencePage;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.preference.SQLTemplateEditorUI;
import zigen.plugin.db.ui.contentassist.processor.DeleteProcessor;
import zigen.plugin.db.ui.contentassist.processor.DropProcessor;
import zigen.plugin.db.ui.contentassist.processor.InsertProcessor;
import zigen.plugin.db.ui.contentassist.processor.SelectProcessor;
import zigen.plugin.db.ui.contentassist.processor.UpdateProcessor;
import zigen.plugin.db.ui.views.internal.SQLContextType;
import zigen.sql.parser.ASTVisitor2;
import zigen.sql.parser.INode;
import zigen.sql.parser.ISqlParser;
import zigen.sql.parser.SqlParser;
import zigen.sql.parser.ast.ASTDeleteStatement;
import zigen.sql.parser.ast.ASTDropStatement;
import zigen.sql.parser.ast.ASTInsertStatement;
import zigen.sql.parser.ast.ASTRoot;
import zigen.sql.parser.ast.ASTSelectStatement;
import zigen.sql.parser.ast.ASTUpdateStatement;
import zigen.sql.parser.exception.ParserException;

public class SQLContentAssistantProcessor2 extends TemplateCompletionProcessor implements IContentAssistProcessor {

	private static final class ProposalComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			return ((TemplateProposal) o2).getRelevance() - ((TemplateProposal) o1).getRelevance();
		}
	}

	private static final Comparator fgProposalComparator = new ProposalComparator();

	private int current = -1;

	private int scope = -1;

	private INode currentNode;

	private IPreferenceStore preferenceStore;

	private DbPluginFormatRule rule;

	public SQLContentAssistantProcessor2() {
		this.preferenceStore = DbPlugin.getDefault().getPreferenceStore();
		this.current = -1;
		this.scope = -1;
		this.currentNode = null;
		this.rule = DbPluginFormatRule.getInstance();
	}


	class SQLParserTimeout extends ParserException {

		private static final long serialVersionUID = 1L;

		public SQLParserTimeout(String message) {
			super(message, null, 0, 0);
		}

	}

	private Object lock = new Object();

	boolean isSQLParsing = false;

	class SQLParseThread implements Runnable {

		IDocument doc;

		int offset;

		String demiliter;

		boolean _isComplete = false;

		public SQLParseThread(IDocument doc, int offset, String demiliter) {
			this.doc = doc;
			this.offset = offset;
			this.demiliter = demiliter;
		}

		public void run() {

			if (isSQLParsing) {
				return;
			}
			synchronized (lock) {
				try {
					isSQLParsing = true;
					CurrentSql cs = new CurrentSql(doc, offset, demiliter);
					String allSql = cs.getSql();
					if (allSql != null && !"".equals(allSql.trim())) {

						TimeWatcher tw = new TimeWatcher();
						tw.start();
						SqlParser parser = new zigen.sql.parser.SqlParser(allSql, DbPlugin.getSqlFormatRult());
						ASTVisitor2 visitor = new zigen.sql.parser.ASTVisitor2();
						INode node = new ASTRoot(); //$NON-NLS-1$
						parser.parse(node);
						node.accept(visitor, null);
						currentNode = findCurrentNode(visitor, cs.getOffsetSql());


						if (currentNode == null) {
							StringBuffer sb = new StringBuffer();
							sb.append("The node is not found. ").append(DbPluginConstant.LINE_SEP);
							sb.append("all SQL is ").append(DbPluginConstant.LINE_SEP);
							sb.append(allSql).append(DbPluginConstant.LINE_SEP);
							sb.append("until ofset sql is ").append(DbPluginConstant.LINE_SEP);
							sb.append(cs.getOffsetSql()).append(DbPluginConstant.LINE_SEP);
							DbPlugin.log(sb.toString());
						} else {
						}
						tw.stop();
					}
				} finally {
					_isComplete = true;
					isSQLParsing = false;
				}

			}

		}

		public boolean isComplete() {
			return _isComplete;
		}


	}

	private void parseSql(IDocument doc, int offset, ProcessorInfo pinfo, String demiliter) throws SQLParserTimeout {
		zigen.sql.parser.ISqlParser parser = null;
		zigen.sql.parser.ASTVisitor2 visitor = null;
		try {
			currentNode = null;
			int timeout = 1;
			SQLParseThread t = new SQLParseThread(doc, offset, demiliter);
			Thread th = new Thread(t);
			th.setPriority(Thread.MIN_PRIORITY);
			th.start();

			try {
				if (timeout > 0) {
					th.join(timeout * 1000);
				} else {
					th.join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (t.isComplete()) {
				pinfo.setCurrentNode(currentNode);
				if (currentNode != null) {
					pinfo.setCurrentScope(currentNode.getScope());
				} else {
					pinfo.setCurrentScope(ISqlParser.SCOPE_DEFAULT);
				}
			} else {
				currentNode = null;
				pinfo.setCurrentNode(currentNode);
				pinfo.setCurrentScope(ISqlParser.SCOPE_DEFAULT);
				throw new SQLParserTimeout("SQL Parse is timeout.");
			}

		} finally {
			if (parser != null)
				parser = null;
			if (visitor != null)
				visitor = null;
		}

	}

	private INode findCurrentNode(ASTVisitor2 visitor, String offsetSql) {
		int nodeOffset = StringUtil.endWordPosition(offsetSql);
		INode node = visitor.findNodeByOffset(nodeOffset);
		if (node != null) {
			return node;
		} else {
			return findCurrentNode(visitor, offsetSql.substring(0, nodeOffset));
		}
	}


	private void addTemplateProposal(List proposals, ICompletionProposal[] templates, ProcessorInfo pinfo) {
		if (templates != null) {
			String word = pinfo.getWord();
			if (pinfo.isAfterPeriod())
				word = "";
			int len = word.length();
			for (int i = 0; i < templates.length; i++) {
				ICompletionProposal template = templates[i];
				String str = template.getDisplayString();
				String value = ContentAssistUtil.subString(str, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(template);

				}
			}
		}
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		List proposals = new ArrayList();
		try {
			String mode = preferenceStore.getString(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_MODE);
			String demiliter = preferenceStore.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
			IDocument doc = viewer.getDocument();
			ProcessorInfo pinfo = new ProcessorInfo();
			pinfo.setAfterPeriod(ContentAssistUtil.isAfterPeriod(doc, offset));
			pinfo.setOffset(offset);
			pinfo.setWord(ContentAssistUtil.getPreviousWord(doc, offset).toLowerCase());
			pinfo.setWordGroup(ContentAssistUtil.getPreviousWordGroup(doc, offset).toLowerCase());

			if (mode.equals(CodeAssistPreferencePage.MODE_NONE)) {
				return null;

			} else if (mode.equals(CodeAssistPreferencePage.MODE_KEYWORD)) {
				SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);
				SQLProposalCreator2.addProposalForFunction(proposals, rule.getFunctionNames(), pinfo);

			} else if (mode.equals(CodeAssistPreferencePage.MODE_PARSE)) {
				try {
					parseSql(doc, offset, pinfo, demiliter);


					INode st = null;
					if ((st = ASTUtil2.findParent(currentNode, "ASTSelectStatement")) != null) { //$NON-NLS-1$
						SelectProcessor p = new SelectProcessor(proposals, pinfo);
						p.createProposals((ASTSelectStatement) st);
					} else if ((st = ASTUtil2.findParent(currentNode, "ASTInsertStatement")) != null) { //$NON-NLS-1$
						InsertProcessor p = new InsertProcessor(proposals, pinfo);
						p.createProposals((ASTInsertStatement) st);
					} else if ((st = ASTUtil2.findParent(currentNode, "ASTUpdateStatement")) != null) { //$NON-NLS-1$
						UpdateProcessor p = new UpdateProcessor(proposals, pinfo);
						p.createProposals((ASTUpdateStatement) st);
					} else if ((st = ASTUtil2.findParent(currentNode, "ASTDeleteStatement")) != null) { //$NON-NLS-1$
						DeleteProcessor p = new DeleteProcessor(proposals, pinfo);
						p.createProposals((ASTDeleteStatement) st);
					} else if ((st = ASTUtil2.findParent(currentNode, "ASTDropStatement")) != null) { //$NON-NLS-1$
						DropProcessor p = new DropProcessor(proposals, pinfo);
						p.createProposals((ASTDropStatement) st);
					} else {
						// SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);
					}

				} catch (SQLParserTimeout e) {
					SQLProposalCreator2.addProposal(proposals, rule.getKeywordNames(), pinfo);
				}
			}
			ICompletionProposal[] templates = super.computeCompletionProposals(viewer, offset);
			addTemplateProposal(proposals, templates, pinfo);

			ICompletionProposal[] tFunctions = computeCompletionProposalsForFunction(viewer, offset);
			addTemplateProposal(proposals, tFunctions, pinfo);


		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[0]);
	}


	protected TemplateContext createContext(ITextViewer viewer, IRegion region) {
		TemplateContextType contextType = getContextType(viewer, region);
		if (contextType != null) {
			IDocument document = viewer.getDocument();
			// return new DocumentTemplateContext(contextType, document, region.getOffset(), region.getLength());
			return new SQLTemplateContext(contextType, document, region.getOffset(), region.getLength());
		}
		return null;
	}

	public ICompletionProposal[] computeCompletionProposalsForFunction(ITextViewer viewer, int offset) {
		ITextSelection selection = (ITextSelection) viewer.getSelectionProvider().getSelection();
		// adjust offset to end of normalized selection
		if (selection.getOffset() == offset)
			offset = selection.getOffset() + selection.getLength();
		String prefix = extractPrefix(viewer, offset);
		Region region = new Region(offset - prefix.length(), prefix.length());
		TemplateContext context = createContextForFunction(viewer, region);
		if (context == null)
			return new ICompletionProposal[0];
		context.setVariable("selection", selection.getText()); // name of the selection variables {line, word}_selection //$NON-NLS-1$
		Template[] templates = getTemplates(context.getContextType().getId());
		List matches = new ArrayList();
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			try {
				context.getContextType().validate(template.getPattern());
			} catch (TemplateException e) {
				continue;
			}
			if (template.matches(prefix, context.getContextType().getId()))
				matches.add(createProposalForFunction(template, context, (IRegion) region, getRelevance(template, prefix)));
		}

		Collections.sort(matches, fgProposalComparator);

		return (ICompletionProposal[]) matches.toArray(new ICompletionProposal[matches.size()]);
	}

	protected TemplateContext createContextForFunction(ITextViewer viewer, IRegion region) {
		TemplateContextType contextType = getContextTypeForFunction(viewer, region);
		if (contextType != null) {
			IDocument document = viewer.getDocument();
			// return new DocumentTemplateContext(contextType, document, region.getOffset(), region.getLength());
			return new SQLTemplateContext(contextType, document, region.getOffset(), region.getLength());

		}
		return null;
	}

	protected TemplateContextType getContextTypeForFunction(ITextViewer viewer, IRegion region) {
		return SQLTemplateEditorUI.getDefault().getContextTypeRegistry().getContextType(SQLContextType.CONTEXT_TYPE_FUNCTION);
	}

	protected ICompletionProposal createProposalForFunction(Template template, TemplateContext context, IRegion region, int relevance) {
		return new SQLTemplateProposal(template, context, region, getImageForFunction(template), relevance);
	}

	protected Image getImageForFunction(Template template) {
		ImageCacher ic = ImageCacher.getInstance();
		return ic.getImage(DbPlugin.IMG_CODE_FUNCTION);
	}


	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'.'};
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	public String getErrorMessage() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	public static String[] SQL_OPERATOR = {"=", "<", ">", "IS NULL", "IS NOT NULL", "LIKE", "IN", "EXIST", "(+)", "||", "<=", ">=", "<>", "(", ")", "+", "-", "*", "/"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$

	protected Template[] getTemplates(String contextTypeId) {
		return SQLTemplateEditorUI.getDefault().getTemplateStore().getTemplates();
	}

	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		return SQLTemplateEditorUI.getDefault().getContextTypeRegistry().getContextType(SQLContextType.CONTEXT_TYPE_SQL);
	}

	protected Image getImage(Template template) {
		ImageCacher ic = ImageCacher.getInstance();
		return ic.getImage(DbPlugin.IMG_CODE_TEMPLATE);
	}


	protected String extractPrefix(ITextViewer viewer, int offset) {
		IDocument document = viewer.getDocument();
		int i = offset;
		if (i > document.getLength())
			return ""; //$NON-NLS-1$
		try {
			while (i > 0) {
				char ch = document.getChar(i - 1);
				if (ch != '<' && !Character.isJavaIdentifierPart(ch))
					break;
				i--;
			}
			return document.get(i, offset - i);
		} catch (BadLocationException e) {
			return ""; //$NON-NLS-1$
		}
	}

	protected int getRelevance(Template template, String prefix) {
		if (template.getName().startsWith(prefix)) {
			return 200;
		}
		return 0;
	}

	protected ICompletionProposal createProposal(Template template, TemplateContext context, IRegion region, int relevance) {
		return new SQLTemplateProposal(template, context, region, getImage(template), relevance);
	}

}
