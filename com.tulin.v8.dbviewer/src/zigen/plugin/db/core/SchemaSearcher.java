/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SchemaSearcher {

	public static String[] execute(IDBConfig config) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con);

		} catch (Exception e) {
			throw e;
		}

	}

	public static String[] execute(Connection con) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();

			if (!isSupport(con)) {
				return new String[0];
			}

			list = new ArrayList();

			String s = getSchemaSearchSql(con);
			if (s != null) {
				st = con.createStatement();
				rs = st.executeQuery(s);
			} else {
				rs = objMet.getSchemas();
			}

			while (rs.next()) {
//				String wk = rs.getString("TABLE_SCHEM"); //$NON-NLS-1$
				String wk = rs.getString(1); //$NON-NLS-1$
				list.add(wk);
			}


			return (String[]) list.toArray(new String[0]);

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}

	}

	private static String getSchemaSearchSql(Connection con){
		try {
			DatabaseMetaData objMet = con.getMetaData();
			if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {
				return "SELECT SCHEMA_NAME AS TABLE_SCHEM FROM information_schema.SCHEMATA";
			}else if(DBType.getType(objMet) == DBType.DB_TYPE_SYMFOWARE){
				String dbName = ConnectionManager.getDBName(con);
				return "SELECT TRIM(SCHEMA_NAME) AS TABLE_SCHEM FROM RDBII_SYSTEM.RDBII_SCHEMA WHERE DB_NAME = '"+dbName+"'";
			}
		} catch (SQLException e) {
		}
		return null;
	}

	public static boolean isSupport(Connection con) {
		try {
			DatabaseMetaData objMet = con.getMetaData();

			if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {
				return true;
			} else {
				return objMet.supportsSchemasInTableDefinitions();
			}

		} catch (Exception e) {
			return false;
		}
	}

	public static void existSchemaName(Connection con, String target) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();

			if (!isSupport(con)) {
				return;
			}
			String s = getSchemaSearchSql(con);
			if (s != null) {
				st = con.createStatement();
				rs = st.executeQuery(s);
			} else {
				rs = objMet.getSchemas();
			}
			while (rs.next()) {
//				String wk = rs.getString("TABLE_SCHEM"); //$NON-NLS-1$
				String wk = rs.getString(1); //$NON-NLS-1$
				if(wk.equalsIgnoreCase(target)){
					return;
				}
			}

			if(s != null){
				throw new SQLException("The schema doesn't exist.\n" + s);
			}else{
				throw new SQLException("The schema doesn't exist.");
			}

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}

	}
}
