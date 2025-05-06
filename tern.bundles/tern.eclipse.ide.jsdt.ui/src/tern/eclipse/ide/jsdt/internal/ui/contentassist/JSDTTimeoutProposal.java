package tern.eclipse.ide.jsdt.internal.ui.contentassist;

import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposal;

import tern.eclipse.ide.ui.contentassist.TimeoutProposal;
import tern.server.protocol.ITernResultsAsyncCollector.TimeoutReason;

/**
 * 
 Extends {@link TimeoutProposal} to set an high relevance for tern completion
 * proposal to display on the top of the completion popup the tern result.
 */
public class JSDTTimeoutProposal extends TimeoutProposal implements
		IJavaCompletionProposal {

	public JSDTTimeoutProposal(int startOffset, TimeoutReason reason) {
		super(startOffset, reason);
	}

	@Override
	public int getRelevance() {
		return JSDTTernCompletionCollector.TERN_RELEVANT;
	}

}
