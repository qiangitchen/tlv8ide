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

public class OracleSourceErrorSearcher {

	public static OracleSourceErrorInfo[] execute(IDBConfig config, String owner, String name, String type) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, name, type);

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleSourceErrorInfo[] execute(Connection con, String owner, String name, String type) throws Exception {
		List list = null;
		ResultSet rs = null;
		Statement st = null;

		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, name, type));

			list = new ArrayList();

			while (rs.next()) {
				OracleSourceErrorInfo info = new OracleSourceErrorInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TYPE")); //$NON-NLS-1$
				info.setLine(rs.getInt("LINE")); //$NON-NLS-1$
				info.setPosition(rs.getInt("POSITION")); //$NON-NLS-1$
				info.setText(rs.getString("TEXT")); //$NON-NLS-1$
				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (OracleSourceErrorInfo[]) list.toArray(new OracleSourceErrorInfo[0]);

	}

	private static String getSQL(String owner, String name, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         OWNER"); //$NON-NLS-1$
		sb.append("         ,NAME"); //$NON-NLS-1$
		sb.append("         ,TYPE"); //$NON-NLS-1$
		sb.append("         ,LINE"); //$NON-NLS-1$
		sb.append("         ,POSITION"); //$NON-NLS-1$
		sb.append("         ,TEXT"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         ALL_ERRORS"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
//		sb.append("         AND NAME = '" + SQLUtil.encodeQuotation(name) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
//		sb.append("         AND TYPE = '" + SQLUtil.encodeQuotation(type) + "'"); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append("         AND NAME = '" + SQLUtil.encodeQuotation(name.toUpperCase()) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("         AND TYPE = '" + SQLUtil.encodeQuotation(type.toUpperCase()) + "'"); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append("     ORDER BY SEQUENCE"); //$NON-NLS-1$

		return sb.toString();

	}
	public static OracleSourceErrorInfo[] execute(IDBConfig config, String owner, String type) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, owner, type);

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleSourceErrorInfo[] execute(Connection con, String owner, String type) throws Exception {
		List list = null;
		ResultSet rs = null;
		Statement st = null;

		try {
			st = con.createStatement();
			rs = st.executeQuery(getSQL(owner, type));

			list = new ArrayList();

			while (rs.next()) {
				OracleSourceErrorInfo info = new OracleSourceErrorInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TYPE")); //$NON-NLS-1$
				info.setLine(rs.getInt("LINE")); //$NON-NLS-1$
				info.setPosition(rs.getInt("POSITION")); //$NON-NLS-1$
				info.setText(rs.getString("TEXT")); //$NON-NLS-1$
				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (OracleSourceErrorInfo[]) list.toArray(new OracleSourceErrorInfo[0]);

	}
	private static String getSQL(String owner, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         OWNER"); //$NON-NLS-1$
		sb.append("         ,NAME"); //$NON-NLS-1$
		sb.append("         ,TYPE"); //$NON-NLS-1$
		sb.append("         ,LINE"); //$NON-NLS-1$
		sb.append("         ,POSITION"); //$NON-NLS-1$
		sb.append("         ,TEXT"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         ALL_ERRORS"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         OWNER = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("         AND TYPE = '" + SQLUtil.encodeQuotation(type.toUpperCase()) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     ORDER BY OWNER, NAME, TYPE, SEQUENCE"); //$NON-NLS-1$

		return sb.toString();

	}

}
