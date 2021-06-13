/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.oracle;

import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.DefaultValidatorFactory;
import zigen.plugin.db.core.rule.IValidatorFactory;
import zigen.plugin.db.core.rule.UnSupportedTypeException;
import zigen.plugin.db.core.rule.Validator;

public class OracleValidatorFactory extends DefaultValidatorFactory implements IValidatorFactory {

	public String validateDataType(TableColumn column, Object value) throws UnSupportedTypeException {

		int type = column.getDataType();
		String columnName = column.getColumnName();
		switch (type) {
		// case Types.DATE:
		// return validate_DATE(columnName, (String) value);
		// case Types.TIMESTAMP:
		// return validate_TIMESTAMP(columnName, (String) value);

		default:
			return super.validateDataType(column, value);
		}

	}

	protected String validate_DATE(String columnName, String value) {
		return Validator.timestamp_Check(columnName, value);
	}

	protected String validate_TIMESTAMP(String columnName, String value) {
		return Validator.timestamp2_Check(columnName, value);
	}

}
