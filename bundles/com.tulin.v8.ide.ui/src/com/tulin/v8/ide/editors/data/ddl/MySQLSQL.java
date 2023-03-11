package com.tulin.v8.ide.editors.data.ddl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.StringArray;

import zigen.plugin.db.DbPluginConstant;

public class MySQLSQL {
	private static String getViewDDL_SQL(String owner, String view) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT view_definition");
		sb.append(" FROM information_schema.views");
		sb.append(" WHERE table_schema = '" + owner + "'");
		sb.append(" AND table_name = '" + view + "'");
		return sb.toString();
	}

	protected static String getViewDDL(String dbName, String owner, String view) throws Exception {
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			con = DBUtils.getAppConn(dbName);
			st = con.createStatement();
			rs = st.executeQuery(getViewDDL_SQL(owner, view));
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtils.CloseConn(con, st, rs);
		}
	}

	public static String getCreateView(String dbName, String owner, String view) {
		StringBuffer wk = new StringBuffer();
		wk.append("CREATE OR REPLACE VIEW");
		wk.append(" AS ");
		wk.append(DbPluginConstant.LINE_SEP);
		try {
			wk.append(getViewDDL(dbName, owner, view));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wk.toString();
	}

	public static String getCreateTableStr(String dbName, String table) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(table);
		sb.append(DbPluginConstant.LINE_SEP);
		sb.append("(");
		sb.append(DbPluginConstant.LINE_SEP);
		sb.append(getColumnDefine(dbName, table));
		sb.append(");");
		String tablecoment = getTableComments(dbName, table);
		if (!"".equals(tablecoment)) {
			sb.append(DbPluginConstant.LINE_SEP);
			sb.append(createCommentOnTableDDL(table, tablecoment));
		}
		setDemiliter(sb, dbName, table);
		return sb.toString();
	}

	private static void setDemiliter(StringBuffer sb, String dbName, String table) {
		sb.append(DbPluginConstant.LINE_SEP);
		StringArray array = new StringArray();
		try {
			List<Map<String, String>> columns = getColumn(dbName, table);
			for (int i = 0; i < columns.size(); i++) {
				if (columns.get(i).get("COMMENTS") != null && !"".equals(columns.get(i).get("COMMENTS"))) {
					array.push(createCommentOnColumnDDL(table, columns.get(i).get("COLUMN_NAME"),
							columns.get(i).get("COMMENTS")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append(array.join(DbPluginConstant.LINE_SEP));
	}

	public static String createCommentOnColumnDDL(String tableName, String columnName, String remarks) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON COLUMN ");
		sb.append(tableName);
		sb.append(".");
		sb.append(columnName);
		sb.append(" IS");
		sb.append(" '" + remarks + "';");
		return sb.toString();
	}

	public static String createCommentOnTableDDL(String tableName, String remarks) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON TABLE ");
		sb.append(tableName);
		sb.append(" IS '");
		sb.append(remarks);
		sb.append("';");
		return sb.toString();
	}

	private static String getColumnDefine(String dbName, String table) {
		StringArray array = new StringArray();
		try {
			List<Map<String, String>> columns = getColumn(dbName, table);
			for (int i = 0; i < columns.size(); i++) {
				String columndip = "  " + columns.get(i).get("COLUMN_NAME") + " " + columns.get(i).get("DATA_TYPE");
				if (columns.get(i).get("DATA_TYPE").indexOf("VARCHAR") > -1) {
					columndip += "(" + columns.get(i).get("CHAR_LENGTH") + ")";
				}
				array.push(columndip);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array.join("," + DbPluginConstant.LINE_SEP) + DbPluginConstant.LINE_SEP;
	}

	private static List<Map<String, String>> getColumn(String dbName, String table) throws Exception {
		String sql = "select t1.TABLE_NAME,t1.COLUMN_NAME,t1.DATA_TYPE,t2.COMMENTS,t1.CHAR_LENGTH "
				+ "from user_tab_columns t1  left join " + "user_col_comments t2 on t1.TABLE_NAME = t2.table_name "
				+ "and t1.COLUMN_NAME = t2.column_name " + "where t2.table_name = '" + table + "'";
		return DBUtils.execQueryforList(dbName, sql);
	}

	private static String getTableComments(String dbkey, String tablename) {
		String result = "";
		String sql = "select t.table_name TABLE_NAME,t.comments TABLE_COMMENT "
				+ " from user_tab_comments t where t.table_name = '" + tablename + "'";
		try {
			List<Map<String, String>> list = DBUtils.execQueryforList(dbkey, sql);
			if (list.size() > 0) {
				Map<String, String> m = list.get(0);
				String val = m.get("TABLE_COMMENT");
				if (val != null && !"null".equals(val)) {
					result = val;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
