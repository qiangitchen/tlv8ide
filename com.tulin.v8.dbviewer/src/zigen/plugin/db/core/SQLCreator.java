/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.SQLException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.AbstractStatementFactory;
import zigen.plugin.db.core.rule.IStatementFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.ITable;

public class SQLCreator {

	public static String selectSql(ITable iTable) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM "); //$NON-NLS-1$
		sb.append(iTable.getSqlTableName());
		return sb.toString();
	}

	public static String createSelectSql(TableElement tableElement, boolean isNew) throws SQLException {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		if (isNew) {
			tableElement.modifyUniqueItems();
		}

		ITable table = tableElement.getTable();

		IStatementFactory factory = AbstractStatementFactory.getFactory(table.getDbConfig());

		TableColumn[] uniqueColumns = tableElement.getUniqueColumns();
		Object[] uniqueItems = tableElement.getUniqueItems();

		StringBuffer sb = new StringBuffer();

		sb.append("SELECT * FROM "); //$NON-NLS-1$
		sb.append(table.getSqlTableName());

		for (int i = 0; i < uniqueColumns.length; i++) {
			Object uniqueItem = uniqueItems[i];
			TableColumn col = uniqueColumns[i];

			if (i == 0) {
				sb.append(" WHERE "); //$NON-NLS-1$
				sb.append(col.getColumnName());

				switch (table.getDbConfig().getDbType()) {
				case DBType.DB_TYPE_ORACLE:
					if (uniqueItem == null || nullSymbol.equals(uniqueItem) || "".equals(uniqueItem)) { //$NON-NLS-1$
						sb.append(" is null"); //$NON-NLS-1$
					} else {
						sb.append(" = "); //$NON-NLS-1$
						sb.append(factory.getString(col.getDataType(), uniqueItems[i]));
					}
					break;
				default:
					if (uniqueItem == null || nullSymbol.equals(uniqueItem)) {
						sb.append(" is null"); //$NON-NLS-1$
					} else {
						sb.append(" = "); //$NON-NLS-1$
						sb.append(factory.getString(col.getDataType(), uniqueItems[i]));
					}
					break;
				}

			} else {
				sb.append(" AND "); //$NON-NLS-1$
				sb.append(col.getColumnName());

				switch (table.getDbConfig().getDbType()) {
				case DBType.DB_TYPE_ORACLE:
					if (uniqueItem == null || nullSymbol.equals(uniqueItem) || "".equals(uniqueItem)) { //$NON-NLS-1$
						sb.append(" is null"); //$NON-NLS-1$
					} else {
						sb.append(" = "); //$NON-NLS-1$
						sb.append(factory.getString(col.getDataType(), uniqueItems[i]));
					}
					break;
				default:
					if (uniqueItem == null || nullSymbol.equals(uniqueItem)) {
						sb.append(" is null"); //$NON-NLS-1$
					} else {
						sb.append(" = "); //$NON-NLS-1$
						sb.append(factory.getString(col.getDataType(), uniqueItems[i]));

					}
					break;
				}

			}
		}
		return sb.toString();
	}

	public static String createUpdateSql(ITable table, TableColumn[] updateColumns, Object[] updateItems, TableColumn[] uniqueColumns, Object[] uniqueItems) throws SQLException {

		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
		IStatementFactory factory = AbstractStatementFactory.getFactory(table.getDbConfig());

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE "); //$NON-NLS-1$
		sb.append(table.getSqlTableName() + " "); //$NON-NLS-1$

		for (int i = 0; i < updateColumns.length; i++) {
			TableColumn col = updateColumns[i];

			if (i == 0) {
				sb.append("SET "); //$NON-NLS-1$
			} else {
				sb.append(", "); //$NON-NLS-1$
			}

			sb.append(col.getColumnName());
			sb.append("= "); //$NON-NLS-1$
			sb.append(factory.getString(col.getDataType(), updateItems[i]));
		}
		for (int i = 0; i < uniqueColumns.length; i++) {
			TableColumn col = uniqueColumns[i];
			int type = col.getDataType();
			Object value = uniqueItems[i];

			if (i == 0) {
				sb.append(" WHERE "); //$NON-NLS-1$
				sb.append(col.getColumnName());

				if (value == null || nullSymbol.equals(value)) {
					sb.append(" is null "); //$NON-NLS-1$
				} else {
					sb.append(" = "); //$NON-NLS-1$
					sb.append(factory.getString(type, value));
				}
			} else {
				sb.append(" AND "); //$NON-NLS-1$
				sb.append(col.getColumnName());
				if (value == null || nullSymbol.equals(value)) {
					sb.append(" is null "); //$NON-NLS-1$
				} else {
					sb.append(" = "); //$NON-NLS-1$
					sb.append(factory.getString(type, value));
				}
			}
		}

		return sb.toString();

	}

	public static String createInsertSql(ITable table, TableColumn[] insertColumns, Object[] insertItems) throws Exception {

		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		IStatementFactory factory = AbstractStatementFactory.getFactory(table.getDbConfig());

		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO "); //$NON-NLS-1$
		sb.append(table.getSqlTableName() + " "); //$NON-NLS-1$
		sb.append("VALUES ("); //$NON-NLS-1$
		for (int i = 0; i < insertColumns.length; i++) {
			TableColumn col = insertColumns[i];
			// int type = col.getDataType();
			Object value = insertItems[i];

			if (i > 0) {
				sb.append(","); //$NON-NLS-1$
			}

			if (value == null || nullSymbol.equals(value)) {
				sb.append("null"); //$NON-NLS-1$
			} else {
				sb.append(factory.getString(col.getDataType(), insertItems[i]));
			}
		}
		sb.append(")"); //$NON-NLS-1$

		return sb.toString();

	}

	public static String createDeleteSql(ITable table, TableColumn[] uniqueColumns, Object[] uniqueItems) throws Exception {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
		StringBuffer sb = new StringBuffer();

		IStatementFactory factory = AbstractStatementFactory.getFactory(table.getDbConfig());

		sb.append("DELETE FROM "); //$NON-NLS-1$
		sb.append(table.getSqlTableName() + " "); //$NON-NLS-1$

		for (int i = 0; i < uniqueColumns.length; i++) {
			TableColumn col = uniqueColumns[i];
			int type = col.getDataType();
			Object value = uniqueItems[i];

			if (i == 0) {
				sb.append("WHERE "); //$NON-NLS-1$
				sb.append(uniqueColumns[i].getColumnName());

				if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
					sb.append(" is null "); //$NON-NLS-1$
				} else {
					sb.append(" = "); //$NON-NLS-1$
					sb.append(factory.getString(type, value));
				}
			} else {
				sb.append(" AND "); //$NON-NLS-1$
				sb.append(uniqueColumns[i].getColumnName());
				if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
					sb.append(" is null "); //$NON-NLS-1$
				} else {
					sb.append(" = "); //$NON-NLS-1$
					sb.append(factory.getString(type, value));
				}

			}
		}

		return sb.toString();
	}

}
