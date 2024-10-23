package com.tulin.v8.core.utils;

public class StringUtils {

	public static boolean isEmpty(Object o) {
		return o == null || "".equals(o);
	}

	public static boolean isNotEmpty(Object o) {
		return o != null && !"".equals(o);
	}

}
