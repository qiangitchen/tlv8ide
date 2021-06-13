/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.TimeWatcher;


/**
 * DefaultColumnSearcherFactory.java.
 *
 * @author ZIGEN
 * @version 1.0
 * @since JDK1.4 history Symbol Date Person Note [1] 2005/11/25 ZIGEN create.
 *
 */
public class DefaultConstraintSearcherFactory extends AbstractConstraintSearcherFactory implements IConstraintSearcherFactory {


	public DefaultConstraintSearcherFactory() {}

	public TablePKColumn[] getPKColumns(Connection con, String schemaPattern, String tableName) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();

			if (schemaPattern != null) {
				rs = objMet.getPrimaryKeys(null, schemaPattern, tableName);
			} else {
				rs = objMet.getPrimaryKeys(null, "%", tableName); //$NON-NLS-1$
			}

			int i = 0;
			while (rs.next()) {
				i++;
				TablePKColumn column = new TablePKColumn();
				column.setSep(rs.getInt("KEY_SEQ")); //$NON-NLS-1$
				column.setColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$
				column.setName(rs.getString("PK_NAME")); //$NON-NLS-1$
				list.add(column);
			}

			Collections.sort(list, new ConstraintSeqSorter());

			return (TablePKColumn[]) list.toArray(new TablePKColumn[0]);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}
	}

	public TableFKColumn[] getFKColumns(Connection con, String schemaPattern, String tableName) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();
			if (schemaPattern != null) {
				rs = objMet.getImportedKeys(null, schemaPattern, tableName);
			} else {
				rs = objMet.getImportedKeys(null, "%", tableName); //$NON-NLS-1$
			}

			while (rs.next()) {
				TableFKColumn column = new TableFKColumn();
				column.setSep(rs.getInt("KEY_SEQ")); //$NON-NLS-1$
				column.setColumnName(rs.getString("FKCOLUMN_NAME")); //$NON-NLS-1$
				column.setName(rs.getString("FK_NAME")); //$NON-NLS-1$
				column.setPkSchema(rs.getString("PKTABLE_SCHEM")); //$NON-NLS-1$
				column.setPkTableName(rs.getString("PKTABLE_NAME")); //$NON-NLS-1$
				column.setPkColumnName(rs.getString("PKCOLUMN_NAME")); //$NON-NLS-1$
				column.setPkName(rs.getString("PK_NAME")); //$NON-NLS-1$
				int delRule = rs.getInt("DELETE_RULE"); //$NON-NLS-1$
				if (delRule == 0) {
					column.setCasucade(true);
				} else {
					column.setCasucade(false);
				}
				list.add(column);
			}

			Collections.sort(list, new ConstraintSeqSorter());
			Collections.sort(list, new ConstraintNameSorter());

			return (TableFKColumn[]) list.toArray(new TableFKColumn[0]);

		} catch (SQLException e) {
			return new TableFKColumn[0];

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
		}
	}

	public TableIDXColumn[] getUniqueIDXColumns(Connection con, String schemaPattern, String tableName, boolean unique) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		try {
			TimeWatcher tw = new TimeWatcher();
			tw.start();

			DatabaseMetaData objMet = con.getMetaData();
			if (schemaPattern != null) {
				rs = objMet.getIndexInfo(null, schemaPattern, tableName, unique, true);
			} else {
				rs = objMet.getIndexInfo(null, "%", tableName, unique, true); //$NON-NLS-1$
			}
			tw.stop();


			while (rs.next()) {
				String indexName = rs.getString("INDEX_NAME"); //$NON-NLS-1$
				if (indexName != null) {
					TableIDXColumn column = new TableIDXColumn();
					column.setName(indexName);
					column.setOrdinal_position(rs.getInt("ORDINAL_POSITION")); //$NON-NLS-1$
					column.setColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$
					if (rs.getBoolean("NON_UNIQUE")) { //$NON-NLS-1$
						column.setNonUnique(true);
					} else {
						column.setNonUnique(false);
					}
					column.setIndexType(getIndexTypeName(rs.getInt("TYPE"))); //$NON-NLS-1$

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
			DbPlugin.log(e);
			throw e;

		} finally {
			ResultSetUtil.close(rs);
		}
	}

	public TableConstraintColumn[] getConstraintColumns(Connection con, String schemaPattern, String tableName) throws Exception {
		return null;
	}
}
