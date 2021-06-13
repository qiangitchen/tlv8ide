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

public class OracleSynonymInfoSearcher {

	public static SynonymInfo execute(IDBConfig config, String owner, String synonym) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, synonym);

		} catch (Exception e) {
			throw e;
		}
	}

	public static SynonymInfo execute(Connection con, String owner, String synonym) throws Exception {
		SynonymInfo info = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, synonym));

			if (rs.next()) {
				info = new SynonymInfo();
				info.setSynonym_name(synonym);
				info.setTable_owner(rs.getString("TABLE_OWNER")); //$NON-NLS-1$
				info.setTable_name(rs.getString("TABLE_NAME")); //$NON-NLS-1$
				info.setDb_link(rs.getString("DB_LINK")); //$NON-NLS-1$
				info.setComments(rs.getString("COMMENTS")); //$NON-NLS-1$
			} else {
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return info;
	}

	private static String getSQL(String owner, String synonym) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT"); //$NON-NLS-1$
		sb.append("        S.TABLE_OWNER"); //$NON-NLS-1$
		sb.append("        ,S.TABLE_NAME"); //$NON-NLS-1$
		sb.append("        ,S.DB_LINK"); //$NON-NLS-1$
		sb.append("        ,T.COMMENTS"); //$NON-NLS-1$
		sb.append("    FROM"); //$NON-NLS-1$
		sb.append("        ALL_SYNONYMS S"); //$NON-NLS-1$
		sb.append("        ,ALL_TAB_COMMENTS T"); //$NON-NLS-1$
		sb.append("    WHERE"); //$NON-NLS-1$
		sb.append("        S.TABLE_OWNER = T.OWNER (+)"); //$NON-NLS-1$
		sb.append("        AND S.TABLE_NAME = T.TABLE_NAME (+)"); //$NON-NLS-1$
		sb.append("        AND S.OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("        AND S.SYNONYM_NAME = '" + SQLUtil.encodeQuotation(synonym) + "'"); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();
	}
}
