package com.tulin.v8.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.tulin.v8.core.config.AppConfig;
import com.tulin.v8.core.entity.AbsDataSource;
import com.tulin.v8.core.entity.DynamicDatasource;
import com.tulin.v8.core.entity.SpringDatasource;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DBUtils {

	/**
	 * 获取数据源配置信息
	 * 
	 * @return {@link java.util.Map}
	 */
	public static Map<String, Map<String, String>> getDBConfig() {
		return Configuration.getConfig();
	}

	/**
	 * 判断数据库类型PostgreSQL数据库
	 * 
	 * @param key
	 * @return boolean
	 */
	public static boolean IsPostgreSQL(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String url = (String) m.get("url");
			return url.toUpperCase().indexOf((":postgresql:").toUpperCase()) > 0 ? true : false;
		} else {
			Map<String, DynamicDatasource> datasources = AppConfig.getDynamicDatasources();
			if (datasources != null && !datasources.isEmpty() && datasources.containsKey(key)) {
				return IsPostgreSQL(datasources.get(key));
			}
		}
		return IsPostgreSQL(AppConfig.getSpringDatasource());
	}

	/**
	 * 判断数据库类型PostgreSQL数据库
	 * 
	 * @param springData
	 * @return
	 */
	public static boolean IsPostgreSQL(AbsDataSource springData) {
		String url = springData.getUrl();
		return url.toUpperCase().indexOf((":postgresql:").toUpperCase()) > 0 ? true : false;
	}

	/**
	 * 判断数据库类型达梦数据库
	 * 
	 * @param key
	 * @return
	 */
	public static boolean IsDMDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String url = (String) m.get("url");
			return url.toUpperCase().indexOf((":dm:").toUpperCase()) > 0 ? true : false;
		} else {
			Map<String, DynamicDatasource> datasources = AppConfig.getDynamicDatasources();
			if (datasources != null && !datasources.isEmpty() && datasources.containsKey(key)) {
				return IsDMDB(datasources.get(key));
			}
		}
		return IsDMDB(AppConfig.getSpringDatasource());
	}

	// 判断数据库类型达梦数据库
	public static boolean IsDMDB(AbsDataSource springData) {
		String url = springData.getUrl();
		return url.toUpperCase().indexOf((":dm:").toUpperCase()) > 0 ? true : false;
	}

	// 判断数据库类型金仓数据库
	public static boolean IsKingDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String url = (String) m.get("url");
			return url.toUpperCase().indexOf(("kingbase").toUpperCase()) > 0 ? true : false;
		} else {
			Map<String, DynamicDatasource> datasources = AppConfig.getDynamicDatasources();
			if (datasources != null && !datasources.isEmpty() && datasources.containsKey(key)) {
				return IsKingDB(datasources.get(key));
			}
		}
		return IsKingDB(AppConfig.getSpringDatasource());
	}

	/**
	 * 判断数据库类型金仓数据库
	 * 
	 * @param springData
	 * @return
	 */
	public static boolean IsKingDB(AbsDataSource springData) {
		String url = springData.getUrl();
		return url.toUpperCase().indexOf(("kingbase").toUpperCase()) > 0 ? true : false;
	}

	/**
	 * 判断数据库类型Oracle
	 * 
	 * @param key
	 * @return
	 */
	public static boolean IsOracleDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String url = (String) m.get("url");
			return url.toUpperCase().indexOf(("oracle").toUpperCase()) > 0 ? true : false;
		} else {
			Map<String, DynamicDatasource> datasources = AppConfig.getDynamicDatasources();
			if (datasources != null && !datasources.isEmpty() && datasources.containsKey(key)) {
				return IsOracleDB(datasources.get(key));
			}
		}
		return IsOracleDB(AppConfig.getSpringDatasource());
	}

	/**
	 * 判断数据库类型Oracle
	 * 
	 * @param springData
	 * @return
	 */
	public static boolean IsOracleDB(AbsDataSource springData) {
		String url = springData.getUrl();
		return url.toUpperCase().indexOf(("oracle").toUpperCase()) > 0 ? true : false;
	}

	/**
	 * 判断数据库类型MySQL
	 * 
	 * @param key
	 * @return
	 */
	public static boolean IsMySQLDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String url = (String) m.get("url");
			return url.toUpperCase().indexOf(("mysql").toUpperCase()) > 0 ? true : false;
		} else {
			Map<String, DynamicDatasource> datasources = AppConfig.getDynamicDatasources();
			if (datasources != null && !datasources.isEmpty() && datasources.containsKey(key)) {
				return IsMySQLDB(datasources.get(key));
			}
		}
		return IsMySQLDB(AppConfig.getSpringDatasource());
	}

	/**
	 * 判断数据库类型MySQL
	 * 
	 * @param springData
	 * @return
	 */
	public static boolean IsMySQLDB(AbsDataSource springData) {
		String url = springData.getUrl();
		return url.toUpperCase().indexOf(("mysql").toUpperCase()) > 0 ? true : false;
	}

	/**
	 * 判断数据库类型SQLServer
	 * 
	 * @param key
	 * @return
	 */
	public static boolean IsMSSQLDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String url = (String) m.get("url");
			return url.toUpperCase().indexOf(("jtds").toUpperCase()) > 0 ? true : false;
		} else {
			Map<String, DynamicDatasource> datasources = AppConfig.getDynamicDatasources();
			if (datasources != null && !datasources.isEmpty() && datasources.containsKey(key)) {
				return IsMSSQLDB(datasources.get(key));
			}
		}
		return IsMSSQLDB(AppConfig.getSpringDatasource());
	}

	/**
	 * 判断数据库类型SQLServer
	 * 
	 * @param key
	 * @return
	 */
	public static boolean IsMSSQLDB(AbsDataSource springData) {
		String url = springData.getUrl();
		return url.toUpperCase().indexOf(("jtds").toUpperCase()) > 0 ? true : false;
	}

	/**
	 * 获取连接
	 * 
	 * @param key
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static Connection getAppConn(String key) throws SQLException, NamingException {
		Connection cn = null;
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String driverStr = m.get("driver");
			String url = m.get("url");
			String userName = m.get("username");
			String password = m.get("password");
			try {
				Class.forName(driverStr);
			} catch (ClassNotFoundException eN) {
				eN.printStackTrace();
				throw new SQLException(eN.toString());
			}
			try {
				url = url.replace("&amp;", "&");
			} catch (Exception e) {
			}
			try {
				cn = DriverManager.getConnection(url, userName, password);
			} catch (Exception eJ) {
				throw new SQLException(eJ.toString());
			}
		} else {
			Map<String, DynamicDatasource> datasources = AppConfig.getDynamicDatasources();
			if (datasources != null && !datasources.isEmpty() && datasources.containsKey(key)) {
				return getAppConn(datasources.get(key));
			}
		}
		SpringDatasource datasource = AppConfig.getSpringDatasource();
		cn = getAppConn(datasource);
		return cn;
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 * @throws NamingException
	 */
