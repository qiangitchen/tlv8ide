package com.tulin.v8.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tulin.v8.core.Dao.SqlMap.GetSqlMapByKey;

/**
 * <code>Configuration</code>
 * 
 * @author CG
 * @since 0.1
 */
public final class Configuration {

	public static String getConfigInfo() {
		String xml = "";
		Map<String, Map<String, String>> m = getConfig();
		Set<String> k = m.keySet();
		Iterator<String> it = k.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			xml += "<" + key + ">" + m.get(key) + "</" + key + ">";
		}
		return xml;
	}

	public static Map<String, Map<String, String>> getConfig() {
		Map<String, Map<String, String>> m = new HashMap<String, Map<String, String>>();
		m = GetSqlMapByKey.getKeyDbDriverMap();
		return m;
	}
}
