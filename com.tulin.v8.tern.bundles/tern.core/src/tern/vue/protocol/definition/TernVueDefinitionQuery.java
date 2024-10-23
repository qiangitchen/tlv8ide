package tern.vue.protocol.definition;

import tern.vue.VueType;
import tern.vue.protocol.TernVueQuery;

public class TernVueDefinitionQuery extends TernVueQuery {

	private static final long serialVersionUID = 1L;

	private static final String DEFINITION_QUERY_TYPE = "definition";
	
	public TernVueDefinitionQuery(VueType vueType) {
		super(DEFINITION_QUERY_TYPE, vueType);
	}

}
