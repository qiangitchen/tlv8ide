package tern.eclipse.jface.fieldassist;

import org.eclipse.jface.fieldassist.IContentProposal;

import tern.server.protocol.completions.TernCompletionItem;
import tern.server.protocol.completions.TernCompletionProposalRec;

public class TernContentProposal extends TernCompletionItem implements
		IContentProposal {

	private final String content;
	private final String description;

	public TernContentProposal(TernCompletionProposalRec proposal) {
		super(proposal);
		int pos = proposal.end - proposal.start;
		this.content = getSignature().substring(pos, getSignature().length());
		this.description = getDoc();

	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getCursorPosition() {
		return 0;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getLabel() {
		return getText();
	}

}
