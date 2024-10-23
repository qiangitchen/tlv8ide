package tern.eclipse.jface.fieldassist;

import org.eclipse.jface.fieldassist.IContentProposal;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.completions.ITernCompletionCollector;
import tern.server.protocol.completions.TernCompletionProposalRec;

public abstract class AbstractTernContentProposalCollector implements
		ITernCompletionCollector {

	@Override
	public void addProposal(TernCompletionProposalRec proposal,
			Object completion, IJSONObjectHelper jsonObjectHelper) {
		addProposal(new TernContentProposal(proposal));
	}

	protected abstract void addProposal(IContentProposal proposal);

}
