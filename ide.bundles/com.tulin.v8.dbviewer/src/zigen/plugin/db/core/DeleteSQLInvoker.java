/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.PreparedStatement;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.ITable;

public class DeleteSQLInvoker {

	public static int invoke(IDBConfig config, ITable table, TableColumn[] uniqueColumns, Object[] uniqueItems) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return invoke(con, table, uniqueColumns, uniqueItems);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		}
	}

	public static int invoke(Connection con, ITable table, TableColumn[] uniqueColumns, Object[] uniqueItems) throws Exception {
		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		StringBuffer sb = new StringBuffer();

		int rowAffected;
		PreparedStatement pst = null;

		try {
			IMappingFactory factory = AbstractMappingFactory.getFactory(table.getDbConfig());

			sb.append("DELETE FROM "); //$NON-NLS-1$
			sb.append(table.getSqlTableName() + " "); //$NON-NLS-1$

			for (int i = 0; i < uniqueColumns.length; i++) {
				if (i == 0) {
					sb.append("WHERE "); //$NON-NLS-1$
					sb.append(uniqueColumns[i].getColumnName());

					if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
						sb.append(" is null "); //$NON-NLS-1$
					} else {
						sb.append(" = ? "); //$NON-NLS-1$
					}
				} else {
					sb.append("AND "); //$NON-NLS-1$
					sb.append(uniqueColumns[i].getColumnName());
					if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
						sb.append(" is null "); //$NON-NLS-1$
					} else {
						sb.append(" = ? "); //$NON-NLS-1$
					}

				}
			}
			pst = con.prepareStatement(sb.toString());
			int index = 0;
			for (int i = 0; i < uniqueItems.length; i++) {
				if (uniqueItems[i] != null && !nullSymbol.equals(uniqueItems[i])) {
					index++;
					factory.setObject(pst, index, uniqueColumns[i], uniqueItems[i]);
				}
			}
			rowAffected = pst.executeUpdate();
			return rowAffected;

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			StatementUtil.close(pst);
		}

	}

}
