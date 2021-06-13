package com.tlv8.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import com.tulin.v8.core.DBUtils;

public class Utils {
	/**
	 * 获取平台配置的数据源键集
	 * 
	 * @return
	 */
	public static Set<String> getPermitionDatasourceKeys() {
		return DBUtils.getDBConfig().keySet();
	}

	/**
	 * 获取平台配置的数据源
	 * 
	 * @return
	 */
	public static Map<String, Map<String, String>> getPermitionDatasources() {
		return DBUtils.getDBConfig();
	}
	
	/**
	 * 获取数据连接
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	public static Connection getAppConn(String key){
		try {
			return DBUtils.getAppConn(key);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
