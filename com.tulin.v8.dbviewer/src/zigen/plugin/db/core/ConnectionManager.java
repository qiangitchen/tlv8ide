/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;

import zigen.plugin.db.DbPlugin;

public class ConnectionManager {

	public static Connection getConnection(IDBConfig config) throws Exception {

		if (config == null) {
			throw new IllegalStateException(Messages.getString("ConnectionManager.0")); //$NON-NLS-1$

		}

		Connection con = null;
		DriverManager manager = DriverManager.getInstance();
		Driver driver = manager.getDriver(config);

		if (driver != null) {
			con = driver.connect(config.getUrl(), config.getProperties());
			config.setDriverVersion(con.getMetaData().getDriverVersion());

			config.setDatabaseProductVersion(getDatabaseProductVersion(con));
			config.setDatabaseProductMajorVersion(getDatabaseMajorVersion(con));
			config.setDatabaseProductMinorVersion(getDatabaseMinorVersion(con));

			checkIsolution(con);

		}


		return con;

	}

	private static String getDatabaseProductVersion(Connection con) {
		String version = "Unknown";
		try {
			version = con.getMetaData().getDatabaseProductVersion();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		} catch (Error e) {
			; // symfoware throw java.lang.AbstractMethodError
		}
		return version;
	}

	private static int getDatabaseMajorVersion(Connection con) {
		int version = 0;
		try {
			version = con.getMetaData().getDatabaseMajorVersion();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		} catch (Error e) {
			;// symfoware throw java.lang.AbstractMethodError
		}
		return version;
	}

	private static int getDatabaseMinorVersion(Connection con) {
		int version = 0;
		try {
			version = con.getMetaData().getDatabaseMinorVersion();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		} catch (Error e) {
			;// symfoware throw  java.lang.AbstractMethodError
		}
		return version;
	}

	private static void checkIsolution(Connection con) {
		try {
			switch (con.getTransactionIsolation()) {
			case Connection.TRANSACTION_NONE:
				break;
			case Connection.TRANSACTION_READ_COMMITTED:
				break;
			case Connection.TRANSACTION_READ_UNCOMMITTED:
				break;
			case Connection.TRANSACTION_REPEATABLE_READ:
				break;
			case Connection.TRANSACTION_SERIALIZABLE:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showWarningMessage(e.getMessage());
		}
	}

	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				rollbackConnection(con);
				con.close();
				con = null;
			} catch (SQLException e) {
				DbPlugin.log(e);
			}
		}
	}

	public static void closeConnection(IDBConfig config, Connection con) {
		if (con != null) {
			try {
				rollbackConnection(con);
				con.close();
				con = null;
			} catch (SQLException e) {
				DbPlugin.log(e);
			}
		}
	}

	public static void shutdown(IDBConfig config) throws Exception {
		DriverManager manager = DriverManager.getInstance();
		try {
			switch (DBType.getType(config)) {

			case DBType.DB_TYPE_DERBY:

				String jdbcDriver = config.getDriverName();
				if (jdbcDriver.indexOf("EmbeddedDriver") > 0) { //$NON-NLS-1$
					Driver driver = manager.getDriver(config);
					driver.connect("jdbc:derby:;shutdown=true", null); //$NON-NLS-1$
				}

				break;
			default:
				break;
			}

		} catch (SQLException e) {
			// SQL exception is generated in the shutdown of derby
			throw e;
		} finally {
			manager.removeCach(config);
		}

	}

	static void rollbackConnection(Connection con) {
		if (con != null) {
			try {
				if (!con.getAutoCommit()) {
					con.rollback();
				}
			} catch (SQLException e) {
				DbPlugin.log(e);
			}
		}
	}

	public static String getDBName(Connection con) {
		String name = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();

			switch (DBType.getType(objMet)) {
				case DBType.DB_TYPE_SYMFOWARE:
					String url = objMet.getURL();
					String[] wk = url.split("/");
					if (wk.length >= 4) {
						String s = wk[3];
						int index = s.indexOf(';');
						if (index >= 0) {
							name = s.substring(0, index);
						} else {
							name = s;
						}
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

}
