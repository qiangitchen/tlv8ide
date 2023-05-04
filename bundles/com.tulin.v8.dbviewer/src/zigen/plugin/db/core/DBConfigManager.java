/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.dialogs.IDBDialogSettings;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DBConfigManager {

	private static IDBDialogSettings setting = DbPlugin.getDefault().getDBDialogSettings();

	public static final String KEY_DBNAME = "NAME"; //$NON-NLS-1$

	public static final String KEY_DRIVER = "DRIVER"; //$NON-NLS-1$

	public static final String KEY_URL = "URL"; //$NON-NLS-1$

	public static final String KEY_USERID = "USERID"; //$NON-NLS-1$

	public static final String KEY_SCHEMA = "SCHEMA"; //$NON-NLS-1$

	public static final String KEY_PASS = "PASSWORD"; //$NON-NLS-1$

	public static final String KEY_CLASSPATH = "CLASSPATH"; //$NON-NLS-1$

	public static final String KEY_CHARSET = "CHARSET"; //$NON-NLS-1$

	public static final String KEY_CONVUNICODE = "CONVUNICODE"; //$NON-NLS-1$

	public static final String KEY_AUTOCOMMIT = "AUTOCOMMIT"; //$NON-NLS-1$

	public static final String KEY_ONLYDEFAULTSCHEMA = "ONLYDEFAULTSCHEMA"; //$NON-NLS-1$

	public static final String KEY_JDBC_TYPE = "JDBCTYPE"; //$NON-NLS-1$

	// for Symfoware
	public static final String KEY_NO_LOCK_MODE = "NOLOCKMODE"; //$NON-NLS-1$

	// for Oracle
	public static final String KEY_CONNECT_AS_SYSDBA = "CONNECT_AS_SYSDBA"; //$NON-NLS-1$

	public static final String KEY_CONNECT_AS_SYSOPEA = "CONNECT_AS_SYSOPEA"; //$NON-NLS-1$

	// for MySQL 5
	public static final String KEY_CONNECT_AS_INFORMATION_SCHEMA = "CONNECT_AS_INFORMATION_SCHEMA"; //$NON-NLS-1$

	public static final String KEY_DISPLAYED_SCHEMAS = "KEY_DISPLAYED_SCHEMAS"; //$NON-NLS-1$

	public static final String KEY_IS_FILTER_PATTERN = "KEY_IS_FILTER_PATTERN"; //$NON-NLS-1$

	public static final String KEY_FILTER_PATTERN = "KEY_FILTER_PATTERN"; //$NON-NLS-1$

	public static void refreshConfig() {
		DbPlugin.getDefault().setConfigischange(true);
		setting = DbPlugin.getDefault().getDBDialogSettings();
	}

	@SuppressWarnings("unused")
	public static IDBConfig[] getDBConfigs() {
		IDBDialogSettings[] sections = setting.getSections();

		List list = new ArrayList(sections.length);

		IDBConfig[] configs = new DBConfig[sections.length];

		for (int i = 0; i < sections.length; i++) {
			IDBDialogSettings settings = sections[i];

			IDBConfig config = new DBConfig();
			config.setDbName(settings.get(KEY_DBNAME));
			// config.setDBName(setting.getName());
			config.setDriverName(settings.get(KEY_DRIVER));
			config.setUrl(settings.get(KEY_URL));
			config.setUserId(settings.get(KEY_USERID));
			config.setSchema(settings.get(KEY_SCHEMA)); // schema add
			config.setPassword(settings.get(KEY_PASS));
			config.setClassPaths(settings.getArray(KEY_CLASSPATH));
			config.setCharset(settings.get(KEY_CHARSET)); // charset add
			config.setConvertUnicode(settings.getBoolean(KEY_CONVUNICODE)); // charset
			// add
			config.setAutoCommit(settings.getBoolean(KEY_AUTOCOMMIT));

			boolean b = settings.getBoolean(KEY_ONLYDEFAULTSCHEMA);

			config.setOnlyDefaultSchema(settings.getBoolean(KEY_ONLYDEFAULTSCHEMA));

			try {
				config.setJdbcType(settings.getInt(KEY_JDBC_TYPE));
			} catch (NumberFormatException e) {
				config.setJdbcType(DBConfig.JDBC_DRIVER_TYPE_4);
			}

			config.setNoLockMode(settings.getBoolean(KEY_NO_LOCK_MODE));

			config.setConnectAsSYSDBA(settings.getBoolean(KEY_CONNECT_AS_SYSDBA));
			config.setConnectAsSYSOPER(settings.getBoolean(KEY_CONNECT_AS_SYSOPEA));

			config.setConnectAsInformationSchema(settings.getBoolean(KEY_CONNECT_AS_INFORMATION_SCHEMA));

			config.setDisplayedSchemas(settings.getSchemaInfos(KEY_DISPLAYED_SCHEMAS));

			config.setCheckFilterPattern(settings.getBoolean(KEY_IS_FILTER_PATTERN));
			config.setFilterPattern(settings.get(KEY_FILTER_PATTERN));

			list.add(config);
			// configs[i] = config;
		}
		Collections.sort(list, new DBConfigSorter());

		return (IDBConfig[]) list.toArray(new DBConfig[0]);
	}

	public static void save(IDBConfig config) throws SameDbNameException {
		if (!setting.hasSection(config.getDbName())) {
			IDBDialogSettings section = setting.addNewSection(config.getDbName());
			setDBConfig(section, config);
			setting.addSection(section);
		} else {
			throw new SameDbNameException(Messages.getString("DBConfigManager.13")); //$NON-NLS-1$
		}
	}

	public static void modify(IDBConfig oldConfig, IDBConfig newConfig) throws SameDbNameException {
		if (oldConfig.getDbName().equals(newConfig.getDbName())) {
			IDBDialogSettings section = setting.getSection(newConfig.getDbName());
			setDBConfig(section, newConfig);
		} else {
			// if (!setting.hasSection(newConfig.getDBName())) {
			if (!hasSection(newConfig.getDbName())) {
				IDBDialogSettings section = setting.addNewSection(newConfig.getDbName());
				setDBConfig(section, newConfig);
				setting.addSection(section);
				setting.removeSection(oldConfig.getDbName());
			} else {
				throw new SameDbNameException(Messages.getString("DBConfigManager.13")); //$NON-NLS-1$
			}
		}
	}

	public static void setAutoCommit(IDBConfig oldConfig, boolean isAutoCommit) {
		IDBDialogSettings section = setting.getSection(oldConfig.getDbName());
		if (section != null) {
			section.put(KEY_AUTOCOMMIT, isAutoCommit);
		}
	}

	public static boolean hasSection(String dbName) {
		return setting.hasSection(dbName);
	}

	public static void remove(IDBConfig config) {
		setting.removeSection(config.getDbName());
	}

	private static void setDBConfig(IDBDialogSettings section, IDBConfig config) {
		if (section != null) {
			section.put(KEY_DBNAME, config.getDbName());
			section.put(KEY_DRIVER, config.getDriverName());
			section.put(KEY_URL, config.getUrl());
			section.put(KEY_USERID, config.getUserId());
			section.put(KEY_SCHEMA, config.getSchema()); // schema add
			section.put(KEY_PASS, config.getPassword());
			section.put(KEY_CLASSPATH, config.getClassPaths());
			section.put(KEY_CHARSET, config.getCharset());// charset add
			section.put(KEY_CONVUNICODE, config.isConvertUnicode());
			section.put(KEY_AUTOCOMMIT, config.isAutoCommit());
			section.put(KEY_ONLYDEFAULTSCHEMA, config.isOnlyDefaultSchema());
			section.put(KEY_JDBC_TYPE, config.getJdbcType());
			section.put(KEY_NO_LOCK_MODE, config.isNoLockMode());
			section.put(KEY_CONNECT_AS_SYSDBA, config.isConnectAsSYSDBA());
			section.put(KEY_CONNECT_AS_SYSOPEA, config.isConnectAsSYSOPER());
			section.put(KEY_CONNECT_AS_INFORMATION_SCHEMA, config.isConnectAsInformationSchema());

			section.put(KEY_DISPLAYED_SCHEMAS, config.getDisplayedSchemas());
			section.put(KEY_IS_FILTER_PATTERN, config.isCheckFilterPattern());
			section.put(KEY_FILTER_PATTERN, config.getFilterPattern());

		} else {
			throw new IllegalStateException("IDBDialogSetting is NULL"); //$NON-NLS-1$
		}
	}

	public static IDBConfig getDBConfig(String dbName) {
		IDBConfig[] configs = DBConfigManager.getDBConfigs();
		for (int i = 0; i < configs.length; i++) {
			IDBConfig config = configs[i];
			if (config.getDbName().equals(dbName)) {
				return config;
			}
		}
		return null;

	}

}
