package com.tulin.v8.core.entity;

import java.util.Map;

/**
 * 动态数据源
 * 
 * @author 陈乾
 */
public class DynamicDatasource extends AbsDataSource {
	private String driverClassName;

	public DynamicDatasource(String name, Map<String, Object> dynamicMap) {
		this.name = name;
		this.url = (String) dynamicMap.get("url");
		this.username = (String) dynamicMap.get("username");
		this.password = (String) dynamicMap.get("password");
		if (dynamicMap.containsKey("driverClassName")) {
			driverClassName = (String) dynamicMap.get("driverClassName");
		} else {
			driverClassName = (String) dynamicMap.get("driver-class-name");
		}
	}

	@Override
	public String getDriver() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

}
