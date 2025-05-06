package tern.vue.protocol.outline;

import tern.server.protocol.TernQuery;

/**
 * Vue Outline Query.
 *
 */
public class VueOutlineQuery extends TernQuery {

	private static final long serialVersionUID = 1L;

	private static final String OUTLINE_TYPE_QUERY = "vue-outline";

	public VueOutlineQuery() {
		super(OUTLINE_TYPE_QUERY);
	}

}
