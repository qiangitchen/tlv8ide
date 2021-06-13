package com.tulin.v8.ureport.server.cache;

import java.util.HashMap;
import java.util.Map;

public class TempObjectCache {
	private static Map<String, Object> tempObjectmap = new HashMap<String, Object>();

	public static Object getObject(String key) {
		return tempObjectmap.get(key);
	}

	public static void putObject(String key, Object obj) {
		tempObjectmap.put(key, obj);
	}

	public static void removeObject(String key) {
		tempObjectmap.remove(key);
	}
}
