package tern.angular.protocol;

import com.eclipsesource.json.JsonArray;

import tern.angular.AngularType;
import tern.server.protocol.JsonHelper;
import tern.server.protocol.TernQuery;

public class TernAngularQuery extends TernQuery {

	private static final long serialVersionUID = 1106369261139966556L;
	private final JsonArray angularTypes;

	public TernAngularQuery(String subtype, AngularType angularType) {
		super("angular");
		super.add("subtype", subtype);
		angularTypes = new JsonArray();
		super.add("angularTypes", angularTypes);
		addType(angularType);
	}

	public void addType(AngularType angularType) {
		angularTypes.add(angularType.name());
	}

	public void setExpression(String expression) {
		super.add("expression", expression);
	}

	public TernAngularScope getScope() {
		TernAngularScope scope = (TernAngularScope) super.get("scope");
		if (scope == null) {
			scope = new TernAngularScope();
			super.add("scope", scope);
		}
		return scope;
	}

	public boolean hasScope() {
		TernAngularScope scope = (TernAngularScope) super.get("scope");
		if (scope == null) {
			return false;
		}
		return true;
	}

	public boolean hasControllers() {
		if (!hasScope()) {
			return false;
		}
		TernAngularScope scope = (TernAngularScope) super.get("scope");
		return scope.hasControllers();
	}

	public boolean hasModule() {
		if (!hasScope()) {
			return false;
		}
		TernAngularScope scope = (TernAngularScope) super.get("scope");
		return scope.hasModule();
	}

	public AngularType getFirstAngularType() {
		return AngularType.get(((JsonArray) super.get("angularTypes")).get(0)
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
