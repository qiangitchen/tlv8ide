package tern.tulin.v8.webtools.contentassist;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import tern.eclipse.ide.ui.contentassist.JSTernCompletionProposal;
import tern.server.protocol.completions.TernCompletionProposalRec;

public class GenericeditorTernCompletionProposal extends JSTernCompletionProposal implements IJavaCompletionProposal {

	public GenericeditorTernCompletionProposal(TernCompletionProposalRec proposal) {
		super(proposal);
	}

	@Override
	public int getRelevance() {
		return GenericeditorTernCompletionCollector.TERN_RELEVANT;
	}

}
