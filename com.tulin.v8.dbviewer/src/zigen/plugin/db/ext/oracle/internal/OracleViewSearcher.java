/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;

public class OracleViewSearcher {

	public static String execute(IDBConfig config, String owner, String view) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, view);

		} catch (Exception e) {
			throw e;
		}
	}

	public static String execute(Connection con, String owner, String view) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, view));

			if (rs.next()) {
				return rs.getString("TEXT"); //$NON-NLS-1$
			}
			return null;

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

	}

	private static String getSQL(String owner, String view) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TEXT"); //$NON-NLS-1$
		sb.append(" FROM ALL_VIEWS"); //$NON-NLS-1$
		sb.append(" WHERE OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(" AND VIEW_NAME = '" + SQLUtil.encodeQuotation(view) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();

	}

}
