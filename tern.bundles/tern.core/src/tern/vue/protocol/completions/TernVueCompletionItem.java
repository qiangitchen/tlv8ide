package tern.vue.protocol.completions;

import tern.server.protocol.completions.TernCompletionItem;

public class TernVueCompletionItem extends TernCompletionItem {

	private final String module;
	private final String controller;

	public TernVueCompletionItem(VueCompletionProposalRec proposal,
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
