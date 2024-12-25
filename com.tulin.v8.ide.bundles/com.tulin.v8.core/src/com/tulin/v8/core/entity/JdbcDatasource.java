package com.tulin.v8.core.entity;

import java.util.Properties;

/**
 * 项目中配置的jdbc数据源
 * 
 * @author 陈乾
 *
 */
public class JdbcDatasource extends AbsDataSource {
	private String driver;
	private int initialSize = 0;
	private int maxActive = 10;
	private int maxIdle = 20;
	private int minIdle = 20;
	private int maxWait = 60000;

	public JdbcDatasource() {
		this.name = "jdbc";
	}

	public JdbcDatasource(Properties properties) {
		this.name = "jdbc";
		this.driver = properties.getProperty("driver");
		this.url = properties.getProperty("url");
		this.setUsername(properties.getProperty("username"));
		this.setPassword(properties.getProperty("password"));
		this.initialSize = Integer.valueOf(properties.getProperty("initialSize", "0").trim());
		this.maxActive = Integer.valueOf(properties.getProperty("maxActive", "20").trim());
		this.maxIdle = Integer.valueOf(properties.getProperty("maxIdle", "20").trim());
		this.minIdle = Integer.valueOf(properties.getProperty("minIdle", "1").trim());
		this.maxWait = Integer.valueOf(properties.getProperty("maxWait", "60000").trim());
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

}
