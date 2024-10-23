package tern.eclipse.ide.jsdt.internal.ui.contentassist;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

import tern.ITernFile;
import tern.ITernProject;
import tern.eclipse.ide.ui.contentassist.JSTernCompletionAsyncCollector;
import tern.eclipse.ide.ui.contentassist.JSTernCompletionProposal;
import tern.server.protocol.completions.TernCompletionProposalRec;

/**
 * Extends {@link JSTernCompletionAsyncCollector} to create JSDT completion
 * proposal to set an high relevance for tern completion proposal to display on
 * the top of the completion popup the tern result.
 * 
 */
public class JSDTTernCompletionCollector extends JSTernCompletionAsyncCollector {

	public static final int TERN_RELEVANT = 10000;

	public JSDTTernCompletionCollector(List<ICompletionProposal> proposals,
			int startOffset, ITernFile ternFile, ITernProject project) {
		super(proposals, startOffset, ternFile, project);
	}

	@Override
	protected JSTernCompletionProposal createProposal(
			TernCompletionProposalRec proposal) {
		return new JSDTTernCompletionProposal(proposal);
	}

	@Override
	protected ICompletionProposal createTimeoutProposal(int startOffset,
			TimeoutReason reason) {
		return new JSDTTimeoutProposal(startOffset, reason);
	}

}
