package tern.vue.protocol;

import com.eclipsesource.json.JsonArray;

import tern.server.protocol.JsonHelper;
import tern.server.protocol.TernQuery;
import tern.vue.VueType;

public class TernVueQuery extends TernQuery {

	private static final long serialVersionUID = -5676256362167161571L;
	private final JsonArray vueTypes;

	public TernVueQuery(String subtype, VueType vueType) {
		super("vue");
		super.add("subtype", subtype);
		vueTypes = new JsonArray();
		super.add("vueTypes", vueTypes);
		addType(vueType);
	}

	public void addType(VueType vueType) {
		vueTypes.add(vueType.name());
	}

	public void setExpression(String expression) {
		super.add("expression", expression);
	}

	public TernVueScope getScope() {
		TernVueScope scope = (TernVueScope) super.get("scope");
		if (scope == null) {
			scope = new TernVueScope();
			super.add("scope", scope);
		}
		return scope;
	}

	public boolean hasScope() {
		TernVueScope scope = (TernVueScope) super.get("scope");
		if (scope == null) {
			return false;
		}
		return true;
	}

	public boolean hasControllers() {
		if (!hasScope()) {
			return false;
		}
		TernVueScope scope = (TernVueScope) super.get("scope");
		return scope.hasControllers();
	}

	public boolean hasModule() {
		if (!hasScope()) {
			return false;
		}
		TernVueScope scope = (TernVueScope) super.get("scope");
		return scope.hasModule();
	}

	public VueType getFirstVueType() {
		return VueType.get(((JsonArray) super.get("vueTypes")).get(0)
				.asString());
	}

	public String getLabel() {
		return new StringBuilder(super.getLabel()).append("_")
				.append(getSubType()).toString();
	}

	private String getSubType() {
		return JsonHelper.getString(this, "subtype");
	}
}
