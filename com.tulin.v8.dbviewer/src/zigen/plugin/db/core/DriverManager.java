/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Driver;
import java.util.HashMap;

public class DriverManager {

	private static DriverManager _instance;

	private HashMap driverMap = new HashMap();

	private DriverManager() {}

	private String getKey(IDBConfig config) {
		String key;
		if (config.getJdbcType() == 2) {
			key = config.getDriverName();
		} else {
			key = config.getDbName() + config.getDriverName();
		}

		return key;
	}

	public void removeCach(IDBConfig config) {
		if (config.getJdbcType() == 2) {
			// The cash of Driver is not deleted for TYPE2. (for SymfoWARE)
		} else {
			driverMap.remove(getKey(config));
		}
	}

	public synchronized static DriverManager getInstance() {
		if (_instance == null) {
			_instance = new DriverManager();
		} else {
		}
		return _instance;

	}

	public Driver getDriver(IDBConfig config) throws Exception {
		String key = getKey(config);

		if (driverMap.containsKey(key)) {
			return (Driver) driverMap.get(key);
		} else {
			Driver driver = getDriver(config.getDriverName(), config.getClassPaths());
			driverMap.put(key, driver);
			return driver;
		}

	}

	private Driver getDriver(String driverName, String[] classpaths) throws Exception {
		Class driverClass = null;
		PluginClassLoader loader = PluginClassLoader.getClassLoader(classpaths, getClass().getClassLoader());
		try {
			driverClass = loader.loadClass(driverName);
		} catch (ClassNotFoundException e) {
			try {
				driverClass = PluginClassLoader.getSystemClassLoader().loadClass(driverName);
			} catch (ClassNotFoundException ex) {
				throw ex;
			}
		}
		return (Driver) driverClass.newInstance();
	}

}
