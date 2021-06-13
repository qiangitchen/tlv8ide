/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConstraintNameSorter;
import zigen.plugin.db.core.ConstraintSeqSorter;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableIDXColumn;

public class OracleIndexSearcher {

	private static String getIndexQuery(String schemaPattern, String tableName, boolean uniqueIndex) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT"); //$NON-NLS-1$
		sb.append("        T.*"); //$NON-NLS-1$
		sb.append("    FROM"); //$NON-NLS-1$
		sb.append("        ("); //$NON-NLS-1$
		sb.append("            SELECT"); //$NON-NLS-1$
		sb.append("                    IND.TABLE_OWNER"); //$NON-NLS-1$
		sb.append("                    ,IND.TABLE_NAME"); //$NON-NLS-1$
		sb.append("                    ,IND.INDEX_NAME"); //$NON-NLS-1$
		sb.append("                    ,COL.COLUMN_POSITION AS ORDINAL_POSITION"); //$NON-NLS-1$
		sb.append("                    ,COL.COLUMN_NAME"); //$NON-NLS-1$
		sb.append("                    ,IND.UNIQUENESS"); //$NON-NLS-1$
		sb.append("                    ,IND.INDEX_TYPE"); //$NON-NLS-1$
		sb.append("                FROM"); //$NON-NLS-1$
		sb.append("                    ALL_IND_COLUMNS COL"); //$NON-NLS-1$
		sb.append("                    ,ALL_INDEXES IND"); //$NON-NLS-1$
		sb.append("                WHERE"); //$NON-NLS-1$
		sb.append("                    IND.OWNER = COL.INDEX_OWNER"); //$NON-NLS-1$
		sb.append("                    AND IND.INDEX_NAME = COL.INDEX_NAME"); //$NON-NLS-1$
		sb.append("                    AND IND.TABLE_OWNER = COL.TABLE_OWNER"); //$NON-NLS-1$
		sb.append("                    AND IND.TABLE_NAME = COL.TABLE_NAME"); //$NON-NLS-1$
		if (!uniqueIndex) {
			sb.append("                    AND IND.UNIQUENESS = 'NONUNIQUE'"); //$NON-NLS-1$
		} else {
			sb.append("                    AND IND.UNIQUENESS = 'UNIQUE'"); //$NON-NLS-1$
		}
		sb.append("                    AND IND.TABLE_OWNER = '" + SQLUtil.encodeQuotation(schemaPattern) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("                    AND IND.TABLE_NAME = '" + SQLUtil.encodeQuotation(tableName) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("        ) T, ALL_CONSTRAINTS CONS"); //$NON-NLS-1$
		sb.append("        WHERE T.TABLE_OWNER = CONS.OWNER(+)"); //$NON-NLS-1$
		sb.append("        AND T.TABLE_NAME = CONS.TABLE_NAME(+)"); //$NON-NLS-1$
		sb.append("        AND T.INDEX_NAME = CONS.CONSTRAINT_NAME(+)"); //$NON-NLS-1$
		sb.append("        AND CONS.CONSTRAINT_NAME IS NULL"); //$NON-NLS-1$


		return sb.toString();

	}

	public static TableIDXColumn[] getIDXColumns(Connection con, String schemaPattern, String tableName, boolean uniqueIndex) throws Exception {
		List list = new ArrayList();

		ResultSet rs = null;
		Statement st = null;
		try {

			st = con.createStatement();
			String sql = getIndexQuery(schemaPattern, tableName, uniqueIndex);
			rs = st.executeQuery(sql);

			while (rs.next()) {
				String indexName = rs.getString("INDEX_NAME"); //$NON-NLS-1$

				if (indexName != null) {

					TableIDXColumn column = new TableIDXColumn();

					column.setName(indexName);
					column.setOrdinal_position(rs.getInt("ORDINAL_POSITION")); //$NON-NLS-1$
					column.setColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$

					if ("UNIQUE".equals(rs.getString("UNIQUENESS"))) { //$NON-NLS-1$ //$NON-NLS-2$
						column.setNonUnique(false);
					} else {
						column.setNonUnique(true);
					}
					column.setIndexType(rs.getString("INDEX_TYPE")); //$NON-NLS-1$

					list.add(column);
				}

			}

			Collections.sort(list, new ConstraintSeqSorter());
			Collections.sort(list, new ConstraintNameSorter());

			return (TableIDXColumn[]) list.toArray(new TableIDXColumn[0]);

		} catch (SQLException e) {
			DbPlugin.log(e);
			return new TableIDXColumn[0];

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
	}

	private static String getIndexQuery(String schemaPattern, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT"); //$NON-NLS-1$
		sb.append("        T.*"); //$NON-NLS-1$
		sb.append("    FROM"); //$NON-NLS-1$
		sb.append("        ("); //$NON-NLS-1$
		sb.append("            SELECT"); //$NON-NLS-1$
		sb.append("                    IND.TABLE_OWNER"); //$NON-NLS-1$
		sb.append("                    ,IND.TABLE_NAME"); //$NON-NLS-1$
		sb.append("                    ,IND.INDEX_NAME"); //$NON-NLS-1$
		sb.append("                    ,COL.COLUMN_POSITION AS ORDINAL_POSITION"); //$NON-NLS-1$
		sb.append("                    ,COL.COLUMN_NAME"); //$NON-NLS-1$
		sb.append("                    ,IND.UNIQUENESS"); //$NON-NLS-1$
		sb.append("                    ,IND.INDEX_TYPE"); //$NON-NLS-1$
		sb.append("                FROM"); //$NON-NLS-1$
		sb.append("                    ALL_IND_COLUMNS COL"); //$NON-NLS-1$
		sb.append("                    ,ALL_INDEXES IND"); //$NON-NLS-1$
		sb.append("                WHERE"); //$NON-NLS-1$
		sb.append("                    IND.OWNER = COL.INDEX_OWNER"); //$NON-NLS-1$
		sb.append("                    AND IND.INDEX_NAME = COL.INDEX_NAME"); //$NON-NLS-1$
		sb.append("                    AND IND.TABLE_OWNER = COL.TABLE_OWNER"); //$NON-NLS-1$
		sb.append("                    AND IND.TABLE_NAME = COL.TABLE_NAME"); //$NON-NLS-1$
		// if (!uniqueIndex) {
		// sb.append(" AND IND.UNIQUENESS = 'NONUNIQUE'"); //$NON-NLS-1$
		// } else {
		// sb.append(" AND IND.UNIQUENESS = 'UNIQUE'"); //$NON-NLS-1$
		// }
		sb.append("                    AND IND.TABLE_OWNER = '" + SQLUtil.encodeQuotation(schemaPattern) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("                    AND IND.TABLE_NAME = '" + SQLUtil.encodeQuotation(tableName) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("        ) T, ALL_CONSTRAINTS CONS"); //$NON-NLS-1$
		sb.append("        WHERE T.TABLE_OWNER = CONS.OWNER(+)"); //$NON-NLS-1$
		sb.append("        AND T.TABLE_NAME = CONS.TABLE_NAME(+)"); //$NON-NLS-1$
		sb.append("        AND T.INDEX_NAME = CONS.CONSTRAINT_NAME(+)"); //$NON-NLS-1$
		sb.append("        AND CONS.CONSTRAINT_NAME IS NULL"); //$NON-NLS-1$


		return sb.toString();

	}


	public static TableIDXColumn[][] getIDXColumns(Connection con, String schemaPattern, String tableName) throws Exception {

		ResultSet rs = null;
		Statement st = null;

		List uniquelist = new ArrayList();
		List nonUniquelist = new ArrayList();
		try {

			st = con.createStatement();
			String sql = getIndexQuery(schemaPattern, tableName);
			rs = st.executeQuery(sql);

			while (rs.next()) {
				String indexName = rs.getString("INDEX_NAME"); //$NON-NLS-1$

				if (indexName != null) {

					TableIDXColumn column = new TableIDXColumn();

					column.setName(indexName);
					column.setOrdinal_position(rs.getInt("ORDINAL_POSITION")); //$NON-NLS-1$
					column.setColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$

					if ("UNIQUE".equals(rs.getString("UNIQUENESS"))) { //$NON-NLS-1$ //$NON-NLS-2$
						column.setNonUnique(false);
					} else {
						column.setNonUnique(true);
					}
					column.setIndexType(rs.getString("INDEX_TYPE")); //$NON-NLS-1$


					if (!column.isNonUnique()) {
						uniquelist.add(column);
					} else {
						nonUniquelist.add(column);
					}

				}

			}
			Collections.sort(uniquelist, new ConstraintSeqSorter());
			Collections.sort(uniquelist, new ConstraintNameSorter());

			Collections.sort(nonUniquelist, new ConstraintSeqSorter());
			Collections.sort(nonUniquelist, new ConstraintNameSorter());


			TableIDXColumn[] uind = (TableIDXColumn[]) uniquelist.toArray(new TableIDXColumn[uniquelist.size()]);
			TableIDXColumn[] nuind = (TableIDXColumn[]) nonUniquelist.toArray(new TableIDXColumn[nonUniquelist.size()]);

			TableIDXColumn[][] result = new TableIDXColumn[][] {uind, nuind};
			return result;

		} catch (SQLException e) {
			DbPlugin.log(e);
			return new TableIDXColumn[0][0];

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
	}

}
