package com.tulin.v8.core.entity;

import java.util.Map;
import java.util.Properties;

import org.springframework.util.StringUtils;

/**
 * spring数据库配置详情
 * 
 * @author 陈乾
 *
 */
public class SpringDatasource extends AbsDataSource {
	private String type;
	private String driverClassName;
	private int initialSize = 5;
	private int minIdle = 10;
	private int maxActive = 20;
	private int maxWait = 60000;
	private int timeBetweenEvictionRunsMillis = 60000;
	private int minEvictableIdleTimeMillis = 30000;
	private int maxEvictableIdleTimeMillis = 90000;
	private String validationQuery;
	private boolean testWhileIdle;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	private Map<String, Object> webStatFilter;
	private Map<String, Object> statViewServlet;
	private Map<String, Map<String, Object>> filter;

	public SpringDatasource() {
	}

	public SpringDatasource(Properties properties) {
		this.type = properties.getProperty("spring.datasource.type");
		this.driverClassName = properties.getProperty("spring.datasource.driverClassName");
		this.url = properties.getProperty("spring.datasource.url");
		this.username = properties.getProperty("spring.datasource.username");
		this.password = properties.getProperty("spring.datasource.password");
		this.validationQuery = properties.getProperty("spring.datasource.validationQuery");
		if (!StringUtils.isEmpty(this.url) && StringUtils.isEmpty(this.driverClassName)) {
			this.driverClassName = properties.getProperty("spring.datasource.driver-class-name");
		}
		if (StringUtils.isEmpty(this.url)) {
			this.driverClassName = properties.getProperty("spring.datasource.druid.driverClassName");
			this.url = properties.getProperty("spring.datasource.druid.url");
			this.username = properties.getProperty("spring.datasource.druid.username");
			this.password = properties.getProperty("spring.datasource.druid.password");
			if (!StringUtils.isEmpty(this.url) && StringUtils.isEmpty(this.driverClassName)) {
				this.driverClassName = properties.getProperty("spring.datasource.druid.driver-class-name");
			}
		}
		if (StringUtils.isEmpty(this.url)) {
			this.driverClassName = properties.getProperty("spring.datasource.druid.master.driverClassName");
			this.url = properties.getProperty("spring.datasource.druid.master.url");
			this.username = properties.getProperty("spring.datasource.druid.master.username");
			this.password = properties.getProperty("spring.datasource.druid.master.password");
			if (!StringUtils.isEmpty(this.url) && StringUtils.isEmpty(this.driverClassName)) {
				this.driverClassName = properties.getProperty("spring.datasource.druid.master.driver-class-name");
			}
		}
		if (StringUtils.isEmpty(this.url) || StringUtils.isEmpty(this.driverClassName)) {
			this.driverClassName = properties.getProperty("spring.datasource.datasource.master.driverClassName");
			this.url = properties.getProperty("spring.datasource.datasource.master.url");
			this.username = properties.getProperty("spring.datasource.datasource.master.username");
			this.password = properties.getProperty("spring.datasource.datasource.master.password");
			if (!StringUtils.isEmpty(this.url) && StringUtils.isEmpty(this.driverClassName)) {
				this.driverClassName = properties.getProperty("spring.datasource.datasource.master.driver-class-name");
			}
		}
		if (StringUtils.isEmpty(this.url) || StringUtils.isEmpty(this.driverClassName)) {
			this.driverClassName = properties
					.getProperty("spring.datasource.dynamic.datasource.master.driverClassName");
			this.url = properties.getProperty("spring.datasource.dynamic.datasource.master.url");
			this.username = properties.getProperty("spring.datasource.dynamic.datasource.master.username");
			this.password = properties.getProperty("spring.datasource.dynamic.datasource.master.password");
			if (!StringUtils.isEmpty(this.url) && StringUtils.isEmpty(this.driverClassName)) {
				this.driverClassName = properties
						.getProperty("spring.datasource.dynamic.datasource.master.driver-class-name");
			}
		}
		if (StringUtils.isEmpty(this.driverClassName)) {
			this.driverClassName = findDriverClassName(properties);
		}
		if (StringUtils.isEmpty(this.url)) {
			this.url = findUrl(properties);
		}
		if (StringUtils.isEmpty(this.username)) {
			this.username = findUserName(properties);
		}
		if (StringUtils.isEmpty(this.password)) {
			this.password = findPassWord(properties);
		}
	}

	private String findDriverClassName(Properties properties) {
		String driverClassName = findProperties(properties, "driverClassName");
		if (driverClassName == null) {
			driverClassName = findProperties(properties, "driver-class-name");
		}
		return driverClassName;
	}

	private String findUrl(Properties properties) {
		return findProperties(properties, "url", "datasource");
	}

	private String findUserName(Properties properties) {
		return findProperties(properties, "username", "datasource");
	}

	private String findPassWord(Properties properties) {
		return findProperties(properties, "password", "datasource");
	}

	private String findProperties(Properties properties, String name) {
		for (String key : properties.stringPropertyNames()) {
			if (key.contains(name)) {
				return properties.getProperty(key);
			}
		}
		return null;
	}

	private String findProperties(Properties properties, String name, String pname) {
		for (String key : properties.stringPropertyNames()) {
			if (key.contains(name) && key.contains(pname)) {
				return properties.getProperty(key);
			}
		}
		return "";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public int getMaxEvictableIdleTimeMillis() {
		return maxEvictableIdleTimeMillis;
	}

	public void setMaxEvictableIdleTimeMillis(int maxEvictableIdleTimeMillis) {
		this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public Map<String, Object> getWebStatFilter() {
		return webStatFilter;
	}

	public void setWebStatFilter(Map<String, Object> webStatFilter) {
		this.webStatFilter = webStatFilter;
	}

	public Map<String, Object> getStatViewServlet() {
		return statViewServlet;
	}

	public void setStatViewServlet(Map<String, Object> statViewServlet) {
		this.statViewServlet = statViewServlet;
	}

	public Map<String, Map<String, Object>> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Map<String, Object>> filter) {
		this.filter = filter;
	}

	@Override
	public String getDriver() {
		return driverClassName;
	}

}
