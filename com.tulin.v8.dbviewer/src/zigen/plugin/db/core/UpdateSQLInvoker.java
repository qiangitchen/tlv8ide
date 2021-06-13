/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.ITable;

public class UpdateSQLInvoker {

	public static int invoke(IDBConfig config, ITable table, TableColumn[] updateColumns, Object[] updateItems, TableColumn[] uniqueColumns, Object[] uniqueItems) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return invoke(con, table, updateColumns, updateItems, uniqueColumns, uniqueItems);

		} catch (Exception e) {
			throw e;
		}
	}

	public static int invoke(Connection con, ITable table, TableColumn[] updateColumns, Object[] updateItems, TableColumn[] uniqueColumns, Object[] uniqueItems) throws Exception {

		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		StringBuffer sb = new StringBuffer();

		int rowAffected;
		PreparedStatement pst = null;

		try {

			IMappingFactory factory = AbstractMappingFactory.getFactory(table.getDbConfig());

			sb.append("UPDATE ");
			sb.append(table.getSqlTableName());
			for (int i = 0; i < updateColumns.length; i++) {
				if (i == 0) {
					sb.append(" SET ");
				} else {
					sb.append(", ");
				}
				sb.append(updateColumns[i].getColumnName() + "= ? ");
			}
			for (int i = 0; i < uniqueColumns.length; i++) {
				if (i == 0) {
					sb.append("WHERE ");
					sb.append(uniqueColumns[i].getColumnName());

					if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
						sb.append(" is null ");
					} else {
						sb.append(" = ? ");
					}
				} else {
					sb.append("AND ");
					sb.append(uniqueColumns[i].getColumnName());
					if (uniqueItems[i] == null || nullSymbol.equals(uniqueItems[i])) {
						sb.append(" is null ");
					} else {
						sb.append(" = ? ");
					}
				}
			}
			pst = con.prepareStatement(sb.toString());

			int index = 0;
			for (int i = 0; i < updateItems.length; i++) {
				index++;
				factory.setObject(pst, index, updateColumns[i], updateItems[i]);
			}

			for (int i = 0; i < uniqueItems.length; i++) {
				if (uniqueItems[i] != null && !nullSymbol.equals(uniqueItems[i])) {
					index++;
					factory.setObject(pst, index, uniqueColumns[i], uniqueItems[i]);
				}

			}

			rowAffected = pst.executeUpdate();

			if (rowAffected == 0) {
				DbPlugin.log("[UpdateSQLInvoker#invoke]" + sb.toString());
			}

			return rowAffected;

		} catch (SQLException e) {
			DbPlugin.log(e);
			String msg = e.getLocalizedMessage();
			switch (table.getDbConfig().getDbType()) {
			case DBType.DB_TYPE_ORACLE:
				if (msg.startsWith("ORA-00997:")) {
					StringBuffer s = new StringBuffer();
					s.append("The table without PrimaryKey including the LONG data type cannot be update. \n");
					s.append("error:" + e.getLocalizedMessage());
					throw new IllegalArgumentException(s.toString());
				}
				break;
			default:
				break;
			}
			throw e;

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(pst);
		}

	}

}
