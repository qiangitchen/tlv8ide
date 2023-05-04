package com.tulin.v8.core.entity;

public abstract class AbsDataSource {
	protected String url = "";
	protected String username;
	protected String password;
	
	public abstract String getDriver();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isOracleDB() {
		return url.toUpperCase().indexOf(("oracle").toUpperCase()) > 0 ? true : false;
	}

	public boolean isPostgreSQL() {
		return url.toUpperCase().indexOf((":postgresql:").toUpperCase()) > 0 ? true : false;
	}

	public boolean isMySQLDB() {
		return url.toUpperCase().indexOf(("mysql").toUpperCase()) > 0 ? true : false;
	}

	public boolean isMSSQLDB() {
		return url.toUpperCase().indexOf(("jtds").toUpperCase()) > 0 ? true : false;
	}

	public boolean IsKingDB() {
		return url.toUpperCase().indexOf(("kingbase").toUpperCase()) > 0 ? true : false;
	}

	public boolean isDMDB() {
		return url.toUpperCase().indexOf((":dm:").toUpperCase()) > 0 ? true : false;
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
}
