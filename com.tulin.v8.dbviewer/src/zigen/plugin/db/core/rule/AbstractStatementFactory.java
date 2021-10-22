/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.mysql.MySQLStatementFactory;
import zigen.plugin.db.core.rule.oracle.OracleStatementFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareStatementFactory;

public abstract class AbstractStatementFactory implements IStatementFactory {

	protected static String NULL = "null";

	protected boolean convertUnicode;

	public static IStatementFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName(), config.isConvertUnicode());
	}

	public static IStatementFactory getFactory(DatabaseMetaData objMet, boolean isConvertUnicode) {
		try {
			return getFactory(objMet.getDriverName(), isConvertUnicode);

		} catch (SQLException e) {
			throw new IllegalStateException("Faild get DriverName");
		}

	}

	private static Map map = new HashMap();

	public static IStatementFactory getFactory(String driverName, boolean isConvertUnicode) {

		IStatementFactory factory = null;

		String key = driverName + ":" + isConvertUnicode;

		if (map.containsKey(key)) {
			factory = (IStatementFactory) map.get(key);
		} else {
			switch (DBType.getType(driverName)) {

				case DBType.DB_TYPE_ORACLE:
					factory = new OracleStatementFactory(isConvertUnicode);
					break;
				case DBType.DB_TYPE_MYSQL:
					factory = new MySQLStatementFactory(isConvertUnicode);
					break;


				case DBType.DB_TYPE_SYMFOWARE:
					factory = new SymfowareStatementFactory(isConvertUnicode);
					break;

				default:
					factory = new DefaultStatementFactory(isConvertUnicode);
					break;
			}

			map.put(key, factory);
		}
		return factory;

	}

}
