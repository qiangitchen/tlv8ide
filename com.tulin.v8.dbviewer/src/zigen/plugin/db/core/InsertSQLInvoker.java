/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.PreparedStatement;

import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.ui.internal.ITable;

public class InsertSQLInvoker {

	public static int invoke(IDBConfig config, ITable table, TableColumn[] insertColumns, Object[] insertItems) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return invoke(con, table, insertColumns, insertItems);
		} catch (Exception e) {
			throw e;
		}
	}

	public static int invoke(Connection con, ITable table, TableColumn[] insertColumns, Object[] insertItems) throws Exception {
		StringBuffer sb = new StringBuffer();

		int rowAffected;
		PreparedStatement pst = null;
		try {
			IMappingFactory factory = AbstractMappingFactory.getFactory(table.getDbConfig());
			sb.append("INSERT INTO "); //$NON-NLS-1$
			sb.append(table.getSqlTableName() + " "); //$NON-NLS-1$
			sb.append("VALUES ("); //$NON-NLS-1$
			for (int i = 0; i < insertItems.length; i++) {
				if (i == 0) {
					sb.append("?"); //$NON-NLS-1$
				} else {
					sb.append(", ?"); //$NON-NLS-1$
				}
			}
			sb.append(")"); //$NON-NLS-1$
			pst = con.prepareStatement(sb.toString());

			int index = 0;
			for (int i = 0; i < insertItems.length; i++) {
				index++;
				factory.setObject(pst, index, insertColumns[i], insertItems[i]);

			}
			rowAffected = pst.executeUpdate();
			return rowAffected;
		} catch (Exception e) {
			throw e;
		} finally {
			StatementUtil.close(pst);
		}

	}

}