//	public static Connection getAppConn() throws SQLException, NamingException {
//		SpringDatasource datasource = AppConfig.getSpringDatasource();
//		return getAppConn(datasource);
//	}

	/**
	 * 获取连接
	 * 
	 * @param springData
	 * @return {@link java.sql.Connection}
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static Connection getAppConn(AbsDataSource springData) throws SQLException, NamingException {
		Connection cn = null;
		if (cn == null) {
			String driverStr = springData.getDriver();
			String url = springData.getUrl();
			String userName = springData.getUsername();
			String password = springData.getPassword();
			try {
				Class.forName(driverStr);
			} catch (ClassNotFoundException eN) {
				eN.printStackTrace();
				throw new SQLException(eN.toString());
			}
			try {
				cn = DriverManager.getConnection(url, userName, password);
			} catch (Exception eJ) {
				throw new SQLException(eJ.toString());
			}
		}
		return cn;
	}

	/**
	 * 获取数据库名
	 * 
	 * @param key
	 * @return String
	 */
	public static String getDataName(String key) {
		String result = "";
		try {
			Map<String, Map<String, String>> rm = Configuration.getConfig();
			Map<String, String> m = rm.get(key);
			String url = m.get("url");
			if (IsOracleDB(key)) {
				result = url.substring(url.lastIndexOf(":") + 1);
			} else if (IsMySQLDB(key)) {
				if (url.indexOf("?") > 0) {
					result = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
				} else {
					result = url.substring(url.lastIndexOf("/") + 1);
				}
			} else {
				result = url.substring(url.lastIndexOf("/") + 1);
			}
		} catch (Exception e) {
			SpringDatasource datasource = AppConfig.getSpringDatasource();
			result = getDataName(datasource);
		}
		return result;
	}

	/**
	 * 获取数据库名
	 * 
	 * @param key
	 * @return String
	 */
	public static String getDataUrl(String key) {
		String result = "";
		try {
			Map<String, Map<String, String>> rm = Configuration.getConfig();
			Map<String, String> m = rm.get(key);
			result = m.get("url");
		} catch (Exception e) {
			SpringDatasource datasource = AppConfig.getSpringDatasource();
			result = datasource.getUrl();
		}
		return result;
	}

	/**
	 * 获取数据库名
	 * 
	 * @param springData
	 * @return
	 */
	public static String getDataName(AbsDataSource springData) {
		String result = "";
		String url = springData.getUrl();
		if (IsOracleDB(springData)) {
			result = url.substring(url.lastIndexOf(":") + 1);
		} else if (IsMySQLDB(springData)) {
			if (url.indexOf("?") > 0) {
				result = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
			} else {
				result = url.substring(url.lastIndexOf("/") + 1);
			}
		} else {
			result = url.substring(url.lastIndexOf("/") + 1);
		}
		return result;
	}

	/**
	 * 获取数据库用户名
	 * 
	 * @param key
	 * @return String
	 */
	public static String getUserName(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String user = m.get("username");
			return user;
		} else {
			SpringDatasource datasource = AppConfig.getSpringDatasource();
			return datasource.getUsername();
		}
	}

	public static String getPassWord(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String user = m.get("password");
			return user;
		} else {
			SpringDatasource datasource = AppConfig.getSpringDatasource();
			return datasource.getPassword();
		}
	}

	public static String getDriverClassName(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		if (m != null) {
			String user = m.get("driver");
			return user;
		} else {
			SpringDatasource datasource = AppConfig.getSpringDatasource();
			return datasource.getDriverClassName();
		}
	}

	/**
	 * 查询操作JDBC
	 * 
	 * @param key
	 * @param sql
	 * @return {@link java.util.List}
	 * @throws SQLException
	 */
	public static List<Map<String, String>> execQueryforList(String key, String sql) throws SQLException {
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		List li = new ArrayList();
		Connection aConn = null;
		try {
			aConn = getAppConn(key);
		} catch (NamingException e1) {
			e1 = new NamingException(e1.toString() + ">>key:" + key + "<< SQL:" + sql);
		}
		Statement qry = aConn.createStatement(1004, 1007);
		try {
			rs = qry.executeQuery(sql);
			rsmd = rs.getMetaData();
			int size = rsmd.getColumnCount();
			while (rs.next()) {
				Map sm = new HashMap();
				for (int i = 1; i <= size; i++) {
					String cellType = rsmd.getColumnTypeName(i);
					String cellName = rsmd.getColumnLabel(i).toUpperCase();
					if ("DATE".equals(cellType) || "DATETIME".equals(cellType)) {
						try {
							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String v_l = format.format(format.parse(rs.getString(i)));
								sm.put(cellName, v_l);
							} catch (Exception e) {
								try {
									SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
									SimpleDateFormat nformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									String v_l = nformat.format(format.parse(rs.getString(i)));
									sm.put(cellName, v_l);
								} catch (Exception er) {
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									String v_l = format.format(format.parse(rs.getString(i)));
									sm.put(cellName, v_l);
								}
							}
						} catch (Exception e) {
							sm.put(cellName, "");
						}
					} else if ("BLOB".equals(cellType)) {
						try {
							sm.put(cellName, rs.getBlob(i));
						} catch (Exception e) {
							sm.put(cellName, null);
						}
					} else if ("CLOB".equals(cellType)) {
						Clob clob = rs.getClob(i);
						String content = "";
						if (clob != null) {
							BufferedReader in = new BufferedReader(clob.getCharacterStream());
							StringWriter out = new StringWriter();
							int c;
							try {
								while ((c = in.read()) != -1) {
									out.write(c);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							content = out.toString();
						}
						sm.put(cellName, content);
					} else {
						sm.put(cellName, (rs.getString(i) == null) ? "" : String.valueOf(rs.getString(i)));
					}
				}

				li.add(sm);
			}
		} catch (SQLException e) {
			throw new SQLException(e.toString() + ">>\n sql:" + sql);
		} finally {
			CloseConn(aConn, qry, rs);
		}
		return li;
	}

	/**
	 * 查询操作JDBC
	 * 
	 * @param aConn
	 * @param sql
	 * @return {@link java.util.List}
	 * @throws SQLException
	 */
	public static List<Map<String, String>> execQueryforList(Connection aConn, String sql) throws SQLException {
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		List li = new ArrayList();
		Statement qry = aConn.createStatement(1004, 1007);
		try {
			rs = qry.executeQuery(sql);
			rsmd = rs.getMetaData();
			int size = rsmd.getColumnCount();
			while (rs.next()) {
				Map sm = new HashMap();
				for (int i = 1; i <= size; i++) {
					String cellType = rsmd.getColumnTypeName(i);
					String cellName = rsmd.getColumnLabel(i).toUpperCase();
					if ("DATE".equals(cellType) || "DATETIME".equals(cellType)) {
						try {
							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String v_l = format.format(format.parse(rs.getString(i)));
								sm.put(cellName, v_l);
							} catch (Exception e) {
								try {
									SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
									SimpleDateFormat nformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									String v_l = nformat.format(format.parse(rs.getString(i)));
									sm.put(cellName, v_l);
								} catch (Exception er) {
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									String v_l = format.format(format.parse(rs.getString(i)));
									sm.put(cellName, v_l);
								}
							}
						} catch (Exception e) {
							sm.put(cellName, "");
						}
					} else if ("BLOB".equals(cellType)) {
						try {
							sm.put(cellName, rs.getBlob(i));
						} catch (Exception e) {
							sm.put(cellName, null);
						}
					} else if ("CLOB".equals(cellType)) {
						Clob clob = rs.getClob(i);
						String content = "";
						if (clob != null) {
							BufferedReader in = new BufferedReader(clob.getCharacterStream());
							StringWriter out = new StringWriter();
							int c;
							try {
								while ((c = in.read()) != -1) {
									out.write(c);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							content = out.toString();
						}
						sm.put(cellName, content);
					} else {
						sm.put(cellName, (rs.getString(i) == null) ? "" : String.valueOf(rs.getString(i)));
					}
				}

				li.add(sm);
			}
		} catch (SQLException e) {
			throw new SQLException(e.toString() + ">>\n sql:" + sql);
		} finally {
			CloseConn(null, qry, rs);
		}
		return li;
	}

	/**
	 * 查询操作JDBC
	 * 
	 * @param sql
	 * @return {@link java.util.List}
	 * @throws SQLException
	 */
//	public static List execQueryforList(String sql) throws SQLException {
//		ResultSet rs = null;
//		ResultSetMetaData rsmd = null;
//		List li = new ArrayList();
//		Connection aConn = null;
//		try {
//			aConn = getAppConn();
//		} catch (NamingException e1) {
//			e1 = new NamingException(e1.toString() + ">>SQL:" + sql);
//		}
//		Statement qry = aConn.createStatement(1004, 1007);
//		try {
//			rs = qry.executeQuery(sql);
//			rsmd = rs.getMetaData();
//			int size = rsmd.getColumnCount();
//			while (rs.next()) {
//				Map sm = new HashMap();
//				for (int i = 1; i <= size; i++) {
//					String cellType = rsmd.getColumnTypeName(i);
//					String cellName = rsmd.getColumnLabel(i).toUpperCase();
//					if ("DATE".equals(cellType) || "DATETIME".equals(cellType)) {
//						try {
//							try {
//								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//								String v_l = format.format(format.parse(rs.getString(i)));
//								sm.put(cellName, v_l);
//							} catch (Exception e) {
//								try {
//									SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//									SimpleDateFormat nformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//									String v_l = nformat.format(format.parse(rs.getString(i)));
//									sm.put(cellName, v_l);
//								} catch (Exception er) {
//									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//									String v_l = format.format(format.parse(rs.getString(i)));
//									sm.put(cellName, v_l);
//								}
//							}
//						} catch (Exception e) {
//							sm.put(cellName, "");
//						}
//					} else if ("BLOB".equals(cellType)) {
//						try {
//							sm.put(cellName, rs.getBlob(i));
//						} catch (Exception e) {
//							sm.put(cellName, null);
//						}
//					} else if ("CLOB".equals(cellType)) {
//						Clob clob = rs.getClob(i);
//						String content = "";
//						if (clob != null) {
//							BufferedReader in = new BufferedReader(clob.getCharacterStream());
//							StringWriter out = new StringWriter();
//							int c;
//							try {
//								while ((c = in.read()) != -1) {
//									out.write(c);
//								}
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//							content = out.toString();
//						}
//						sm.put(cellName, content);
//					} else {
//						sm.put(cellName, (rs.getString(i) == null) ? "" : String.valueOf(rs.getString(i)));
//					}
//				}
//
//				li.add(sm);
//			}
//		} catch (SQLException e) {
//			throw new SQLException(e.toString() + ">>\n sql:" + sql);
//		} finally {
//			CloseConn(aConn, qry, rs);
//		}
//		return li;
//	}

	/**
	 * 查詢操作[为了兼容老版本]不推荐使用
	 * 
	 * @param aConn
	 * @param aSQL
	 * @return {@link java.sql.ResultSet}
	 * @throws SQLException
	 */
	@Deprecated
	public static ResultSet execQuery(Connection aConn, String aSQL) throws SQLException {
		ResultSet result = null;
		Statement qry = aConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		try {
			result = qry.executeQuery(aSQL);
			return result;
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
		}
	}

	/**
	 * 执行sql
	 * 
	 * @param dbkey
	 * @param sql
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean execute(String dbkey, String sql) throws Exception {
		boolean result = false;
		Connection conn = null;
		Statement stm = null;
		try {
			conn = getAppConn(dbkey);
			stm = conn.createStatement();
			stm.execute(sql);
			result = true;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			CloseConn(conn, stm, null);
		}
		return result;
	}

	/**
	 * 关闭连接
	 * 
	 * @param conn
	 * @param stm
	 * @param rs
	 */
	public static void closeConn(Connection conn, Statement stm, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
		}
		try {
			if (stm != null)
				stm.close();
		} catch (SQLException e) {
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
		}
	}

	public static void CloseConn(Connection conn, Statement stm, ResultSet rs) {
		closeConn(conn, stm, rs);
	}

}
