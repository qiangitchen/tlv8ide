/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.DatabaseMetaData;

public class DBType {

	public static final int DB_TYPE_UNKNOWN = -1;

	public static final int DB_TYPE_ORACLE = 1;

	public static final int DB_TYPE_MYSQL = 2;

	public static final int DB_TYPE_SYMFOWARE = 3;

	public static final int DB_TYPE_HIRDB = 4;

	public static final int DB_TYPE_SYBASE = 5;

	public static final int DB_TYPE_DERBY = 6;

	public static final int DB_TYPE_SQLITE = 7;

	public static final int DB_TYPE_H2 = 8;

	public static final int DB_TYPE_POSTGRESQL = 9;

	public static final int DB_TYPE_DB2 = 10;

	public static final int DB_TYPE_SQLSERVER = 11;

	public static final int DB_TYPE_HSQLDB = 12;

	public static final int DB_TYPE_INTERBASE = 13;

	public static final int DB_TYPE_DM = 14;

	public static final int DB_TYPE_KINGBASEES = 15;

	public static final int getType(IDBConfig config) {
		return getType(config.getDriverName());
	}

	public static final int getType(DatabaseMetaData objMet) {
		try {
			return getType(objMet.getDriverName());
		} catch (Exception e) {
			throw new IllegalStateException(Messages.getString("DBType.0")); //$NON-NLS-1$
		}

	}

	public static final int getType(String driverName) {
		final String DB_ORACLE = "oracle"; //$NON-NLS-1$
		final String DB_SYMFOWARE = "symfoware"; //$NON-NLS-1$
		final String DB_MYSQL = "mysql"; // com.mysql.jdbc.Driver
		// //$NON-NLS-1$
		final String DB_HIRDB = "hirdb"; // JP.co.Hitachi.soft.HiRDB.JDBC.PrdbDriver
		// //$NON-NLS-1$
		final String DB_SYBASE = "sybase"; // com.sybase.jdbc.SybDriver
		// //$NON-NLS-1$
		final String DB_DERBY = "derby"; // :org.apache.derby.jdbc.ClientDriver
		// //$NON-NLS-1$
		final String DB_SQLITE = "sqlite"; // //$NON-NLS-1$
		final String DB_H2 = "h2"; // org.h2.Driver //$NON-NLS-1$
		final String DB_POSTGRESQL = "postgresql"; // org.postgresql.Driver
		// //$NON-NLS-1$
		final String DB_DB2 = "db2"; // com.ibm.db2.jcc.DB2Driver
		// //$NON-NLS-1$
		final String DB_SQLSERVER = "sqlserver"; // com.microsoft.jdbc.sqlserver.SQLServerDriver,
		// //$NON-NLS-1$
		final String DB_HSQLDB = "hsqldb"; // org.hsqldb.jdbcDriver
		// //$NON-NLS-1$
		final String DB_INTERBASE = "interbase"; //$NON-NLS-1$

		final String DB_DM = "dm"; // dm.jdbc.driver.DmDriver //$NON-NLS-1$

		final String DB_KingDB = "kingbase"; // com.kingbase8.Driver //$NON-NLS-1$

		driverName = driverName.toLowerCase();

		if (driverName.indexOf(DB_ORACLE) >= 0) {
			return DB_TYPE_ORACLE;
		} else if (driverName.indexOf(DB_SYMFOWARE) >= 0) {
			return DB_TYPE_SYMFOWARE;
		} else if (driverName.indexOf(DB_MYSQL) >= 0) {
			return DB_TYPE_MYSQL;
		} else if (driverName.indexOf(DB_HIRDB) >= 0) {
			return DB_TYPE_HIRDB;
		} else if (driverName.indexOf(DB_SYBASE) >= 0) {
			return DB_TYPE_SYBASE;
		} else if (driverName.indexOf(DB_DERBY) >= 0) {
			return DB_TYPE_DERBY;
		} else if (driverName.indexOf(DB_SQLITE) >= 0) {
			return DB_TYPE_SQLITE;
		} else if (driverName.indexOf(DB_H2) >= 0) {
			return DB_TYPE_H2;
		} else if (driverName.indexOf(DB_POSTGRESQL) >= 0) {
			return DB_TYPE_POSTGRESQL;
		} else if (driverName.indexOf(DB_DB2) >= 0) {
			return DB_TYPE_DB2;
		} else if (driverName.indexOf(DB_SQLSERVER) >= 0) {
			return DB_TYPE_SQLSERVER;
		} else if (driverName.indexOf(DB_HSQLDB) >= 0) {
			return DB_TYPE_HSQLDB;
		} else if (driverName.indexOf(DB_INTERBASE) >= 0) {
			return DB_TYPE_INTERBASE;
		} else if (driverName.indexOf(DB_DM) >= 0) {
			return DB_TYPE_DM;
		} else if (driverName.indexOf(DB_KingDB) >= 0) {
			return DB_TYPE_KINGBASEES;
		} else {
			return DB_TYPE_UNKNOWN;
		}
	}
}
