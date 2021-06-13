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

public class OracleSourceSearcher {

	public static OracleSourceInfo[] execute(IDBConfig config, String owner, String type) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, type);

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleSourceInfo[] execute(Connection con, String owner, String type) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		List list = new ArrayList();

		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, type));

			while (rs.next()) {

				OracleSourceInfo info = new OracleSourceInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TYPE")); //$NON-NLS-1$

				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (OracleSourceInfo[]) list.toArray(new OracleSourceInfo[0]);

	}

	private static String getSQL(String owner, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         DISTINCT OWNER"); //$NON-NLS-1$
		sb.append("         ,NAME"); //$NON-NLS-1$
		sb.append("         ,TYPE"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         ALL_SOURCE"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("         AND TYPE = '" + SQLUtil.encodeQuotation(type) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ORDER BY NAME"); //$NON-NLS-1$

		return sb.toString();

	}

}
