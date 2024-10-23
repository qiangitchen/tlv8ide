package tern.eclipse.ide.genericeditor.contentassist;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;

import tern.eclipse.ide.ui.contentassist.TimeoutProposal;
import tern.server.protocol.ITernResultsAsyncCollector.TimeoutReason;

public class GenericeditorTimeoutProposal extends TimeoutProposal implements IJavaCompletionProposal {

	public GenericeditorTimeoutProposal(int startOffset, TimeoutReason reason) {
		super(startOffset, reason);
	}

	@Override
	public int getRelevance() {
		return GenericeditorTernCompletionCollector.TERN_RELEVANT;
	}

}
