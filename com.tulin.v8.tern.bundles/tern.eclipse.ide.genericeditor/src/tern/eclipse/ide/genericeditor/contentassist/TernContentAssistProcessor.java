package tern.eclipse.ide.genericeditor.contentassist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import tern.ITernFile;
import tern.ITernProject;
import tern.TernResourcesManager;
import tern.eclipse.ide.core.resources.TernDocumentFile;
import tern.eclipse.ide.genericeditor.contentassist.ITernContextProvider.TernContext;
import tern.eclipse.ide.ui.contentassist.TernCompletionsQueryFactory;
import tern.eclipse.ide.ui.utils.EditorUtils;
import tern.server.protocol.ITernResultsAsyncCollector.TimeoutReason;
import tern.server.protocol.completions.TernCompletionsQuery;

/**
 * JS代码提示
 */
public class TernContentAssistProcessor implements IContentAssistProcessor {

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		TernContext ternContext = null;
		ITernProject ternProject = null;
		IDocument document = viewer.getDocument();
		IFile file = EditorUtils.getFile(document);
		if (file != null) {
			IProject project = file.getProject();
			try {
				ternProject = TernResourcesManager.getTernProject(project, true);
			} catch (IOException e) {
				// ignore error
			}
			if (ternProject != null) {
				ITernFile tf = new TernDocumentFile(file, document);
				ternContext = new TernContext(ternProject, tf, offset);
			}
		}
		if (ternContext != null) {
			try {
				final List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
				ITernFile tf = ternContext.file;
				IProject project = (IProject) ternProject.getAdapter(IProject.class);

				int startOffset = ternContext.invocationOffset;
				String filename = tf.getFullName(ternProject);
				TernCompletionsQuery query = TernCompletionsQueryFactory.createQuery(project, filename, startOffset);
				ternProject.request(query, tf,
						new GenericeditorTernCompletionCollector(proposals, startOffset, tf, ternProject));
				ICompletionProposal[] completionProposals = new ICompletionProposal[proposals.size()];
				for (int i = 0; i < proposals.size(); i++) {
					completionProposals[i] = proposals.get(i);
				}
				return completionProposals;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (ternProject != null) {
			GenericeditorTimeoutProposal timeoutProposal = new GenericeditorTimeoutProposal(offset,
					TimeoutReason.TIMED_OUT);
			return new ICompletionProposal[] { timeoutProposal };
		}
		return null;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return new IContextInformation[] {};
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}
