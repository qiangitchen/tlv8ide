package tern.eclipse.ide.internal.ui.descriptors.options;

import tern.server.protocol.JsonHelper;

import com.eclipsesource.json.JsonObject;

/**
 * Lint rule.
 *
 */
public class LintRule {

	private final String label;
	private final JsonObject option;

	public LintRule(JsonObject defaultRule, JsonObject options) {
		String ruleId = defaultRule.names().get(0);
		JsonObject labelAndSeverity = (JsonObject) defaultRule.get(ruleId);
		JsonObject option = getRuleOption(ruleId,
				JsonHelper.getString(labelAndSeverity.get("severity")), options);
		this.label = JsonHelper.getString(labelAndSeverity.get("label"));
		this.option = option;
	}

	/**
	 * Returns the label of the rule.
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	private JsonObject getRuleOption(String ruleId, String defaultSeverity,
			JsonObject options) {
		JsonObject option = (JsonObject) options.get(ruleId);
		if (option == null) {
			option = new JsonObject();
			option.set("severity", defaultSeverity);
			options.set(ruleId, option);
		}
		return option;
	}

	public String getSeverity() {
		return JsonHelper.getString(option.get("severity"));
	}

	public void setSeverity(String severity) {
		option.set("severity", severity);
	}
}
