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

public class OracleTriggerSearcher {

	public static OracleTriggerInfo[] execute(IDBConfig config, String owner, String table) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, table);

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleTriggerInfo[] execute(Connection con, String owner, String table) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		List list = new ArrayList();

		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, table));

			while (rs.next()) {

				OracleTriggerInfo info = new OracleTriggerInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("TRIGGER_NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TRIGGER_TYPE")); //$NON-NLS-1$
				info.setEvent(rs.getString("TRIGGERING_EVENT")); //$NON-NLS-1$

				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (OracleTriggerInfo[]) list.toArray(new OracleTriggerInfo[0]);

	}

	private static String getSQL(String owner, String table) {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        OWNER");
		sb.append("        ,TRIGGER_NAME");
		sb.append("        ,TRIGGER_TYPE");
		sb.append("        ,TRIGGERING_EVENT");
		sb.append("    FROM");
		sb.append("        ALL_TRIGGERS");
		sb.append("    WHERE");
		sb.append("        TABLE_OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("        AND TABLE_NAME = '" + SQLUtil.encodeQuotation(table) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ORDER BY TRIGGER_NAME"); //$NON-NLS-1$

		return sb.toString();

	}

}
