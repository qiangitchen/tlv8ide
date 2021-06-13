package com.tulin.v8.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONObject;

public class DataSourceUtils {
	public static Connection getAppConn(String key) throws SQLException,
			NamingException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Context context = new InitialContext();
		DataSource ds = (DataSource) context.lookup("java:comp/env/" + key);
		Connection cn = ds.getConnection();
		if (cn != null)
			System.out.println("Connection ok");
		return cn;
	}

	public static JSONObject testLink(String driverStr, String url,
			String userName, String password) {
		String result = Messages.getString("DataSourceUtils.result.1");
		Connection cn = null;
		JSONObject json = new JSONObject();
		try {
			try {
				Class.forName(driverStr);
			} catch (ClassNotFoundException eN) {
				result = Messages.getString("DataSourceUtils.result.2")
						+ driverStr + "! e:" + eN.toString();
				json.put("flag", false);
				json.put("message", result);
				return json;
			}
			try {
				cn = DriverManager.getConnection(url, userName, password);
				json.put("flag", true);
				json.put("message", Messages.getString("DataSourceUtils.result.1"));
			} catch (SQLException eJ) {
				result = String.format(
						Messages.getString("DataSourceUtils.result.3"), url,
						userName, password) + "! e:" + eJ.toString();
				json.put("flag", false);
				json.put("message", result);
			} finally {
				try {
					DBUtils.CloseConn(cn, null, null);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		return json;
	}
}
