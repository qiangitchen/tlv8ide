package tern.vue;

import tern.utils.StringUtils;

/**
 * Vue type.
 * 
 */
public enum VueType {

	module, controller, decorator, directive, directives, factory, filter, model, provider, repeat_expression, service, unknown;

	/**
	 * Returns the vue type from the given value otherwise returns
	 * {@link VueType#unknown}
	 * 
	 * @param value
	 * @return
	 */
	public static VueType get(String value) {
		if (StringUtils.isEmpty(value)) {
			return unknown;
		}
		VueType type = unknown;
		for (int i = 0; i < values().length; i++) {
			type = values()[i];
			if (type.name().equalsIgnoreCase(value)) {
				return type;
			}
		}
		return type;
	}

}
