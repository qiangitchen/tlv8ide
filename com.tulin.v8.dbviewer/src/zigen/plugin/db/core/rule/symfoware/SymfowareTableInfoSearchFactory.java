package zigen.plugin.db.core.rule.symfoware;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.rule.DefaultTableInfoSearchFactory;


public class SymfowareTableInfoSearchFactory extends DefaultTableInfoSearchFactory{

	public SymfowareTableInfoSearchFactory(DatabaseMetaData meta){
		super(meta);
	}
	public String getTableInfoAllSql(String schema, String[] types) {
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT");
		sb.append("        TRIM(T.TABLE_NAME) TABLE_NAME");
		sb.append("        ,T.TABLE_TYPE AS TABLE_TYPE");
		sb.append("        ,TRIM(COM.COMMENT_VALUE) AS REMARKS");
		sb.append("    FROM");
		sb.append("        RDBII_SYSTEM.RDBII_TABLE T");
		sb.append("            LEFT OUTER JOIN RDBII_SYSTEM.RDBII_COMMENT COM");
		sb.append("                ON (");
		sb.append("                    T.DB_CODE = COM.DB_CODE");
		sb.append("                    AND T.SCHEMA_CODE = COM.SCHEMA_CODE");
		sb.append("                    AND T.TABLE_CODE = COM.TABLE_CODE");
		sb.append("                    AND COM.COMMENT_TYPE = 'TV'");
		sb.append("                )");
		sb.append("    WHERE");
		sb.append("        T.DB_NAME = '" + SQLUtil.encodeQuotation(getDbName()) + "'");
		sb.append("        AND T.SCHEMA_NAME = '" + SQLUtil.encodeQuotation(schema) + "'");
		if (types.length > 0) {
			sb.append("        AND (");
			for (int i = 0; i < types.length; i++) {
				if (i > 0) {
					sb.append(" OR ");
				}
				sb.append("    T.TABLE_TYPE = '" + getSymfoTableType(types[i]) + "'");
			}
			sb.append("    )");
		}

		return sb.toString();
	}

	private String getSymfoTableType(String type){
		String symfoType = null;
		if("TABLE".equalsIgnoreCase(type)){
			symfoType = "BS";
		}else if("VIEW".equalsIgnoreCase(type)){
			symfoType = "VW";
		}
		return symfoType;
	}
	public String getTableInfoSql(String schema, String tableName, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        TRIM(T.TABLE_NAME) TABLE_NAME");
		sb.append("        ,T.TABLE_TYPE AS TABLE_TYPE");
		sb.append("        ,TRIM(COM.COMMENT_VALUE) AS REMARKS");
		sb.append("    FROM");
		sb.append("        RDBII_SYSTEM.RDBII_TABLE T");
		sb.append("            LEFT OUTER JOIN RDBII_SYSTEM.RDBII_COMMENT COM");
		sb.append("                ON (");
		sb.append("                    T.DB_CODE = COM.DB_CODE");
		sb.append("                    AND T.SCHEMA_CODE = COM.SCHEMA_CODE");
		sb.append("                    AND T.TABLE_CODE = COM.TABLE_CODE");
		sb.append("                    AND COM.COMMENT_TYPE = 'TV'");
		sb.append("                )");
		sb.append("    WHERE");
		sb.append("        T.DB_NAME = '" + SQLUtil.encodeQuotation(getDbName()) + "'");
		sb.append("        AND T.SCHEMA_NAME = '" + SQLUtil.encodeQuotation(schema) + "'");
		sb.append("        AND T.TABLE_NAME = '" + SQLUtil.encodeQuotation(tableName) + "'");
		sb.append("        AND T.TABLE_TYPE = '" + getSymfoTableType(type) + "'");
		return sb.toString();
	}

	public String getDbName() {
		String name = null;
		try {
			String url = meta.getURL();
			String[] wk = url.split("/");
			if (wk.length >= 4) {
				String s = wk[3];
				int index = s.indexOf(';');
				if (index >= 0) {
					name = s.substring(0, index);
				} else {
					name = s;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	public List getTableInfoAll(Connection con, String owner, String[] types) throws Exception {
		List result = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			String query = getTableInfoAllSql(owner, types);
			System.out.println(query);
			if (query != null) {
				result = new ArrayList();
				rs = st.executeQuery(query);
				while (rs.next()) {
					TableInfo info = new TableInfo();
					info.setName(rs.getString("TABLE_NAME"));
					String type = rs.getString("TABLE_TYPE");
					if("BS".equals(type)){
						info.setTableType("TABLE");
					}else if("VW".equals(type)){
						info.setTableType("VIEW");
					}else if("TD".equals(type) || "TP".equals(type) ){
						info.setTableType("Temporary Table");
					}else{
						info.setTableType("Unknown");
					}
					info.setComment(rs.getString("REMARKS"));
					result.add(info);
				}
			}
		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return result;
	}
	public TableInfo getTableInfo(Connection con, String owner, String tableName, String type) throws Exception {
		TableInfo info = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			String query = getTableInfoSql(owner, tableName, type);
			System.out.println(query);
			if (query != null) {
				rs = st.executeQuery(query);
				while (rs.next()) {
					info = new TableInfo();
					info.setName(rs.getString("TABLE_NAME"));
					String t = rs.getString("TABLE_TYPE");
					if("BS".equals(t)){
						info.setTableType("TABLE");
					}else if("VW".equals(t)){
						info.setTableType("VIEW");
					}else if("TD".equals(t) || "TP".equals(t) ){
						info.setTableType("Temporary Table");
					}else{
						info.setTableType("Unknown");
					}
					info.setComment(rs.getString("REMARKS"));
				}
			}
		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return info;
	}

}
