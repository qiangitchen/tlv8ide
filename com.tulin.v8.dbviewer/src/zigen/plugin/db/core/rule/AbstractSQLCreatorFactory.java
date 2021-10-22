/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.db2.DB2SQLCreatorFactory;
import zigen.plugin.db.core.rule.derby.DerbySQLCreatorFactory;
import zigen.plugin.db.core.rule.h2.H2SQLCreatorFactory;
import zigen.plugin.db.core.rule.hsqldb.HSQLDBSQLCreatorFactory;
import zigen.plugin.db.core.rule.mysql.MySQLSQLCreatorFactory;
import zigen.plugin.db.core.rule.oracle.OracleSQLCreatorFactory;
import zigen.plugin.db.core.rule.postgresql.PostgreSQLSQLCreatorFactory;
import zigen.plugin.db.core.rule.sqlite.SqliteSQLCreatorFactory;
import zigen.plugin.db.core.rule.sqlserver.SQLServerSQLCreatorFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareSQLCreatorFactory;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.internal.ITable;

public abstract class AbstractSQLCreatorFactory implements ISQLCreatorFactory {

	protected ITable table;

	public static final ISQLCreatorFactory getFactory(IDBConfig config, ITable table) {
		return getFactory(config.getDriverName(), table);
	}

	public static final ISQLCreatorFactory getFactory(DatabaseMetaData objMet, ITable table) {
		try {
			return getFactory(objMet.getDriverName(), table);

		} catch (SQLException e) {
			throw new IllegalStateException(Messages.getString("AbstractSQLCreatorFactory.Message1")); //$NON-NLS-1$
		}

	}

	public static final ISQLCreatorFactory getFactoryNoCache(IDBConfig config, ITable table) {
		return getFactoryNoCache(config.getDriverName(), table);
	}

	public static final ISQLCreatorFactory getFactoryNoCache(DatabaseMetaData objMet, ITable table) {
		try {
			return getFactoryNoCache(objMet.getDriverName(), table);
		} catch (SQLException e) {
			throw new IllegalStateException(Messages.getString("AbstractSQLCreatorFactory.Message1")); //$NON-NLS-1$
		}
	}

	private static Map map = new HashMap();

	public static final ISQLCreatorFactory getFactory(String driverName, ITable table) {
		ISQLCreatorFactory factory = null;

		String key = driverName;

		if (map.containsKey(key)) {
			factory = (ISQLCreatorFactory) map.get(key);
			if (factory instanceof DefaultSQLCreatorFactory) {
				DefaultSQLCreatorFactory _factory = (DefaultSQLCreatorFactory) factory;
				_factory.setTable(table);
			} else {
				throw new IllegalStateException(Messages.getString("AbstractSQLCreatorFactory.Message2")); //$NON-NLS-1$
			}

		} else {
			switch (DBType.getType(driverName)) {

			case DBType.DB_TYPE_ORACLE:
				factory = new OracleSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_MYSQL:
				factory = new MySQLSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_POSTGRESQL:
				factory = new PostgreSQLSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_SYMFOWARE:
				factory = new SymfowareSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_DB2:
				factory = new DB2SQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_SQLSERVER:
				factory = new SQLServerSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_SQLITE:
				factory = new SqliteSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_H2:
				factory = new H2SQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_HSQLDB:
				factory = new HSQLDBSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_DERBY:
				factory = new DerbySQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_DM:
				factory = new OracleSQLCreatorFactory(table);
				break;
			case DBType.DB_TYPE_KINGBASEES:
				factory = new OracleSQLCreatorFactory(table);
				break;
			default:
				factory = new DefaultSQLCreatorFactory(table);
				break;
			}

			map.put(key, factory);
		}
		return factory;

	}

	public static final ISQLCreatorFactory getFactoryNoCache(String driverName, ITable table) {
		ISQLCreatorFactory factory = null;

		switch (DBType.getType(driverName)) {

		case DBType.DB_TYPE_ORACLE:
			factory = new OracleSQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_MYSQL:
			factory = new MySQLSQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_POSTGRESQL:
			factory = new PostgreSQLSQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_SYMFOWARE:
			factory = new SymfowareSQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_DB2:
			factory = new DB2SQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_SQLSERVER:
			factory = new SQLServerSQLCreatorFactory(table);
			break;

		case DBType.DB_TYPE_H2:
			factory = new H2SQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_HSQLDB:
			factory = new HSQLDBSQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_DERBY:
			factory = new DerbySQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_DM:
			factory = new OracleSQLCreatorFactory(table);
			break;
		case DBType.DB_TYPE_KINGBASEES:
			factory = new OracleSQLCreatorFactory(table);
			break;
		default:
			factory = new DefaultSQLCreatorFactory(table);
			break;
		}
		return factory;

	}

	protected void setDemiliter(StringBuffer sb) {
		String demiliter = DbPlugin.getDefault().getPreferenceStore()
				.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
		if ("/".equals(demiliter)) { //$NON-NLS-1$
			sb.append(DbPluginConstant.LINE_SEP);
		}
		sb.append(demiliter);
		sb.append(DbPluginConstant.LINE_SEP);
	}

	abstract public String createSelect(String condition, int limit);

	protected final String getViewDDL(IDBConfig config, String owner, String view) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			st = con.createStatement();

			ITableInfoSearchFactory factory = AbstractTableInfoSearchFactory.getFactory(con.getMetaData());
			String dbName = factory.getDbName();

			rs = st.executeQuery(getViewDDL_SQL(dbName, owner, view));
			if (rs.next()) {
				return rs.getString(1);
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

	abstract String getViewDDL_SQL(String dbName, String owner, String view);

}
