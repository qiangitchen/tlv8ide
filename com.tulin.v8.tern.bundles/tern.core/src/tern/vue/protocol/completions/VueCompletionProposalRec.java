package tern.vue.protocol.completions;

import tern.server.protocol.completions.TernCompletionProposalRec;

public class VueCompletionProposalRec extends TernCompletionProposalRec {

	public VueCompletionProposalRec(TernCompletionProposalRec item,
			int startOffset) {
		super(item.name, item.type, item.doc, item.url, item.origin,
				startOffset, startOffset);
	}

}
