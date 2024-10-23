package com.tulin.v8.webtools.ide.utils;

public class StringUtils {

	public static boolean isEmpty(String value) {
		return value == null || value.length() == 0;
	}

	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
}
