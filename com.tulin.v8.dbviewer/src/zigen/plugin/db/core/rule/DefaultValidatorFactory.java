/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.Types;

import zigen.plugin.db.core.TableColumn;

public class DefaultValidatorFactory extends AbstractValidatorFactory {

	public String validateDataType(TableColumn column, Object value) throws UnSupportedTypeException {
		int type = column.getDataType();
		String columnName = column.getColumnName().toUpperCase();

		switch (type) {
		case Types.CHAR:
			return validate_CHAR(columnName, (String) value);

		case Types.VARCHAR:
			return validate_VARCHAR(columnName, (String) value);

		case Types.LONGVARCHAR: // -1
			return validate_LONGVARCHAR(columnName, (String) value);

		case Types.BIT:
		case Types.BOOLEAN:
			return validate_BIT(columnName, (String) value);

		case Types.TINYINT:
			return validate_TINYINT(columnName, (String) value);

		case Types.INTEGER:
			return validate_INTEGER(columnName, (String) value);

		case Types.SMALLINT:
			return validate_SMALLINT(columnName, (String) value);

		case Types.BIGINT:
			return validate_BIGINT(columnName, (String) value);

		case Types.REAL:
			return validate_REAL(columnName, (String) value);

		case Types.FLOAT:
			return validate_FLOAT(columnName, (String) value);

		case Types.DOUBLE:
			return validate_DOUBLE(columnName, (String) value);

		case Types.NUMERIC:
			return validate_NUMERIC(columnName, (String) value);

		case Types.DECIMAL:
			return validate_DECIMAL(columnName, (String) value);

		case Types.DATE:
			return validate_DATE(columnName, (String) value);

		case Types.TIME:
			return validate_TIME(columnName, (String) value);

		case Types.TIMESTAMP:
			return validate_TIMESTAMP(columnName, (String) value);

		case Types.BINARY: // -2
		case Types.VARBINARY: // -3
		case Types.LONGVARBINARY: // -4
		case Types.CLOB:
		case Types.BLOB:
		case Types.OTHER:
			return null;
		default:
			return null;

		}

	}

	protected String validate_CHAR(String columnName, String value) {
		return null;
	}

	protected String validate_VARCHAR(String columnName, String value) {
		return null;
	}

	protected String validate_LONGVARCHAR(String columnName, String value) {
		return null;
	}

	protected String validate_BIT(String columnName, String value) {
		return Validator.boolean_Check(columnName, value);
	}

	protected String validate_TINYINT(String columnName, String value) {
		return Validator.tinyint_Check(columnName, value);
	}

	protected String validate_INTEGER(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_SMALLINT(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_BIGINT(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_REAL(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_FLOAT(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_DOUBLE(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_NUMERIC(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_DECIMAL(String columnName, String value) {
		return Validator.decimal_Check(columnName, value);
	}

	protected String validate_DATE(String columnName, String value) {
		return Validator.date_Check(columnName, value);
	}

	protected String validate_TIME(String columnName, String value) {
		return Validator.time_Check(columnName, value);
	}

	protected String validate_TIMESTAMP(String columnName, String value) {
		return Validator.timestamp_Check(columnName, value);
	}

}
