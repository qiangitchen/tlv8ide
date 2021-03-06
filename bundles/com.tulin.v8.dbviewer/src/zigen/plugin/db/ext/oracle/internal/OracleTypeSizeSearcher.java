/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class OracleTypeSizeSearcher {

	public static HashMap execute(IDBConfig config) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con);

		} catch (Exception e) {
			throw e;
		}
	}

	public static HashMap execute(Connection con) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getSql());

			HashMap map = new HashMap();
			while (rs.next()) {
				String type = rs.getString("TYPE"); //$NON-NLS-1$
				int size = rs.getInt("TYPE_SIZE"); //$NON-NLS-1$

				map.put(type, size);
			}
			return map;

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

	}

	private static String getSql() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         type"); //$NON-NLS-1$
		sb.append("         ,type_size"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         v$type_size"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         type IN ("); //$NON-NLS-1$
		sb.append("             'KCBH'"); //$NON-NLS-1$
		sb.append("             ,'UB4'"); //$NON-NLS-1$
		sb.append("             ,'KTBBH'"); //$NON-NLS-1$
		sb.append("             ,'KTBIT'"); //$NON-NLS-1$
		sb.append("             ,'KDBH'"); //$NON-NLS-1$
		sb.append("             ,'KDBT'"); //$NON-NLS-1$
		sb.append("             ,'UB1'"); //$NON-NLS-1$
		sb.append("             ,'SB2'"); //$NON-NLS-1$
		sb.append("         )"); //$NON-NLS-1$
		return sb.toString();
	}

}
