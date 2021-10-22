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

public class OracleSequenceSearcher {

	public static OracleSequenceInfo[] execute(IDBConfig config, String owner) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner);

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleSequenceInfo[] execute(Connection con, String owner) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		List list = new ArrayList();

		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner));

			while (rs.next()) {

				OracleSequenceInfo info = new OracleSequenceInfo();
				int i = 0;
				info.setSequece_owner(rs.getString(++i));
				info.setSequence_name(rs.getString(++i));
				info.setMin_value(rs.getBigDecimal(++i));
				info.setMax_value(rs.getBigDecimal(++i));
				info.setIncrement_by(rs.getBigDecimal(++i));
				info.setCycle_flg(rs.getString(++i));
				info.setOrder_flg(rs.getString(++i));
				info.setCache_size(rs.getBigDecimal(++i));
				info.setLast_number(rs.getBigDecimal(++i));

				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (OracleSequenceInfo[]) list.toArray(new OracleSequenceInfo[0]);

	}

	private static String getSQL(String owner) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT"); //$NON-NLS-1$
		sb.append("        *"); //$NON-NLS-1$
		sb.append("    FROM"); //$NON-NLS-1$
		sb.append("        ALL_SEQUENCES"); //$NON-NLS-1$
		sb.append("    WHERE"); //$NON-NLS-1$
		sb.append("        SEQUENCE_OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("    ORDER BY"); //$NON-NLS-1$
		sb.append("        SEQUENCE_NAME"); //$NON-NLS-1$

		return sb.toString();

	}

}
