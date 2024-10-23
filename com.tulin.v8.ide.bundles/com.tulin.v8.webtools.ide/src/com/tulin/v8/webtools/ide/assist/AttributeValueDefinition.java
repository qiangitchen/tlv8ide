package com.tulin.v8.webtools.ide.assist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributeValueDefinition {

	/** attribute value proposals of align attribute */
	private static final String[] align = { "left", "center", "right" };

	/** attribute value proposals of valign attribute */
	private static final String[] valign = { "top", "middle", "bottom" };

	/** attribute value proposals of type attribute of input element */
	private static final String[] inputType = { "text", "password", "hidden", "checkbox", "radio", "button", "reset",
			"submit", "file" };

	/** attribute value proposals of target attribute */
	private static final String[] target = { "_blank", "_self", "_parent", "_top" };

	private static Map<Integer, String[]> map = new HashMap<Integer, String[]>();

	static {
		addAttributeValues(AttributeInfo.ALIGN, align);
		addAttributeValues(AttributeInfo.VALIGN, valign);
		addAttributeValues(AttributeInfo.INPUT_TYPE, inputType);
		addAttributeValues(AttributeInfo.TARGET, target);
	}

	private static void addAttributeValues(int type, String[] values) {
		Arrays.sort(values);
		map.put(type, values);
	}

	public static String[] getAttributeValues(int type) {
		if (map.get(type) == null) {
			return new String[0];
		}
		return map.get(type);
	}

}
