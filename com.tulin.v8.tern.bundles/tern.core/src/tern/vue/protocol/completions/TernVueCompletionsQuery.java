package tern.vue.protocol.completions;

import tern.vue.VueType;
import tern.vue.protocol.TernVueQuery;

public class TernVueCompletionsQuery extends TernVueQuery {

	private static final long serialVersionUID = 1L;

	private static final String COMPLETIONS_TYPE_QUERY = "completions";

	public TernVueCompletionsQuery(VueType vueType) {
		super(COMPLETIONS_TYPE_QUERY, vueType);
	}

}
