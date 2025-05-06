package tern.angular.protocol.completions;

import tern.server.protocol.completions.TernCompletionItem;

public class TernAngularCompletionItem extends TernCompletionItem {

	private final String module;
	private final String controller;

	public TernAngularCompletionItem(AngularCompletionProposalRec proposal,
			String module, String controller) {
		super(proposal);
		this.module = module;
		this.controller = controller;
	}

	public String getModule() {
		return module;
	}

	public String getController() {
		return controller;
	}

}
