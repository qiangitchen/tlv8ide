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
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;

public class OracleDbBlockSizeSearcher {

	public static int execute(IDBConfig config) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con);
		} catch (Exception e) {
			throw e;
		}
	}

	public static int execute(Connection con) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getSql());

			// HashMap map = new HashMap();
			if (rs.next()) {
				return rs.getInt("VALUE"); //$NON-NLS-1$
			}
			return 0;

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

	}

	private static final String getSql() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         VALUE"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         v$parameter"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         name = 'db_block_size'"); //$NON-NLS-1$
		return sb.toString();
	}

}
