/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.MaxRecordException;

public class SQLInvoker {

	public static TableElement[] executeQuery(IDBConfig config, String query) throws Exception, MaxRecordException {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return executeQuery(con, query, config.isConvertUnicode(), config.isNoLockMode());
		} catch (Exception e) {
			throw e;
		}
	}

	public static TableElement[] executeQuery(Connection con, String query, boolean convUnicode, boolean isNoLockMode) throws Exception, MaxRecordException {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();

			if (isNoLockMode) {
				if (DBType.DB_TYPE_SYMFOWARE == DBType.getType(con.getMetaData())) {
					if (!query.trim().endsWith("WITH OPTION LOCK_MODE(NL)")) { //$NON-NLS-1$
						query += " WITH OPTION LOCK_MODE(NL)"; //$NON-NLS-1$
					}
				}
			}

			rs = stmt.executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			List list = new ArrayList();
			TableColumn[] columns = getTableColumns(meta);
			TableElement header = new TableHeaderElement(columns);
			list.add(header);
			int size = meta.getColumnCount();
			int recordNo = 1;
			int limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
			IMappingFactory factory = AbstractMappingFactory.getFactory(con.getMetaData(), convUnicode);

			while (rs.next()) {
				if (limit > 0 && recordNo > limit) {
					String msg = Messages.getString("SQLInvoker.2");
					throw new MaxRecordException(msg, (TableElement[]) list.toArray(new TableElement[0]));
				}

				Object[] items = new Object[size];
				for (int i = 0; i < size; i++) {
					// <- [002] 2005/11/22 add zigen
					items[i] = factory.getObject(rs, i + 1);
					/*
					 * factory.getObject(rs, i + 1); }
					 */
					// [002] 2005/11/22 add zigen -->
				}

				TableElement elements = new TableElement(recordNo, columns, items);
				recordNo++;
				list.add(elements);
			}

			return (TableElement[]) list.toArray(new TableElement[0]);

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(stmt);
		}

	}

	
	public static TableElement[] executeQueryForPager(Connection con, String query, boolean convUnicode, boolean isNoLockMode, int offset, int limit) throws Exception{
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();

			if (isNoLockMode) {
				if (DBType.DB_TYPE_SYMFOWARE == DBType.getType(con.getMetaData())) {
					if (!query.trim().endsWith("WITH OPTION LOCK_MODE(NL)")) { //$NON-NLS-1$
						query += " WITH OPTION LOCK_MODE(NL)"; //$NON-NLS-1$
					}
				}
			}

			rs = stmt.executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			List list = new ArrayList();
			TableColumn[] columns = getTableColumns(meta);
			TableElement header = new TableHeaderElement(columns);
			list.add(header);
			int size = meta.getColumnCount();
			int recordNo = 0;
			IMappingFactory factory = AbstractMappingFactory.getFactory(con.getMetaData(), convUnicode);

			int addCount = 0;
			while (rs.next()) {
				recordNo++;

				if (recordNo >= offset && addCount < limit) {
					Object[] items = new Object[size];
					for (int i = 0; i < size; i++) {
						items[i] = factory.getObject(rs, i + 1);
					}

					TableElement elements = new TableElement(recordNo, columns, items);
					list.add(elements);
					addCount++;
				}

			}

			return (TableElement[]) list.toArray(new TableElement[0]);

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(stmt);
		}

	}
	
	
	private static TableColumn[] getTableColumns(ResultSetMetaData meta) throws SQLException {
		int count = meta.getColumnCount();

		TableColumn[] columns = new TableColumn[count];

		for (int i = 0; i < count; i++) {
			TableColumn column = new TableColumn();
			column.setColumnName(meta.getColumnName(i + 1));
			column.setTypeName(meta.getColumnTypeName(i + 1));
			column.setDataType(meta.getColumnType(i + 1));
			column.setColumnSize(meta.getColumnDisplaySize(i + 1));
			column.setDecimalDigits(meta.getScale(i + 1));

			columns[i] = column;
		}
		return columns;

	}

	public static int executeUpdate(IDBConfig config, String sql) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return executeUpdate(con, sql);
		} catch (Exception e) {
			throw e;
		}
	}

	public static int executeUpdate(Connection con, String sql) throws Exception {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			return stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw e;
		} finally {
			StatementUtil.close(stmt);
		}
	}

	// for * MySQL (SELECT FROM INTO)
	public static boolean execute(Connection con, String sql) throws Exception {
		boolean b = false;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.execute(sql);
			b = true;
		} catch (Exception e) {
			throw e;
		} finally {
			StatementUtil.close(stmt);
		}
		return b;
	}

	public static long executeQueryTotalCount(Connection con, String query, int timeoutSec) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		long totalCount = 0l;
		try {

			stmt = con.createStatement();

			if (timeoutSec > 0) {

				try {
					stmt.setQueryTimeout(timeoutSec);
				} catch (SQLException e) {
					e.getStackTrace();
				}
			}

			rs = stmt.executeQuery(query);
			if (rs.next()) {
				totalCount = rs.getLong(1);
			}

			return totalCount;
		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(stmt);
		}

	}
}
