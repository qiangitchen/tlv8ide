package tern.angular;

import tern.utils.StringUtils;

/**
 * Angular type.
 * 
 */
public enum AngularType {

	module, controller, decorator, directive, directives, factory, filter, model, provider, repeat_expression, service, unknown;

	/**
	 * Returns the angular type from the given value otherwise returns
	 * {@link AngularType#unknown}
	 * 
	 * @param value
	 * @return
	 */
	public static AngularType get(String value) {
		if (StringUtils.isEmpty(value)) {
			return unknown;
		}
		AngularType type = unknown;
		for (int i = 0; i < values().length; i++) {
			type = values()[i];
			if (type.name().equalsIgnoreCase(value)) {
				return type;
			}
		}
		return type;
	}

}
