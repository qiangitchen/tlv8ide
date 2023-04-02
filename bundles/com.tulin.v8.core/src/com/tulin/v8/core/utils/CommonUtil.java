package com.tulin.v8.core.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;

import com.tulin.v8.core.DBUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonUtil {
	public static final int COLUMN_NAME = 4;
	public static final int DATA_TYPE = 5;
	public static final int TYPE_NAME = 6;
	public static final int COLUMN_SIZE = 7;
	public static final int DECIMAL_DIGITS = 9;
	public static final int NULLABLE = 11;
	public static final int REMARKS = 12;
	public static final int IS_NULLABLE = 18;

	/**
	 * 判断内容是否为空
	 * 
	 * @param paramObject
	 * @return boolean
	 */
	public static boolean isEmpty(Object paramObject) {
		return (paramObject == null) || (paramObject.toString().trim().equals(""));
	}

	/**
	 * 重构文件路径
	 * 
	 * @param paramString
	 * @return String
	 */
	public static String rebuildFilePath(String paramString) {
		return paramString.replace("\\", "/");
	}

	/**
	 * 获取操作系统名称
	 * 
	 * @return String
	 */
	public static String getOSName() {
		String osname = System.getProperty("os.name").toLowerCase();
		// System.out.println("OS Name:" + osname);
		return osname;
	}

	/**
	 * 获取操作系统版本
	 * 
	 * @return double
	 */
	public static double getOSVersion() {
		// window 版本号，例如win2000是5.0，xp是5.1，vista是6.0，win7是6.1 win10是10.0
		String osversion = System.getProperty("os.version");
		// System.out.println("OS Name:" + getOSName());
		// System.out.println("OS Version:" + osversion);
		return Double.parseDouble(osversion);
	}

	/**
	 * 是否为Windows系统
	 * 
	 * @return boolean
	 */
	public static boolean isWinOS() {
		return getOSName().contains("win");
	}

	/**
	 * 是否为Mac OS系统
	 * 
	 * @return boolean
	 */
	public static boolean isMacOS() {
		return getOSName().contains("mac");
	}

	/**
	 * 是否为Linux系统
	 * 
	 * @return boolean
	 */
	public static boolean isLinuxOS() {
		return getOSName().contains("linux");
	}

	/**
	 * 是否为32位架构
	 * 
	 * @return boolean
	 */
	public static boolean is32() {
		return "x86".equals(Platform.getOSArch());
	}

	/**
	 * 是否为64位架构
	 * 
	 * @return boolean
	 */
	public static boolean is64() {
		return Platform.getOSArch().indexOf("64") > -1;
	}

	/**
	 * 是否为32位架构Windows系统
	 * 
	 * @return boolean
	 */
	public static boolean isWin32() {
		return isWinOS() && is32();
	}

	/**
	 * 是否为64位架构Windows系统
	 * 
	 * @return boolean
	 */
	public static boolean isWin64() {
		return isWinOS() && is64();
	}

	/**
	 * 获取文件路径分隔符
	 * 
	 * @return String
	 */
	public static String getPathdep() {
		if (isWinOS()) {
			return "\\";
		} else {
			return "/";
		}
	}

	/**
	 * 获取数据库对象【表和视图】
	 * 
	 * @param dbkey
	 * @return {@link java.util.Map}
	 * @throws Exception
	 */
	public static Map<String, List<String>> getDataObject(String dbkey) throws Exception {
		Map map = new HashMap<String, List<String>>();
		map = getDataObject(dbkey, null);
		if (map.isEmpty()) {
			return map;
		}
		String dataName = DBUtils.getDataName(dbkey);
		String userName = DBUtils.getUserName(dbkey);
		String tsql = "select TABLE_NAME from user_tables t";
		if (DBUtils.IsMSSQLDB(dbkey)) {
			tsql = "select name as TABLE_NAME from dbo.sysobjects where xtype = 'U'";
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			tsql = "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA='" + dataName + "'";
		} else if (DBUtils.IsPostgreSQL(dbkey)) {
			tsql = "select a.relname AS TABLE_NAME FROM pg_class a where a.relkind='r' "
					+ " and a.relnamespace in (SELECT oid FROM pg_namespace WHERE nspname='public' or nspname='"
					+ userName + "')";
		}
		if (DBUtils.IsMySQLDB(dbkey)) {
			Connection conn = DBUtils.getAppConn(dbkey);
			PreparedStatement pstmt1 = conn.prepareStatement(tsql);
			ResultSet rs = pstmt1.executeQuery();
			List<String> li = new ArrayList<String>();
			while (rs.next()) {
				li.add(rs.getString(1));
			}
			map.put("TABLE", li);
			DBUtils.CloseConn(conn, null, rs);
		} else {
			List<Map<String, String>> list = DBUtils.execQueryforList(dbkey, tsql);
			List<String> li = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				Map m = list.get(i);
				li.add((String) m.get("TABLE_NAME"));
			}
			map.put("TABLE", li);
		}
		String vsql = "select VIEW_NAME from user_views t";
		List<String> liv = new ArrayList<String>();
		if (DBUtils.IsMSSQLDB(dbkey)) {
			vsql = "select name as VIEW_NAME from dbo.sysobjects where xtype = 'V'";
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			vsql = "select TABLE_NAME as VIEW_NAME from information_schema.VIEWS where TABLE_SCHEMA='" + dataName + "'";
		} else if (DBUtils.IsPostgreSQL(dbkey)) {
			vsql = "select a.relname AS VIEW_NAME FROM pg_class a where a.relkind='v' "
					+ " and a.relnamespace in (SELECT oid FROM pg_namespace WHERE nspname='public' or nspname='"
					+ userName + "')";
		}
		List<Map<String, String>> listv = DBUtils.execQueryforList(dbkey, vsql);
		for (int i = 0; i < listv.size(); i++) {
			Map m = listv.get(i);
			liv.add((String) m.get("VIEW_NAME"));
		}
		map.put("VIEW", liv);
		return map;
	}

	/**
	 * 获取数据库对象【表和视图】【JDBC】
	 * 
	 * @param dbkey
	 * @return {@link java.util.Map}
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static Map<String, List<String>> getDataObject(String dbkey, String schemaPattern) throws Exception {
		Map map = new HashMap<String, List<String>>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			DatabaseMetaData objMet = conn.getMetaData();
			String catalog = null;
			if (DBUtils.IsPostgreSQL(dbkey)) {
				catalog = conn.getCatalog();
				schemaPattern = "public";
			}
			if (DBUtils.IsOracleDB(dbkey)) {
				schemaPattern = objMet.getUserName();
			}
			if (DBUtils.IsMySQLDB(dbkey)) {
				schemaPattern = conn.getCatalog();
			}
			if (schemaPattern != null) {
				rs = objMet.getTables(catalog, schemaPattern, "%", new String[] { "TABLE" }); //$NON-NLS-1$
			} else {
				rs = objMet.getTables(catalog, "%", "%", new String[] { "TABLE" }); //$NON-NLS-1$ //$NON-NLS-2$
			}
			List<String> li = new ArrayList<String>();
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				li.add(tableName);
			}
			map.put("TABLE", li);
			if (schemaPattern != null) {
				rs = objMet.getTables(catalog, schemaPattern, "%", new String[] { "VIEW" }); //$NON-NLS-1$
			} else {
				rs = objMet.getTables(catalog, "%", "%", new String[] { "VIEW" }); //$NON-NLS-1$ //$NON-NLS-2$
			}
			List<String> liv = new ArrayList<String>();
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				liv.add(tableName);
			}
			map.put("VIEW", liv);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, stm, rs);
		}
		return map;
	}

	/**
	 * 获取表的注释信息
	 * 
	 * @param dbkey
	 * @param tablename
	 * @return String
	 */
	public static String getTableComments(String dbkey, String tablename) {
		String coms = getTableComments(dbkey, null, tablename, new String[] { "TABLE", "VIEW" });
		if (coms != null) {
			return coms;
		}
		String result = "";
		String sql = "select t.table_name TABLE_NAME,t.comments TABLE_COMMENT "
				+ " from user_tab_comments t where t.table_name = '" + tablename + "'";
		if (DBUtils.IsMSSQLDB(dbkey)) {
			sql = "select a.name TABLE_NAME, isnull(g.value,'') TABLE_COMMENT from"
					+ " sys.tables a left join sys.extended_properties g "
					+ " on (a.object_id = g.major_id AND g.minor_id = 0) where a.name  = '" + tablename + "'";
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			sql = "select TABLE_NAME,TABLE_COMMENT from INFORMATION_SCHEMA.TABLES where TABLE_NAME = '" + tablename
					+ "'";
		} else if (DBUtils.IsPostgreSQL(dbkey)) {
			sql = "select a.relname AS TABLE_NAME,b.description AS TABLE_COMMENT "
					+ " FROM pg_class a LEFT OUTER JOIN pg_description b "
					+ " ON b.objsubid=0 AND a.oid = b.objoid WHERE a.relname='" + tablename.toLowerCase()
					+ "' AND a.relkind='r'";
		}
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				String val = rs.getString("TABLE_COMMENT");
				if (val != null && !"null".equals(val)) {
					result = val;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBUtils.CloseConn(conn, stm, rs);
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * 获取表的注释信息【JDBC】
	 * 
	 * @param dbkey
	 * @param tablename
	 * @return String
	 */
	public static String getTableComments(String dbkey, String schemaPattern, String tablename, String[] types) {
		String cm = null;
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			DatabaseMetaData objMet = conn.getMetaData();
			String catalog = null;
			if (DBUtils.IsPostgreSQL(dbkey)) {
				catalog = conn.getCatalog();
				schemaPattern = "public";
			}
			if (DBUtils.IsOracleDB(dbkey)) {
				schemaPattern = objMet.getUserName();
			}
			if (DBUtils.IsMySQLDB(dbkey)) {
				schemaPattern = conn.getCatalog();
			}
			if (schemaPattern != null) {
				rs = objMet.getTables(catalog, schemaPattern, tablename, types); // $NON-NLS-1$
			} else {
				rs = objMet.getTables(catalog, "%", tablename, types); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (rs.next()) {
				cm = rs.getString("REMARKS");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, stm, rs);
		}
		return cm;
	}

	/**
	 * 获取表的字段信息
	 * 
	 * @param dbkey
	 * @param tableName
	 * @return {@link java.util.List}
	 * @throws Exception
	 */
	public static List<String[]> getTableColumn(String dbkey, String tableName) throws Exception {
		List<String[]> rlist = getTableColumn(dbkey, null, tableName);
		if (rlist.size() > 0) {
			return rlist;
		}
		rlist = new ArrayList<String[]>();
		String sql = "select t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE,t2.COMMENTS,t1.CHAR_LENGTH,t1.DATA_DEFAULT AS COLUMN_DEF "
				+ "from user_tab_columns t1  left join " + "user_col_comments t2 on t1.TABLE_NAME = t2.table_name "
				+ "and t1.COLUMN_NAME = t2.column_name";
		if (tableName == null || "".equals(tableName)) {
			if (DBUtils.IsMSSQLDB(dbkey)) {
				sql = "select b.name as TABLE_NAME,a.name as COLUMN_NAME,c.name as DATA_TYPE,"
						+ "d.value as COMMENTS,a.prec as CHAR_LENGTH,a.text as COLUMN_DEF "
						+ "from dbo.syscolumns as a " + "inner join dbo.sysobjects as b on b.id = a.id "
						+ "inner join dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype "
						+ "left join sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid";
			} else if (DBUtils.IsMySQLDB(dbkey)) {
				sql = "select TABLE_NAME,COLUMN_NAME,DATA_TYPE,column_comment as COMMENTS,character_maximum_length as CHAR_LENGTH,COLUMN_TYPE,COLUMN_DEFAULT AS COLUMN_DEF "
						+ " from information_schema.columns";
			} else if (DBUtils.IsPostgreSQL(dbkey)) {
				sql = "select a.attnum,a.attname AS COLUMN_NAME,t.typname AS DATA_TYPE,"
						+ "a.attlen AS length,a.atttypmod AS CHAR_LENGTH,"
						+ "a.attnotnull AS notnull,b.description AS COMMENTS,d.adsrc as COLUMN_DEF  "
						+ "FROM pg_class c left join pg_attribute a on a.attrelid = c.oid "
						+ " LEFT OUTER JOIN pg_description b ON a.attrelid=b.objoid AND a.attnum = b.objsubid "
						+ " left join pg_type t on a.atttypid = t.oid "
						+ " left join pg_attrdef d on d.adrelid=a.attrelid and d.adnum=a.attnum WHERE a.attnum > 0";
			}
		} else {
			sql = "select t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE,t2.COMMENTS,t1.CHAR_LENGTH,t1.DATA_DEFAULT AS COLUMN_DEF "
					+ "from user_tab_columns t1  left join " + "user_col_comments t2 on t1.TABLE_NAME = t2.table_name "
					+ "and t1.COLUMN_NAME = t2.column_name " + "where t2.table_name = '" + tableName + "'";
			if (DBUtils.IsMSSQLDB(dbkey)) {
				sql = "select b.name as TABLE_NAME,a.name as COLUMN_NAME,c.name as DATA_TYPE,"
						+ "d.value as COMMENTS,a.prec as CHAR_LENGTH,a.text as COLUMN_DEF "
						+ "from dbo.syscolumns as a " + "inner join dbo.sysobjects as b on b.id = a.id "
						+ "inner join dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype "
						+ "left join sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid "
						+ "where b.name = '" + tableName + "'";
			} else if (DBUtils.IsMySQLDB(dbkey)) {
				sql = "select TABLE_NAME,COLUMN_NAME,DATA_TYPE,column_comment as COMMENTS,character_maximum_length as CHAR_LENGTH,COLUMN_TYPE,COLUMN_DEFAULT AS COLUMN_DEF "
						+ " from information_schema.columns where table_name='" + tableName + "'";
			} else if (DBUtils.IsPostgreSQL(dbkey)) {
				sql = "SELECT A.attnum,A.attname AS COLUMN_NAME,T.typname AS DATA_TYPE,A.attlen AS LENGTH,"
						+ "	A.atttypmod AS CHAR_LENGTH,A.attnotnull AS NOTNULL,"
						+ "	b.description AS COMMENTS,d.adsrc AS COLUMN_DEF FROM "
						+ "	pg_class C LEFT JOIN pg_attribute A ON A.attrelid = C.oid"
						+ "	LEFT OUTER JOIN pg_description b ON A.attrelid = b.objoid "
						+ "	AND A.attnum = b.objsubid LEFT JOIN pg_type T ON A.atttypid = T.oid"
						+ "	LEFT JOIN pg_attrdef d ON d.adrelid = A.attrelid AND d.adnum = A.attnum "
						+ " WHERE C.relname = '" + tableName.toLowerCase() + "' and a.attnum > 0";
			}
		}
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				String[] item = new String[6];
				item[0] = rs.getString("COLUMN_NAME");
				item[1] = rs.getString("DATA_TYPE");
				item[2] = rs.getString("COMMENTS");
				item[3] = rs.getString("CHAR_LENGTH");
				if (item[3] == null || Long.parseLong(item[3]) < 1) {
					item[3] = "";
				}
				if (DBUtils.IsMySQLDB(dbkey)) {
					item[4] = rs.getString("COLUMN_TYPE");
				} else {
					item[4] = "";
				}
				item[5] = rs.getString("COLUMN_DEF");
				rlist.add(item);
			}
		} catch (Exception e) {
			System.err.println(sql);
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, stm, rs);
		}
		return rlist;
	}

	/**
	 * 获取表的字段信息【JDBC】
	 * 
	 * @param dbkey
	 * @param tableName
	 * @return {@link java.util.List}
	 * @throws Exception
	 */
	public static List<String[]> getTableColumn(String dbkey, String schemaPattern, String tableName) throws Exception {
		List<String[]> rlist = new ArrayList<String[]>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			DatabaseMetaData objMet = conn.getMetaData();
			String catalog = null;
			if (DBUtils.IsPostgreSQL(dbkey)) {
				catalog = conn.getCatalog();
				schemaPattern = "public";
			}
			if (DBUtils.IsMySQLDB(dbkey)) {
				schemaPattern = conn.getCatalog();
			}
			if (DBUtils.IsOracleDB(dbkey)) {
				schemaPattern = objMet.getUserName();
			}
			if (schemaPattern != null) {
				rs = objMet.getColumns(catalog, schemaPattern, tableName, null); // $NON-NLS-1$
			} else {
				rs = objMet.getColumns(catalog, "%", tableName, null); //$NON-NLS-1$ //$NON-NLS-2$
			}
			while (rs.next()) {
				String[] item = new String[6];
				item[0] = rs.getString("COLUMN_NAME");
				item[1] = rs.getString("TYPE_NAME");
				item[2] = rs.getString("REMARKS");
				item[3] = rs.getString("COLUMN_SIZE");
				if (item[3] == null || Integer.parseInt(item[3]) < 1) {
					item[3] = "";
				}
				if ("text".equalsIgnoreCase(item[1]) || "clob".equalsIgnoreCase(item[1])
						|| "blob".equalsIgnoreCase(item[1]) || item[1].toLowerCase().contains("int")
						|| item[1].toLowerCase().contains("float") || item[1].toLowerCase().contains("number")
						|| item[1].toLowerCase().contains("text") || item[1].toLowerCase().contains("date")
						|| item[1].toLowerCase().contains("time")) {
					item[3] = "";
				}
				if (DBUtils.IsMySQLDB(dbkey)) {
					item[4] = getColumnType(dbkey, schemaPattern, tableName, item[0]);
				} else {
					item[4] = "";
				}
				item[5] = rs.getString("COLUMN_DEF");
				rlist.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, stm, rs);
		}
		return rlist;
	}

	/**
	 * 获取字段类型
	 * 
	 * @param dbkey
	 * @param schemaPattern
	 * @param tableName
	 * @param columnName
	 * @return String
	 */
	public static String getColumnType(String dbkey, String schemaPattern, String tableName, String columnName) {
		String result = "";
		String sql = "select COLUMN_TYPE from information_schema.columns where table_name='" + tableName
				+ "' and COLUMN_NAME ='" + columnName + "'";
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				result = rs.getString("COLUMN_TYPE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, stm, rs);
		}
		return result;
	}

	/**
	 * 获取字段是否允许为空
	 * 
	 * @param dbkey
	 * @param tableName
	 * @param columnName
	 * @return boolean
	 */
	public static boolean ColumnsnNllAbled(String dbkey, String tableName, String columnName) {
		int n = ColumnsnNllAbled(dbkey, null, tableName, columnName);
		if (n != -1) {
			return n == DatabaseMetaData.columnNullable;
		}
		String sql = "select t1.nullable as IS_NULLABLE " + "from user_tab_columns t1  left join "
				+ "user_col_comments t2 on t1.TABLE_NAME = t2.table_name " + "and t1.COLUMN_NAME = t2.column_name "
				+ "where t2.table_name = '" + tableName + "' and t1.COLUMN_NAME = '" + columnName + "'";
		if (DBUtils.IsMSSQLDB(dbkey)) {
			sql = "select a.isnullable as IS_NULLABLE from dbo.syscolumns as a "
					+ "inner join dbo.sysobjects as b on b.id = a.id "
					+ "inner join dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype "
					+ "left join sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid "
					+ "where b.name = '" + tableName + "' and a.name = '" + columnName + "'";
		} else if (DBUtils.IsMySQLDB(dbkey) || DBUtils.IsPostgreSQL(dbkey)) {
			sql = "select IS_NULLABLE " + "from information_schema.columns where table_name='" + tableName
					+ "' and COLUMN_NAME='" + columnName + "'";
		}
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				String isnullstr = rs.getString("IS_NULLABLE");
				if (isnullstr != null) {
					isnullstr = isnullstr.toUpperCase();
					return isnullstr.equals("1") || isnullstr.equals("Y") || isnullstr.equals("YES");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				DBUtils.CloseConn(conn, stm, rs);
			} catch (Exception e) {
			}
		}
		return true;
	}

	/**
	 * 获取字段是否允许为空【JDBC】
	 * 
	 * @param dbkey
	 * @param tableName
	 * @param columnName
	 * @return int
	 */
	public static int ColumnsnNllAbled(String dbkey, String schemaPattern, String tableName, String columnName) {
		int r = -1;
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			String catalog = null;
			if (DBUtils.IsPostgreSQL(dbkey)) {
				catalog = conn.getCatalog();
				schemaPattern = "public";
			}
			if (DBUtils.IsOracleDB(dbkey)) {
				schemaPattern = conn.getMetaData().getUserName();
			}
			if (DBUtils.IsMySQLDB(dbkey)) {
				schemaPattern = conn.getCatalog();
			}
			DatabaseMetaData objMet = conn.getMetaData();
			if (schemaPattern != null) {
				rs = objMet.getColumns(catalog, schemaPattern, tableName, columnName); // $NON-NLS-1$
			} else {
				rs = objMet.getColumns(catalog, "%", tableName, columnName); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (rs.next()) {
				r = rs.getInt(NULLABLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, stm, rs);
		}
		return r;
	}

	public static boolean needQuotation(String columnType) {
		String type = columnType.toLowerCase();
		if (type.contains("date") || type.contains("time") || type.contains("int") || type.contains("float")
				|| type.contains("double") || type.contains("decimal")) {
			return false;
		}
		return true;
	}
}
