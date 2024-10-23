package tern.angular.protocol.completions;

import tern.server.protocol.completions.TernCompletionProposalRec;

public class AngularCompletionProposalRec extends TernCompletionProposalRec {

	public AngularCompletionProposalRec(TernCompletionProposalRec item,
			int startOffset) {
		super(item.name, item.type, item.doc, item.url, item.origin,
				startOffset, startOffset);
	}

}
