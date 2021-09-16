package com.tulin.v8.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.CallableStatement;
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
import oracle.jdbc.driver.OracleTypes;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DBUtils {

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
		String url = (String) m.get("url");
		return url.toUpperCase().indexOf((":postgresql:").toUpperCase()) > 0 ? true : false;
	}

	// 判断数据库类型达梦数据库
	public static boolean IsDMDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		// Sys.printMsg(rm);
		String url = (String) m.get("url");
		return url.toUpperCase().indexOf((":dm:").toUpperCase()) > 0 ? true : false;
	}

	// 判断数据库类型金仓数据库
	public static boolean IsKingDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		// Sys.printMsg(rm);
		String url = (String) m.get("url");
		return url.toUpperCase().indexOf(("kingbase").toUpperCase()) > 0 ? true : false;
	}

	// 判断数据库类型Oracle
	public static boolean IsOracleDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		// Sys.printMsg(rm);
		String url = (String) m.get("url");
		return url.toUpperCase().indexOf(("oracle").toUpperCase()) > 0 ? true : false;
	}

	// 判断数据库类型MySQL
	public static boolean IsMySQLDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		// Sys.printMsg(rm);
		String url = (String) m.get("url");
		return url.toUpperCase().indexOf(("mysql").toUpperCase()) > 0 ? true : false;
	}

	// 判断数据库类型SQLServer
	public static boolean IsMSSQLDB(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		// Sys.printMsg(rm);
		String url = (String) m.get("url");
		return url.toUpperCase().indexOf(("jtds").toUpperCase()) > 0 ? true : false;
	}

	// 获取连接
	public static Connection getAppConn(String key) throws SQLException, NamingException {
		Connection cn = null;
		if (cn == null) {
			Map<String, Map<String, String>> rm = Configuration.getConfig();
			Map<String, String> m = rm.get(key);
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
			}catch (Exception e) {
			}
			try {
				cn = DriverManager.getConnection(url, userName, password);
			} catch (Exception eJ) {
				throw new SQLException(eJ.toString());
			}
		}
		return cn;
	}

	// 获取数据库名
	public static String getDataName(String key) {
		String result = "";
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		String url = m.get("url");
		if (IsOracleDB(key)) {
			result = url.substring(url.lastIndexOf(":") + 1);
		} else if (IsMySQLDB(key)) {
			result = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
		} else {
			result = url.substring(url.lastIndexOf("/") + 1);
		}
		return result;
	}

	// 获取数据库用户名
	public static String getUserName(String key) {
		Map<String, Map<String, String>> rm = Configuration.getConfig();
		Map<String, String> m = rm.get(key);
		String user = m.get("username");
		return user;
	}

	// 查询操作JDBC
	public static List execQueryforList(String key, String sql) throws SQLException {
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

	// 查詢操作[为了兼容老版本]不推荐使用
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

	// 关闭连接
	public static void CloseConn(Connection conn, Statement stm, ResultSet rs) {
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

	// 调用存储过程; aParams为分号分割的字符串参数值列表, 所有参数只能是字符串类型, 且只能是in类型
	public static String callProc(String aProcName, String aParamValues) {
		String result;
		result = "调用存储过程";
		System.out.println(String.format("com.tulin.v8.core.DBUtils.callProc(aProcName:%s, aParamValues:%s)", aProcName,
				aParamValues));

		String[] values = aParamValues.split(";");
		try {
			Connection conn = DBUtils.getAppConn("system");
			try {
				String s1 = "";
				for (int i = 1; i <= values.length; i++) {
					s1 += ",?";
				}
				if (s1.length() > 0)
					s1 = s1.substring(1);
				String sql = String.format("{ call %s(%s,?) }", aProcName, s1);
				CallableStatement proc = conn.prepareCall(sql);

				try {
					for (int i = 1; i <= values.length; i++) {
						proc.setString(i, values[i - 1]);
						proc.registerOutParameter(3, OracleTypes.VARCHAR);
					}
					proc.execute();
					result += "->" + proc.getString(3);
					System.out.println(result);
				} finally {
					proc.close();
				}
				return result;
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
