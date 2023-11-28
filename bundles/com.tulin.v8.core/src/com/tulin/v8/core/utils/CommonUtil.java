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

import org.apache.ibatis.jdbc.SQL;
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
	 * @see getDataObject(String dbkey, String schemaPattern)
	 */
	@Deprecated
	public static Map<String, List<String>> getDataObject(String dbkey) throws Exception {
		Map map = new HashMap<String, List<String>>();
		map = getDataObject(dbkey, null);
		if (map.isEmpty()) {
			return map;
		}
		map.put("TABLE", getDataTables(dbkey));
		map.put("VIEW", getDataViews(dbkey));
		return map;
	}

	public static List<String> getDataTables(String dbkey) throws Exception {
		String dataName = DBUtils.getDataName(dbkey);
		String userName = DBUtils.getUserName(dbkey);
		SQL tSql = new SQL();
		tSql.SELECT("TABLE_NAME");
		tSql.FROM("user_tables t");
		if (DBUtils.IsMSSQLDB(dbkey)) {
			tSql = new SQL().SELECT("name as TABLE_NAME");
			tSql.FROM("dbo.sysobjects");
			tSql.WHERE("xtype = 'U'");
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			tSql = new SQL().SELECT("TABLE_NAME");
			tSql.FROM("information_schema.TABLES");
			tSql.WHERE("TABLE_SCHEMA = '" + dataName + "'");
		} else if (DBUtils.IsPostgreSQL(dbkey)) {
			tSql = new SQL().SELECT("a.relname AS TABLE_NAME");
			tSql.FROM("pg_class a");
			tSql.WHERE("a.relkind='r'");
			SQL tSqls = new SQL().SELECT("oid");
			tSqls.FROM("pg_namespace");
			tSqls.WHERE("nspname='public' or nspname='" + userName + "'");
			tSql.WHERE("a.relnamespace in (" + tSqls.toString() + ")");
		}
		List<String> lit = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			pstmt1 = conn.prepareStatement(tSql.toString());
			rs = pstmt1.executeQuery();
			while (rs.next()) {
				lit.add(rs.getString(1));
			}
		} finally {
			DBUtils.CloseConn(conn, pstmt1, rs);
		}
		return lit;
	}

	public static List<String> getDataViews(String dbkey) throws Exception {
		String dataName = DBUtils.getDataName(dbkey);
		String userName = DBUtils.getUserName(dbkey);
		SQL vSql = new SQL().SELECT("VIEW_NAME").FROM("user_views t");
		if (DBUtils.IsMSSQLDB(dbkey)) {
			vSql = new SQL().SELECT("name as VIEW_NAME");
			vSql.FROM("dbo.sysobjects");
			vSql.WHERE("xtype = 'V'");
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			vSql = new SQL().SELECT("TABLE_NAME as VIEW_NAME");
			vSql.FROM("information_schema.VIEWS");
			vSql.WHERE("TABLE_SCHEMA = '" + dataName + "'");
		} else if (DBUtils.IsPostgreSQL(dbkey)) {
			vSql = new SQL().SELECT("a.relname AS VIEW_NAME");
			vSql.FROM("pg_class a").WHERE("a.relkind='v'");
			SQL vSqls = new SQL().SELECT("oid");
			vSqls.FROM("pg_namespace");
			vSqls.WHERE("nspname='public' or nspname='" + userName + "'");
			vSql.WHERE("a.relnamespace in (" + vSqls.toString() + ")");
		}
		List<String> liv = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			pstmt2 = conn.prepareStatement(vSql.toString());
			rs2 = pstmt2.executeQuery();
			while (rs2.next()) {
				liv.add(rs2.getString(1));
			}
		} finally {
			DBUtils.CloseConn(conn, pstmt2, rs2);
		}
		return liv;
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
				rs = objMet.getTables(catalog, schemaPattern, "%", new String[] { "TABLE" });
			} else {
				rs = objMet.getTables(catalog, "%", "%", new String[] { "TABLE" });
			}
			List<String> li = new ArrayList<String>();
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				li.add(tableName);
			}
			if (li.size() < 1) {
				li = getDataTables(dbkey);
			}
			map.put("TABLE", li);
			if (schemaPattern != null) {
				rs = objMet.getTables(catalog, schemaPattern, "%", new String[] { "VIEW" });
			} else {
				rs = objMet.getTables(catalog, "%", "%", new String[] { "VIEW" });
			}
			List<String> liv = new ArrayList<String>();
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				liv.add(tableName);
			}
			if (liv.size() < 1) {
				liv = getDataViews(dbkey);
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
	 * @see getTableComments(String dbkey, String schemaPattern, String tablename,
	 *      String[] types)
	 */
	@Deprecated
	public static String getTableComments(String dbkey, String tablename) {
		String coms = getTableComments(dbkey, null, tablename, new String[] { "TABLE", "VIEW" });
		if (coms != null) {
			return coms;
		}
		return getTableCommentsBySql(dbkey, tablename);
	}

	static String getTableCommentsBySql(String dbkey, String tablename) {
		String result = "";
		SQL sql = new SQL();
		sql.SELECT("t.table_name TABLE_NAME,t.comments TABLE_COMMENT");
		sql.FROM("user_tab_comments t");
		sql.WHERE("t.table_name = '" + tablename + "'");
		if (DBUtils.IsMSSQLDB(dbkey)) {
			sql = new SQL().SELECT("a.name TABLE_NAME, isnull(g.value,'') TABLE_COMMENT");
			sql.FROM("sys.tables a");
			sql.LEFT_OUTER_JOIN("sys.extended_properties g on (a.object_id = g.major_id AND g.minor_id = 0)");
			sql.WHERE("a.name  = '" + tablename + "'");
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			sql = new SQL().SELECT("TABLE_NAME,TABLE_COMMENT");
			sql.FROM("INFORMATION_SCHEMA.TABLES");
			sql.WHERE("TABLE_NAME='" + tablename + "'");
		} else if (DBUtils.IsPostgreSQL(dbkey)) {
			sql = new SQL();
			sql.SELECT("a.relname AS TABLE_NAME,b.description AS TABLE_COMMENT");
			sql.FROM("pg_class a");
			sql.LEFT_OUTER_JOIN("pg_description b ON b.objsubid=0 AND a.oid = b.objoid");
			sql.WHERE("a.relkind='r'");
			sql.WHERE("a.relname='" + tablename.toLowerCase() + "'");
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				String val = rs.getString("TABLE_COMMENT");
				if (val != null && !"null".equals(val)) {
					result = val;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeConn(conn, ps, rs);
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
		String cm = "";
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
			if (StringUtils.isEmpty(cm)) {
				cm = getTableCommentsBySql(dbkey, tablename);
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
	 * @see getTableColumn(String dbkey, String schemaPattern, String tableName)
	 */
	@Deprecated
	public static List<String[]> getTableColumn(String dbkey, String tableName) throws Exception {
		List<String[]> rlist = getTableColumn(dbkey, null, tableName);
		if (rlist.size() > 0) {
			return rlist;
		}
		rlist = new ArrayList<String[]>();
		SQL sql = new SQL();
		sql.SELECT("t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE");
		sql.SELECT("t2.COMMENTS,t1.CHAR_LENGTH,t1.DATA_DEFAULT AS COLUMN_DEF");
		sql.FROM("user_tab_columns t1");
		sql.LEFT_OUTER_JOIN(
				"user_col_comments t2 on t1.TABLE_NAME = t2.table_name and t1.COLUMN_NAME = t2.column_name");
		if (tableName == null || "".equals(tableName)) {
			if (DBUtils.IsMSSQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("b.name as TABLE_NAME,a.name as COLUMN_NAME,c.name as DATA_TYPE");
				sql.SELECT("d.value as COMMENTS,a.prec as CHAR_LENGTH,a.text as COLUMN_DEF");
				sql.FROM("dbo.syscolumns as a");
				sql.INNER_JOIN("dbo.sysobjects as b on b.id = a.id");
				sql.INNER_JOIN("dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype");
				sql.LEFT_OUTER_JOIN("sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid");
			} else if (DBUtils.IsMySQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("TABLE_NAME,COLUMN_NAME,DATA_TYPE,column_comment as COMMENTS");
				sql.SELECT("character_maximum_length as CHAR_LENGTH,COLUMN_TYPE,COLUMN_DEFAULT AS COLUMN_DEF");
				sql.FROM("information_schema.columns");
			} else if (DBUtils.IsPostgreSQL(dbkey)) {
				sql = new SQL();
				sql.SELECT("a.attnum,a.attname AS COLUMN_NAME,t.typname AS DATA_TYPE");
				sql.SELECT("a.attlen AS length,a.atttypmod AS CHAR_LENGTH");
				sql.SELECT("a.attnotnull AS notnull,b.description AS COMMENTS,d.adsrc as COLUMN_DEF");
				sql.FROM(" pg_class c").LEFT_OUTER_JOIN("pg_attribute a on a.attrelid = c.oid");
				sql.LEFT_OUTER_JOIN("pg_description b ON a.attrelid=b.objoid AND a.attnum = b.objsubid");
				sql.LEFT_OUTER_JOIN("pg_type t on a.atttypid = t.oid");
				sql.LEFT_OUTER_JOIN("join pg_attrdef d on d.adrelid=a.attrelid and d.adnum=a.attnum");
				sql.WHERE("a.attnum > 0");
			}
		} else {
			sql = new SQL();
			sql.SELECT(
					"t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE,t2.COMMENTS,t1.CHAR_LENGTH,t1.DATA_DEFAULT AS COLUMN_DEF");
			sql.FROM("user_tab_columns t1");
			sql.LEFT_OUTER_JOIN(
					"user_col_comments t2 on t1.TABLE_NAME = t2.table_name and t1.COLUMN_NAME = t2.column_name");
			sql.WHERE("t2.table_name = '" + tableName + "'");
			if (DBUtils.IsMSSQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("b.name as TABLE_NAME,a.name as COLUMN_NAME,c.name as DATA_TYPE");
				sql.SELECT("d.value as COMMENTS,a.prec as CHAR_LENGTH,a.text as COLUMN_DEF");
				sql.FROM("dbo.syscolumns as a");
				sql.INNER_JOIN("dbo.sysobjects as b on b.id = a.id");
				sql.INNER_JOIN("join dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype");
				sql.LEFT_OUTER_JOIN("sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid");
				sql.WHERE("b.name = '" + tableName + "'");
			} else if (DBUtils.IsMySQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("TABLE_NAME,COLUMN_NAME,DATA_TYPE,column_comment as COMMENTS");
				sql.SELECT("character_maximum_length as CHAR_LENGTH,COLUMN_TYPE,COLUMN_DEFAULT AS COLUMN_DEF");
				sql.FROM("information_schema.columns");
				sql.WHERE("table_name='" + tableName + "'");
			} else if (DBUtils.IsPostgreSQL(dbkey)) {
				sql = new SQL();
				sql.SELECT("A.attnum,A.attname AS COLUMN_NAME,T.typname AS DATA_TYPE,A.attlen AS LENGTH");
				sql.SELECT("A.atttypmod AS CHAR_LENGTH,A.attnotnull AS NOTNULL");
				sql.SELECT("b.description AS COMMENTS,d.adsrc AS COLUMN_DEF");
				sql.FROM("pg_class C").LEFT_OUTER_JOIN("pg_attribute A ON A.attrelid = C.oid");
				sql.LEFT_OUTER_JOIN("pg_description b ON A.attrelid = b.objoid AND A.attnum = b.objsubid");
				sql.LEFT_OUTER_JOIN("pg_type T ON A.atttypid = T.oid");
				sql.LEFT_OUTER_JOIN("pg_attrdef d ON d.adrelid = A.attrelid AND d.adnum = A.attnum");
				sql.WHERE("C.relname = '" + tableName.toLowerCase() + "' and a.attnum > 0");
			}
		}
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			stm = conn.createStatement();
			rs = stm.executeQuery(sql.toString());
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
	 * 获取字段注释
	 * 
	 * @param dbkey
	 * @param conn
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	static String getTableColumnRemarks(String dbkey, Connection conn, String tableName, String columnName) {
		String res = "";
		SQL sql = new SQL();
		sql.SELECT("t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE");
		sql.SELECT("t2.COMMENTS,t1.CHAR_LENGTH,t1.DATA_DEFAULT AS COLUMN_DEF");
		sql.FROM("user_tab_columns t1");
		sql.LEFT_OUTER_JOIN(
				"user_col_comments t2 on t1.TABLE_NAME = t2.table_name and t1.COLUMN_NAME = t2.column_name");
		sql.WHERE("t1.COLUMN_NAME='" + columnName + "'");
		if (tableName == null || "".equals(tableName)) {
			if (DBUtils.IsMSSQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("b.name as TABLE_NAME,a.name as COLUMN_NAME,c.name as DATA_TYPE");
				sql.SELECT("d.value as COMMENTS,a.prec as CHAR_LENGTH,a.text as COLUMN_DEF");
				sql.FROM("dbo.syscolumns as a");
				sql.INNER_JOIN("dbo.sysobjects as b on b.id = a.id");
				sql.INNER_JOIN("dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype");
				sql.LEFT_OUTER_JOIN("sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid");
				sql.WHERE("a.name='" + columnName + "'");
			} else if (DBUtils.IsMySQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("TABLE_NAME,COLUMN_NAME,DATA_TYPE,column_comment as COMMENTS");
				sql.SELECT("character_maximum_length as CHAR_LENGTH,COLUMN_TYPE,COLUMN_DEFAULT AS COLUMN_DEF");
				sql.FROM("information_schema.columns");
				sql.WHERE("column_name='" + columnName + "'");
			} else if (DBUtils.IsPostgreSQL(dbkey)) {
				sql = new SQL();
				sql.SELECT("a.attnum,a.attname AS COLUMN_NAME,t.typname AS DATA_TYPE");
				sql.SELECT("a.attlen AS length,a.atttypmod AS CHAR_LENGTH");
				sql.SELECT("a.attnotnull AS notnull,b.description AS COMMENTS,d.adsrc as COLUMN_DEF");
				sql.FROM(" pg_class c").LEFT_OUTER_JOIN("pg_attribute a on a.attrelid = c.oid");
				sql.LEFT_OUTER_JOIN("pg_description b ON a.attrelid=b.objoid AND a.attnum = b.objsubid");
				sql.LEFT_OUTER_JOIN("pg_type t on a.atttypid = t.oid");
				sql.LEFT_OUTER_JOIN("join pg_attrdef d on d.adrelid=a.attrelid and d.adnum=a.attnum");
				sql.WHERE("a.attnum > 0");
				sql.WHERE("a.attname='" + columnName.toLowerCase() + "'");
			}
		} else {
			sql = new SQL();
			sql.SELECT(
					"t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE,t2.COMMENTS,t1.CHAR_LENGTH,t1.DATA_DEFAULT AS COLUMN_DEF");
			sql.FROM("user_tab_columns t1");
			sql.LEFT_OUTER_JOIN(
					"user_col_comments t2 on t1.TABLE_NAME = t2.table_name and t1.COLUMN_NAME = t2.column_name");
			sql.WHERE("t2.table_name = '" + tableName + "'");
			sql.WHERE("t1.COLUMN_NAME='" + columnName + "'");
			if (DBUtils.IsMSSQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("b.name as TABLE_NAME,a.name as COLUMN_NAME,c.name as DATA_TYPE");
				sql.SELECT("d.value as COMMENTS,a.prec as CHAR_LENGTH,a.text as COLUMN_DEF");
				sql.FROM("dbo.syscolumns as a");
				sql.INNER_JOIN("dbo.sysobjects as b on b.id = a.id");
				sql.INNER_JOIN("join dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype");
				sql.LEFT_OUTER_JOIN("sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid");
				sql.WHERE("b.name = '" + tableName + "'");
				sql.WHERE("a.name='" + columnName + "'");
			} else if (DBUtils.IsMySQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("TABLE_NAME,COLUMN_NAME,DATA_TYPE,column_comment as COMMENTS");
				sql.SELECT("character_maximum_length as CHAR_LENGTH,COLUMN_TYPE,COLUMN_DEFAULT AS COLUMN_DEF");
				sql.FROM("information_schema.columns");
				sql.WHERE("table_name='" + tableName + "'");
				sql.WHERE("column_name='" + columnName + "'");
			} else if (DBUtils.IsPostgreSQL(dbkey)) {
				sql = new SQL();
				sql.SELECT("A.attnum,A.attname AS COLUMN_NAME,T.typname AS DATA_TYPE,A.attlen AS LENGTH");
				sql.SELECT("A.atttypmod AS CHAR_LENGTH,A.attnotnull AS NOTNULL");
				sql.SELECT("b.description AS COMMENTS,d.adsrc AS COLUMN_DEF");
				sql.FROM("pg_class C").LEFT_OUTER_JOIN("pg_attribute A ON A.attrelid = C.oid");
				sql.LEFT_OUTER_JOIN("pg_description b ON A.attrelid = b.objoid AND A.attnum = b.objsubid");
				sql.LEFT_OUTER_JOIN("pg_type T ON A.atttypid = T.oid");
				sql.LEFT_OUTER_JOIN("pg_attrdef d ON d.adrelid = A.attrelid AND d.adnum = A.attnum");
				sql.WHERE("C.relname = '" + tableName.toLowerCase() + "' and a.attnum > 0");
				sql.WHERE("a.attname='" + columnName.toLowerCase() + "'");
			}
		}
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery(sql.toString());
			if (rs.next()) {
				res = rs.getString("COMMENTS");
			}
		} catch (Exception e) {
			System.err.println(sql);
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(null, stm, rs);
		}
		return res;
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
				rs = objMet.getColumns(catalog, schemaPattern, tableName, null);
			} else {
				rs = objMet.getColumns(catalog, null, tableName, null);
			}
			while (rs.next()) {
				String[] item = new String[6];
				item[0] = rs.getString("COLUMN_NAME");
				item[1] = rs.getString("TYPE_NAME");
				item[2] = rs.getString("REMARKS");
				if (StringUtils.isEmpty(item[2])) {
					item[2] = getTableColumnRemarks(dbkey, conn, tableName, item[0]);
				}
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
					item[4] = getColumnType(dbkey, conn, schemaPattern, tableName, item[0]);
				} else {
					item[4] = "";
				}
				try {
					item[5] = rs.getString("COLUMN_DEF");
				} catch (Exception e) {
					item[5] = getColumnDefault(dbkey, conn, tableName, item[0]);
				}
				rlist.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(conn, null, rs);
		}
		return rlist;
	}

	/**
	 * 获取字段默认值
	 * 
	 * @param dbkey
	 * @param conn
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	static String getColumnDefault(String dbkey, Connection conn, String tableName, String columnName) {
		String res = "";
		Statement stm = null;
		ResultSet rs = null;
		try {
			SQL sql = new SQL();
			sql.SELECT(
					"t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE,t2.COMMENTS,t1.CHAR_LENGTH,t1.DATA_DEFAULT AS COLUMN_DEF");
			sql.FROM("user_tab_columns t1");
			sql.LEFT_OUTER_JOIN(
					"user_col_comments t2 on t1.TABLE_NAME = t2.table_name and t1.COLUMN_NAME = t2.column_name");
			sql.WHERE("t2.table_name = '" + tableName + "'");
			sql.WHERE("t1.COLUMN_NAME='" + columnName + "'");
			if (DBUtils.IsMSSQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("b.name as TABLE_NAME,a.name as COLUMN_NAME,c.name as DATA_TYPE");
				sql.SELECT("d.value as COMMENTS,a.prec as CHAR_LENGTH,a.text as COLUMN_DEF");
				sql.FROM("dbo.syscolumns as a");
				sql.INNER_JOIN("dbo.sysobjects as b on b.id = a.id");
				sql.INNER_JOIN("join dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype");
				sql.LEFT_OUTER_JOIN("sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid");
				sql.WHERE("b.name = '" + tableName + "'");
				sql.WHERE("a.name='" + columnName + "'");
			} else if (DBUtils.IsMySQLDB(dbkey)) {
				sql = new SQL();
				sql.SELECT("TABLE_NAME,COLUMN_NAME,DATA_TYPE,column_comment as COMMENTS");
				sql.SELECT("character_maximum_length as CHAR_LENGTH,COLUMN_TYPE,COLUMN_DEFAULT AS COLUMN_DEF");
				sql.FROM("information_schema.columns");
				sql.WHERE("table_name='" + tableName + "'");
				sql.WHERE("column_name='" + columnName + "'");
			} else if (DBUtils.IsPostgreSQL(dbkey)) {
				sql = new SQL();
				sql.SELECT("A.attnum,A.attname AS COLUMN_NAME,T.typname AS DATA_TYPE,A.attlen AS LENGTH");
				sql.SELECT("A.atttypmod AS CHAR_LENGTH,A.attnotnull AS NOTNULL");
				sql.SELECT("b.description AS COMMENTS,d.adsrc AS COLUMN_DEF");
				sql.FROM("pg_class C").LEFT_OUTER_JOIN("pg_attribute A ON A.attrelid = C.oid");
				sql.LEFT_OUTER_JOIN("pg_description b ON A.attrelid = b.objoid AND A.attnum = b.objsubid");
				sql.LEFT_OUTER_JOIN("pg_type T ON A.atttypid = T.oid");
				sql.LEFT_OUTER_JOIN("pg_attrdef d ON d.adrelid = A.attrelid AND d.adnum = A.attnum");
				sql.WHERE("C.relname = '" + tableName.toLowerCase() + "' and a.attnum > 0");
				sql.WHERE("a.attname='" + columnName.toLowerCase() + "'");
			}
			stm = conn.createStatement();
			rs = stm.executeQuery(sql.toString());
			if (rs.next()) {
				res = rs.getString("COLUMN_DEF");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeConn(null, stm, rs);
		}
		return res;
	}

	/**
	 * 获取字段类型
	 * 
	 * @param dbkey
	 * @param conn
	 * @param schemaPattern
	 * @param tableName
	 * @param columnName
	 * @return String
	 */
	static String getColumnType(String dbkey, Connection conn, String schemaPattern, String tableName,
			String columnName) {
		String result = "";
		SQL sql = new SQL().SELECT("COLUMN_TYPE");
		sql.FROM("information_schema.columns");
		sql.WHERE("table_name=?");
		sql.WHERE("COLUMN_NAME=?");
		if (schemaPattern != null) {
			sql.WHERE("TABLE_SCHEMA=?");
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, tableName);
			ps.setString(2, columnName);
			if (schemaPattern != null) {
				ps.setString(3, schemaPattern);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.CloseConn(null, ps, rs);
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
		SQL sql = new SQL().SELECT("t1.nullable as IS_NULLABLE");
		sql.FROM("user_tab_columns t1");
		sql.LEFT_OUTER_JOIN(
				"user_col_comments t2 on t1.TABLE_NAME = t2.table_name and t1.COLUMN_NAME = t2.column_name");
		sql.WHERE("t2.table_name = ? and t1.COLUMN_NAME = ?");
		if (DBUtils.IsMSSQLDB(dbkey)) {
			sql = new SQL().SELECT("a.isnullable as IS_NULLABLE");
			sql.FROM("dbo.syscolumns as a");
			sql.INNER_JOIN("dbo.sysobjects as b on b.id = a.id");
			sql.INNER_JOIN("dbo.systypes as c on a.xtype = c.xtype and c.xusertype = c.xtype");
			sql.LEFT_OUTER_JOIN("sys.extended_properties d on d.major_id = a.id and d.minor_id = a.colid");
			sql.WHERE("b.name = ? and a.name = ?");
		} else if (DBUtils.IsMySQLDB(dbkey) || DBUtils.IsPostgreSQL(dbkey)) {
			sql = new SQL().SELECT("IS_NULLABLE");
			sql.FROM("information_schema.columns");
			sql.WHERE("table_name=? and COLUMN_NAME=?");
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getAppConn(dbkey);
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, tableName);
			ps.setString(2, columnName);
			rs = ps.executeQuery();
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
			DBUtils.closeConn(conn, ps, rs);
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
				rs = objMet.getColumns(catalog, schemaPattern, tableName, columnName);
			} else {
				rs = objMet.getColumns(catalog, "%", tableName, columnName);
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
