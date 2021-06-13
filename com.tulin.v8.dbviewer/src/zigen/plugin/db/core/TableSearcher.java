/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zigen.plugin.db.core.rule.AbstractTableInfoSearchFactory;
import zigen.plugin.db.core.rule.ITableInfoSearchFactory;
import zigen.plugin.db.core.rule.TableComment;

public class TableSearcher {

	public static TableInfo[] execute(Connection con, String schemaPattern, String[] types) throws Exception {
		return execute(con, schemaPattern, types, null);
	}

	public static TableInfo execute(Connection con, String schemaPattern, String tablePattern, String type) throws Exception {
		return execute(con, schemaPattern, tablePattern, type, null);
	}

	public static TableInfo[] execute(Connection con, String schemaPattern, String[] types, Character encloseChar) throws Exception {

		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();
			ITableInfoSearchFactory factory = AbstractTableInfoSearchFactory.getFactory(objMet);
			if (DBType.getType(objMet) == DBType.DB_TYPE_ORACLE){
				list = factory.getTableInfoAll(con, schemaPattern, types);

			}else if(DBType.getType(objMet) == DBType.DB_TYPE_SYMFOWARE ){
				list = factory.getTableInfoAll(con, schemaPattern, types);

			}else{
				list = new ArrayList();

				if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {
					StringBuffer sb = new StringBuffer();
					sb.append("SELECT");
					sb.append("        TABLE_NAME");
					sb.append("        ,TABLE_TYPE");
					sb.append(" ,TABLE_COMMENT REMARKS");
					// sb.append(" ,'' REMARKS");
					sb.append("    FROM");
					sb.append("        information_schema.TABLES");
					sb.append("    WHERE");
					sb.append("        TABLE_SCHEMA = '" + SQLUtil.encodeQuotation(schemaPattern) + "'");
					if (types.length > 0) {
						sb.append("    AND (");
						for (int i = 0; i < types.length; i++) {
							if (i > 0) {
								sb.append(" OR ");
							}
							sb.append("    TABLE_TYPE Like '%" + SQLUtil.encodeQuotation(types[i]) + "'");
						}
						sb.append("    )");
					}

					st = con.createStatement();
					rs = st.executeQuery(sb.toString());
				} else {
					if (SchemaSearcher.isSupport(con)) {
						rs = objMet.getTables(null, schemaPattern, "%", types); //$NON-NLS-1$
					} else {
						rs = objMet.getTables(null, "%", "%", types); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME"); //$NON-NLS-1$
					TableInfo info = new TableInfo();
					if (encloseChar != null) {
						info.setName(SQLUtil.enclose(tableName, encloseChar.charValue()));
					} else {
						info.setName(tableName);
					}
					info.setTableType(rs.getString("TABLE_TYPE")); //$NON-NLS-1$

					info.setComment(rs.getString("REMARKS")); //$NON-NLS-1$
					list.add(info);

				}

			}
			Collections.sort(list, new TableInfoSorter());
			return (TableInfo[]) list.toArray(new TableInfo[0]);

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);

		}

	}


	public static TableInfo execute(Connection con, String schemaPattern, String tablePattern, String type, Character encloseChar) throws Exception {
		TableInfo info = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();
			ITableInfoSearchFactory factory = AbstractTableInfoSearchFactory.getFactory(objMet);
			if (DBType.getType(objMet) == DBType.DB_TYPE_ORACLE){
				info = factory.getTableInfo(con, schemaPattern, tablePattern, type);

			}else if(DBType.getType(objMet) == DBType.DB_TYPE_SYMFOWARE ){
				info = factory.getTableInfo(con, schemaPattern, tablePattern, type);

			}else{
				info = new TableInfo();

				if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {
					StringBuffer sb = new StringBuffer();
					sb.append("SELECT");
					sb.append("        TABLE_NAME");
					sb.append("        ,TABLE_TYPE");
					sb.append(" ,TABLE_COMMENT REMARKS");
					// sb.append(" ,'' REMARKS");
					sb.append("    FROM");
					sb.append("        information_schema.TABLES");
					sb.append("    WHERE");
					sb.append("        TABLE_SCHEMA = '" + SQLUtil.encodeQuotation(schemaPattern) + "'");
					sb.append("        AND TABLE_NAME = '" + SQLUtil.encodeQuotation(tablePattern) + "'");
					sb.append("        AND TABLE_NAME = '" + SQLUtil.encodeQuotation(tablePattern) + "'");
					sb.append("        AND TABLE_TYPE Like '%" + SQLUtil.encodeQuotation(type) + "'");
					st = con.createStatement();
					rs = st.executeQuery(sb.toString());
				} else {
					if (SchemaSearcher.isSupport(con)) {
						rs = objMet.getTables(null, schemaPattern, "%", new String[]{type}); //$NON-NLS-1$
					} else {
						rs = objMet.getTables(null, "%", "%", new String[]{type}); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				if (rs.next()) {
					String tableName = rs.getString("TABLE_NAME"); //$NON-NLS-1$
					if (encloseChar != null) {
						info.setName(SQLUtil.enclose(tableName, encloseChar.charValue()));
					} else {
						info.setName(tableName);
					}
					info.setTableType(rs.getString("TABLE_TYPE")); //$NON-NLS-1$
					info.setComment(rs.getString("REMARKS")); //$NON-NLS-1$

				}

			}
			return info;

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);

		}

	}
}

class TableInfoSorter implements Comparator {

	public TableInfoSorter() {}

	public int compare(Object o1, Object o2) {

		String firstType = ((TableInfo) o1).getTableType();
		String secondType = ((TableInfo) o2).getTableType();

		if (firstType.equals(secondType)) {
			return 0;
		} else if (firstType.equals("TABLE")) { //$NON-NLS-1$
			return -1;
		} else {
			return (firstType.compareTo(secondType)) * -1;
		}
	}
}
