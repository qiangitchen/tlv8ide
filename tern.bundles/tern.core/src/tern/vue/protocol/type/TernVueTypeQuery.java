package tern.vue.protocol.type;

import tern.vue.VueType;
import tern.vue.protocol.TernVueQuery;

public class TernVueTypeQuery extends TernVueQuery {

	private static final long serialVersionUID = 1L;

	private static final String TYPE_QUERY_TYPE = "type";
	
	public TernVueTypeQuery(VueType vueType) {
		super(TYPE_QUERY_TYPE, vueType);
	}

}
