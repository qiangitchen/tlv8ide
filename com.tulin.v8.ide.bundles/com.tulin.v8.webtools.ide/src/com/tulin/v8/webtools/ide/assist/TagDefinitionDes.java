package com.tulin.v8.webtools.ide.assist;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class TagDefinitionDes {
	private static final String BUNDLE_NAME = "com.tulin.v8.webtools.ide.assist.tagdefinition";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private TagDefinitionDes() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	public static Enumeration<String> getKeys() {
		return RESOURCE_BUNDLE.getKeys();
	}
}
