package com.tulin.v8.core.entity;

import java.util.Map;

/**
 * Druid数据源
 * 
 * @author 陈乾
 */
public class DruidDatasource extends DynamicDatasource {

	private boolean enabled = true;

	public DruidDatasource(String name, Map<String, Object> druidMap) {
		super(name, druidMap);
		if (druidMap.containsKey("enabled")) {
			enabled = (boolean) druidMap.get("enabled");
		}
		if (url == null || "".equals(url)) {
			enabled = false;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

}
