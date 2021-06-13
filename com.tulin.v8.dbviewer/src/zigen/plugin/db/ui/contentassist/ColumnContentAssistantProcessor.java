/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import java.util.ArrayList;

import org.eclipse.jface.contentassist.IContentAssistSubjectControl;
import org.eclipse.jface.contentassist.ISubjectControlContentAssistProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import zigen.plugin.db.ui.internal.Column;

public class ColumnContentAssistantProcessor implements IContentAssistProcessor, ISubjectControlContentAssistProcessor {

	static final String[] Keywords = {"AND", "ASC", "BETWEEN", "BY", "DESC", "EXISTS", "IN", "IS NULL", "IS NOT NULL", "LIKE", "NOT", "NOT EXISTS", "NULL", "OR", "ORDER BY"};

	Column[] columns;

	public ColumnContentAssistantProcessor(Column[] columns) {
		this.columns = columns;
	}

	private class ProposalComputer {

		private final int fDocumentOffset;

		private final ArrayList fProposals;

		IDocument fDocument;

		public ProposalComputer(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
			fDocument = contentAssistSubjectControl.getDocument();
			fDocumentOffset = documentOffset;
			fProposals = new ArrayList();
		}

		public ICompletionProposal[] computeProposals() {
			ProcessorInfo pinfo = new ProcessorInfo();
			pinfo.setAfterPeriod(ContentAssistUtil.isAfterPeriod(fDocument, fDocumentOffset));
			pinfo.setOffset(fDocumentOffset);
			pinfo.setWord(ContentAssistUtil.getPreviousWord(fDocument, fDocumentOffset).toLowerCase());
			pinfo.setWordGroup(ContentAssistUtil.getPreviousWordGroup(fDocument, fDocumentOffset).toLowerCase());

			SQLProposalCreator2.addProposal(fProposals, columns, pinfo);
			SQLProposalCreator2.addProposal(fProposals, Keywords, pinfo);

			return (ICompletionProposal[]) fProposals.toArray(new ICompletionProposal[fProposals.size()]);
		}

	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		throw new UnsupportedOperationException("ITextViewer not supported"); //$NON-NLS-1$
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {};
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return new char[] {};
	}

	public String getErrorMessage() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	public ICompletionProposal[] computeCompletionProposals(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		return new ProposalComputer(contentAssistSubjectControl, documentOffset).computeProposals();
	}

	public IContextInformation[] computeContextInformation(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		return null;
	}


}
