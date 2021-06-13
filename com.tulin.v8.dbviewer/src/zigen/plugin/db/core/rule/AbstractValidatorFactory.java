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

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.derby.DerbyValidatorFactory;
import zigen.plugin.db.core.rule.mysql.MySQLValidatorFactory;
import zigen.plugin.db.core.rule.oracle.OracleValidatorFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareValidatorFactory;
import zigen.plugin.db.preference.PreferencePage;

public abstract class AbstractValidatorFactory implements IValidatorFactory {

	String nullSymbol;

	public static IValidatorFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName());

	}

	public static IValidatorFactory getFactory(DatabaseMetaData objMet) {
		try {
			return getFactory(objMet.getDriverName());

		} catch (SQLException e) {
			throw new IllegalStateException("Failed get DriverName"); //$NON-NLS-1$
		}
	}

	private static Map map = new HashMap();

	private static IValidatorFactory getFactory(String driverName) {

		IValidatorFactory factory = null;

		String key = driverName;

		if (map.containsKey(key)) {
			factory = (IValidatorFactory) map.get(key);
		} else {
			switch (DBType.getType(driverName)) {

				case DBType.DB_TYPE_ORACLE:
					factory = new OracleValidatorFactory();
					break;
				case DBType.DB_TYPE_SYMFOWARE:
					factory = new SymfowareValidatorFactory();
					break;
				case DBType.DB_TYPE_MYSQL:
					factory = new MySQLValidatorFactory();
					break;
				case DBType.DB_TYPE_DERBY:
					factory = new DerbyValidatorFactory();
					break;
				default:
					factory = new DefaultValidatorFactory();
					break;
			}

			map.put(key, factory);
		}
		factory.setNullSymbol(DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL));

		return factory;

	}

	public String validate(TableColumn column, Object value) throws UnSupportedTypeException {

		String columnName = column.getColumnName();

		if (nullSymbol.equals(value)) {
			if (column.isNotNull()) {
				return columnName + Messages.getString("AbstractValidatorFactory.0"); //$NON-NLS-1$
			} else {
				return null;
			}
		}

		return validateDataType(column, value);

	}

	public String getNullSymbol() {
		return nullSymbol;
	}

	public void setNullSymbol(String nullSymbol) {
		this.nullSymbol = nullSymbol;
	}

	abstract String validateDataType(TableColumn column, Object value) throws UnSupportedTypeException;

}
