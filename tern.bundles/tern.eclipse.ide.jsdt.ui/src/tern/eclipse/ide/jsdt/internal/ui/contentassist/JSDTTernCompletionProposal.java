package tern.eclipse.ide.jsdt.internal.ui.contentassist;

import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposal;

import tern.eclipse.ide.ui.contentassist.JSTernCompletionProposal;
import tern.server.protocol.completions.TernCompletionProposalRec;

/**
 * Extends {@link JSTernCompletionProposal} to implements JSDT
 * {@link IJavaCompletionProposal} to set an high relevance for tern completion
 * proposal to display on the top of the completion popup the tern result.
 *
 */
public class JSDTTernCompletionProposal extends JSTernCompletionProposal
		implements IJavaCompletionProposal {

	public JSDTTernCompletionProposal(TernCompletionProposalRec proposal) {
		super(proposal);
	}

	@Override
	public int getRelevance() {
		// TODO : compute relevance switch type?
		return JSDTTernCompletionCollector.TERN_RELEVANT;
	}

}
