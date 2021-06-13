/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.internal.Table;

public class OracleIndexNameSearcher {

	public static String[] execute(IDBConfig config, Table table) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, table);

		} catch (Exception e) {
			throw e;
		}
	}

	public static String[] execute(Connection con, Table table) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		try {
			List list = new ArrayList();
			st = con.createStatement();
			rs = st.executeQuery(getSql(table));

			// String owner = table.getSchema().getName();
			// String tableName = table.getName();

			// TableColumn[] columns = ColumnSearcher.execute(con, owner,
			// tableName);

			while (rs.next()) {
				list.add(rs.getString("INDEX_NAME")); //$NON-NLS-1$
			}
			return (String[]) list.toArray(new String[0]);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

	}

	private static String getSql(Table table) {
		String owner = table.getSchemaName();
		String tableName = table.getName();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         INDEX_NAME"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         all_indexes"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         table_owner = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     AND table_name = '" + SQLUtil.encodeQuotation(tableName) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

}
