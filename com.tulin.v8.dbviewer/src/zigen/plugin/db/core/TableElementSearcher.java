/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.ITable;

public class TableElementSearcher extends TableManager {


	public static TableElement findElement(Connection con, TableElement element, boolean isNew) throws Exception {
		ResultSet rs = null;
		PreparedStatement pst = null;
		TableElement elements = null;
		TablePKColumn[] pks = null;
		TableIDXColumn[] uidxs = null;
		try {
			ITable table = element.getTable();

			pks = table.getTablePKColumns();

			if (pks == null || pks.length == 0) {
				uidxs = ConstraintUtil.getFirstUniqueIndex(table.getTableUIDXColumns());
			}

			pst = createPreparedStatement(con, element, isNew);

			rs = pst.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();

			TableColumn[] columns = getTableColumns(meta, element.getTable());

			if (rs.next()) {
				if (pks != null && pks.length > 0) {
					elements = createElement(rs, table, columns, pks, element.getRecordNo());
				} else {
					elements = createElement(rs, table, columns, uidxs, element.getRecordNo());
				}

			}

			return elements;

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(pst);
		}

	}

	static PreparedStatement createPreparedStatement(Connection con, TableElement tableElement, boolean isNew) throws Exception {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		PreparedStatement pst = null;

		// <- [002] 2005/11/22 add zigen
		IMappingFactory factory = AbstractMappingFactory.getFactory(tableElement.getTable().getDbConfig());
		// [002] 2005/11/22 add zigen -->

		if (isNew) {
			tableElement.modifyUniqueItems();
		}

		ITable table = tableElement.getTable();
		TableColumn[] uniqueColumns = tableElement.getUniqueColumns();
		Object[] uniqueItems = tableElement.getUniqueItems();

		if (table != null) {
			StringBuffer sb = new StringBuffer();

			sb.append("SELECT * FROM "); //$NON-NLS-1$
			sb.append(table.getSqlTableName() + " "); //$NON-NLS-1$

			for (int i = 0; i < uniqueColumns.length; i++) {
				Object uniqueItem = uniqueItems[i];
				TableColumn uniqueColumn = uniqueColumns[i];
				if (i == 0) {
					sb.append(" WHERE "); //$NON-NLS-1$
					sb.append(uniqueColumn.getColumnName());

					switch (table.getDbConfig().getDbType()) {
					case DBType.DB_TYPE_ORACLE:
						if (uniqueItem == null || nullSymbol.equals(uniqueItem) || "".equals(uniqueItem)) { //$NON-NLS-1$
							sb.append(" is null"); //$NON-NLS-1$
						} else {
							sb.append(" = ?"); //$NON-NLS-1$
						}
						break;
					default:
						if (uniqueItem == null || nullSymbol.equals(uniqueItem)) {
							sb.append(" is null"); //$NON-NLS-1$
						} else {
							sb.append(" = ?"); //$NON-NLS-1$
						}
						break;
					}

				} else {

					sb.append(" AND "); //$NON-NLS-1$
					sb.append(uniqueColumn.getColumnName());

					switch (table.getDbConfig().getDbType()) {
					case DBType.DB_TYPE_ORACLE:
						if (uniqueItem == null || nullSymbol.equals(uniqueItem) || "".equals(uniqueItem)) { //$NON-NLS-1$
							sb.append(" is null"); //$NON-NLS-1$
						} else {
							sb.append(" = ?"); //$NON-NLS-1$
						}
						break;
					default:
						if (uniqueItem == null || nullSymbol.equals(uniqueItem)) {
							sb.append(" is null"); //$NON-NLS-1$
						} else {
							sb.append(" = ?"); //$NON-NLS-1$
						}
						break;
					}

				}
			}
			pst = con.prepareStatement(sb.toString());

			int index = 0;
			for (int i = 0; i < uniqueItems.length; i++) {
				Object uniqueItem = uniqueItems[i];
				TableColumn uniqueColumn = uniqueColumns[i];

				switch (table.getDbConfig().getDbType()) {
				case DBType.DB_TYPE_ORACLE:
					if (uniqueItem != null && !nullSymbol.equals(uniqueItem) && !"".equals(uniqueItem)) { //$NON-NLS-1$
						index++;
						factory.setObject(pst, index, uniqueColumn, uniqueItem);
					}
					break;
				default:
					if (uniqueItem != null && !nullSymbol.equals(uniqueItem)) {
						index++;
						factory.setObject(pst, index, uniqueColumn, uniqueItem);
					}
					break;
				}
			}
			return pst;

		} else {
			throw new Exception(Messages.getString("TableElementSearcher.17")); //$NON-NLS-1$
		}

	}

}
