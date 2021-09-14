package com.tulin.v8.core.utils;

import java.util.UUID;

public class IDUtils {
	public static String getGUID() {
		return UUID.randomUUID().toString().toUpperCase().replace("-", "");
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
}
