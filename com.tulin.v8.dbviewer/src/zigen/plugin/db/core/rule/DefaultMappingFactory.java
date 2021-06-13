/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.TableColumn;

public class DefaultMappingFactory extends AbstractMappingFactory implements IMappingFactory {

	protected DefaultMappingFactory(boolean convertUnicode) {
		this.convertUnicode = convertUnicode;
	}

	public Object getObject(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();

		Object obj = null;
		int type = rmd.getColumnType(icol);

		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR: // -1
			obj = getString(rs, icol);
			break;

		case Types.BIT: // boolean
		case Types.BOOLEAN:
			obj = getBoolean(rs, icol);
			break;

		case Types.TINYINT:
		case Types.INTEGER: // int
		case Types.SMALLINT: // short
		case Types.BIGINT: // long
			obj = getLong(rs, icol);
			break;

		case Types.REAL: // float
		case Types.FLOAT: // double
		case Types.DOUBLE: // double
			obj = getDouble(rs, icol);
			break;

		case Types.NUMERIC: // BigDecimal
		case Types.DECIMAL:// BigDecimal
			obj = getBigDecimal(rs, icol);

			break;

		case Types.DATE:
			obj = getDate(rs, icol);
			break;

		case Types.TIME:
			obj = getTime(rs, icol);
			break;

		case Types.TIMESTAMP:
			obj = getTimestamp(rs, icol);
			break;

		case Types.BINARY: // -2
		case Types.VARBINARY: // -3
		case Types.LONGVARBINARY: // -4
			obj = getBinary(rs, icol);
			break;

		case Types.CLOB:
			obj = getClob(rs, icol);
			break;
		case Types.BLOB:
			obj = getBlob(rs, icol);
			break;

		case Types.OTHER:
			obj = getOther(rs, icol);
			break;

		default:
//			obj = "<<Unknown Type (" + type + ")>>";
			obj = getUnknown(rs, icol, type);
			break;
		}
		return obj;
	}

	// true
	protected boolean canModify_CHAR() {
		return true;
	}

	protected boolean canModify_VARCHAR() {
		return true;
	}

	protected boolean canModify_LONGVARCHAR() {
		return true;
	}

	protected boolean canModify_BIT() {
		return true;
	}

	protected boolean canModify_BOOLEAN() {
		return true;
	}

	protected boolean canModify_TINYINT() {
		return true;
	}

	protected boolean canModify_SMALLINT() {
		return true;
	}

	protected boolean canModify_INTEGER() {
		return true;
	}

	protected boolean canModify_BIGINT() {
		return true;
	}

	protected boolean canModify_REAL() {
		return true;
	}

	protected boolean canModify_FLOAT() {
		return true;
	}

	protected boolean canModify_DOUBLE() {
		return true;
	}

	protected boolean canModify_NUMERIC() {
		return true;
	}

	protected boolean canModify_DECIMAL() {
		return true;
	}

	protected boolean canModify_DATE() {
		return true;
	}

	protected boolean canModify_TIME() {
		return true;
	}

	protected boolean canModify_TIMESTAMP() {
		return true;
	}

	protected boolean canModify_BINARY() {
		// return false;
		return true;
	}

	protected boolean canModify_VARBINARY() {
		// return false;
		return true;
	}

	protected boolean canModify_LONGVARBINARY() {
		// return false;
		return true;
	}

	protected boolean canModify_CLOB() {
		// return false;
		return true;
	}

	protected boolean canModify_BLOB() {
		// return false;
		return true;
	}

	protected boolean canModify_OTHER() {
		return false;
	}

	public boolean canModifyDataType(int dataType) {
		switch (dataType) {
		case Types.CHAR:
			return canModify_CHAR();
		case Types.VARCHAR:
			return canModify_VARCHAR();
		case Types.LONGVARCHAR: // -1
			return canModify_LONGVARCHAR();
		case Types.BIT:
			return canModify_BIT();
		case Types.BOOLEAN:
			return canModify_BOOLEAN();
		case Types.TINYINT:
			return canModify_TINYINT();
		case Types.SMALLINT:
			return canModify_SMALLINT();
		case Types.INTEGER:
			return canModify_INTEGER();
		case Types.BIGINT:
			return canModify_BIGINT();
		case Types.REAL:
			return canModify_REAL();
		case Types.FLOAT:
			return canModify_FLOAT();
		case Types.DOUBLE:
			return canModify_DOUBLE();
		case Types.NUMERIC:
			return canModify_NUMERIC();
		case Types.DECIMAL:
			return canModify_DECIMAL();
		case Types.DATE:
			return canModify_DATE();
		case Types.TIME:
			return canModify_TIME();
		case Types.TIMESTAMP:
			return canModify_TIMESTAMP();

		case Types.BINARY: // -2
			return canModify_BINARY();

		case Types.VARBINARY: // -3
			return canModify_VARBINARY();

		case Types.LONGVARBINARY: // -4
			return canModify_LONGVARBINARY();

		case Types.CLOB:
			return canModify_CLOB();
		case Types.BLOB:
			return canModify_BLOB();

		case Types.OTHER:
			return canModify_OTHER();

		default:
			return false;
		}

	}

	protected String getString(ResultSet rs, int icol) throws SQLException {
		String value = rs.getString(icol);

		if (rs.wasNull())
			return nullSymbol;

		if (convertUnicode) {
			return JDBCUnicodeConvertor.convert(value);
		} else {
			return value;
		}
	}

	protected String getBoolean(ResultSet rs, int icol) throws SQLException {
		boolean value = rs.getBoolean(icol);
		if (rs.wasNull())
			return nullSymbol;

		return String.valueOf(value);
	}

	protected String getBigDecimal(ResultSet rs, int icol) throws SQLException {
		BigDecimal value = rs.getBigDecimal(icol);
		if (rs.wasNull())
			return nullSymbol;
		// return toStringForDisplay(value); // 1.0 to 1
		return value.toString();
	}

	protected String getLong(ResultSet rs, int icol) throws SQLException {
		try {
			long value = rs.getLong(icol);

			if (rs.wasNull())
				return nullSymbol;

			return String.valueOf(value);
		} catch (SQLException e) {

			BigDecimal value = rs.getBigDecimal(icol);
			if (rs.wasNull())
				return nullSymbol;
			return String.valueOf(value);
		}
	}

	protected String getDouble(ResultSet rs, int icol) throws SQLException {
		double value = rs.getDouble(icol);
		if (rs.wasNull())
			return nullSymbol;

		// return String.valueOf(value);
		return toStringForDisplay(value); // 1.0 to 1
	}

	protected String getDate(ResultSet rs, int icol) throws SQLException {
		Date value = rs.getDate(icol);

		if (rs.wasNull())
			return nullSymbol;

		return dateFormat.format(value);

	}

	protected String getTime(ResultSet rs, int icol) throws SQLException {
		Time value = rs.getTime(icol);
		if (rs.wasNull())
			return nullSymbol;

		return timeFormat.format(value);

	}

	protected String getTimestamp(ResultSet rs, int icol) throws SQLException {
		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull())
			return nullSymbol;

		return timeStampFormat.format(new Date(value.getTime()));

	}

	protected String getBinary(ResultSet rs, int icol) throws SQLException {
		String obj = null;
		try {

			InputStream is = rs.getBinaryStream(icol);

			if (rs.wasNull())
				return nullSymbol;

			obj = "<<Binary>>";

		} catch (Exception e) {
			DbPlugin.log(e);
			throw new SQLException(e.getMessage());
		}
		return obj;

	}

	protected Object getClob(ResultSet rs, int icol) throws SQLException {
		Object value = rs.getObject(icol);

		if (rs.wasNull())
			return nullSymbol;

		return "<<CLOB>>";
	}

	protected Object getBlob(ResultSet rs, int icol) throws SQLException {
		Object value = rs.getObject(icol);

		if (rs.wasNull())
			return nullSymbol;
		return "<<BLOB>>";
	}

	protected String getOther(ResultSet rs, int icol) throws SQLException {
		Object value = rs.getObject(icol);

		if (rs.wasNull())
			return nullSymbol;
		return "<<OTHER>>";
	}

	protected String getUnknown(ResultSet rs, int icol, int type) throws SQLException {
		Object value = rs.getObject(icol);

		if (rs.wasNull())
			return nullSymbol;

		return "<<Unknown Type (" + type + ")>>";

	}


	protected void setChar(PreparedStatement pst, int icol, String str) throws SQLException {

		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.CHAR);
		} else {
			pst.setString(icol, str);
		}
	}

	protected void setVarchar(PreparedStatement pst, int icol, String str) throws SQLException {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.VARCHAR);
		} else {
			pst.setString(icol, str);
		}
	}

	protected void setLonvarchar(PreparedStatement pst, int icol, String str) throws SQLException {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.LONGVARCHAR);
		} else {
			StringReader reader = new StringReader(str);
			pst.setCharacterStream(icol, reader, str.length());
		}
	}

	protected void setBigDecimal(PreparedStatement pst, int icol, String str) throws SQLException {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.NUMERIC);
		} else {
			pst.setBigDecimal(icol, new BigDecimal(str));

		}
	}

	protected void setDate(PreparedStatement pst, int icol, String str) throws Exception {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.DATE);
		} else {
			pst.setDate(icol, toDate(str));

		}

	}

	protected void setTime(PreparedStatement pst, int icol, String str) throws Exception {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.TIME);
		} else {
			pst.setTime(icol, toTime(str));

		}

	}

	protected void setTimestamp(PreparedStatement pst, int icol, String str) throws Exception {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.TIMESTAMP);
		} else {
			pst.setTimestamp(icol, toTimestamp(str));

		}

	}

	protected void setBoolean(PreparedStatement pst, int icol, String str) throws Exception {
		if (nullSymbol.equals(str)) {
			pst.setNull(icol, Types.BOOLEAN);
		} else {
			if (str.toLowerCase().equals("true") || str.equals("1")) {
				pst.setBoolean(icol, true);
			} else {
				pst.setBoolean(icol, false);
			}

		}

	}

	protected void setBinary(PreparedStatement pst, int icol, Object value) throws Exception {
		if (value == null) {
			pst.setNull(icol, Types.BLOB);
			return;

		} else {
			int size = 0;
			try {
				if (value instanceof File) {
					File file = (File) value;
					size = (int) file.length();
					pst.setBinaryStream(icol, new FileInputStream(file), (int) size);

				} else if (value instanceof byte[]) {
					byte[] bytes = (byte[]) value;
					size = bytes.length;
					pst.setBinaryStream(icol, new ByteArrayInputStream(bytes), (int) size);

				} else if (value instanceof String) {
					String str = (String) value;
					if (nullSymbol.equals(str)) {
						pst.setNull(icol, Types.BLOB);
						return;
					} else {
						byte[] bytes = str.getBytes();
						size = bytes.length;
						pst.setBinaryStream(icol, new ByteArrayInputStream(bytes), (int) size);
					}
				}

			} catch (Exception e) {
				DbPlugin.log(e);

			}

		}
	}

	protected void setBlob(PreparedStatement pst, int icol, Object value) throws SQLException {
		if (value == null) {
			pst.setNull(icol, Types.BLOB);
			return;

		} else {
			int size = 0;
			try {
				if (value instanceof File) {
					File file = (File) value;
					size = (int) file.length();
					pst.setBinaryStream(icol, new FileInputStream(file), (int) size);

				} else if (value instanceof byte[]) {
					byte[] bytes = (byte[]) value;
					size = bytes.length;
					pst.setBinaryStream(icol, new ByteArrayInputStream(bytes), (int) size);

				} else if (value instanceof String) {
					String str = (String) value;
					if (nullSymbol.equals(str)) {
						pst.setNull(icol, Types.BLOB);
						return;
					} else {
						byte[] bytes = str.getBytes();
						size = bytes.length;
						pst.setBinaryStream(icol, new ByteArrayInputStream(bytes), (int) size);
					}
				}

			} catch (Exception e) {
				DbPlugin.log(e);

			}

		}
	}

	protected void setClob(PreparedStatement pst, int icol, Object value) throws SQLException {
		if (value == null) {
			pst.setNull(icol, Types.CLOB);
			return;
		} else {
			int size = 0;
			try {
				if (value instanceof File) {
					File file = (File) value;
					size = (int) file.length();
					pst.setCharacterStream(icol, new FileReader(file), (int) size);
				} else if (value instanceof char[]) {
					char[] chars = (char[]) value;
					size = chars.length;
					pst.setCharacterStream(icol, new CharArrayReader(chars), (int) size);
				} else if (value instanceof String) {
					String str = (String) value;
					if (nullSymbol.equals(str)) {
						pst.setNull(icol, Types.CLOB);
						return;
					} else {
						size = str.getBytes().length;
						pst.setCharacterStream(icol, new StringReader(str), (int) size);
					}
				}

			} catch (Exception e) {
				DbPlugin.log(e);

			}

		}
	}

	public void setObject(PreparedStatement pst, int icol, TableColumn column, Object value) throws Exception {

		try {
			int type = column.getDataType();

			String str = String.valueOf(value);

			switch (type) {
			case Types.CHAR:
				setChar(pst, icol, str);
				break;
			case Types.VARCHAR:
				setVarchar(pst, icol, str);
				break;

			case Types.LONGVARCHAR: // -1
				setLonvarchar(pst, icol, str);
				break;

			case Types.BIT:
			case Types.BOOLEAN:
				setBoolean(pst, icol, str);
				break;

			case Types.TINYINT:
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.BIGINT:
			case Types.REAL:
			case Types.FLOAT:
			case Types.DOUBLE:
			case Types.NUMERIC:
			case Types.DECIMAL:
				setBigDecimal(pst, icol, str);
				break;

			case Types.DATE:
				setDate(pst, icol, str);
				break;
			case Types.TIME:
				setTime(pst, icol, str);
				break;

			case Types.TIMESTAMP:
				setTimestamp(pst, icol, str);
				break;

			case Types.BINARY: // -2
			case Types.VARBINARY: // -3
			case Types.LONGVARBINARY: // -4
				setBinary(pst, icol, value);
				// setBlob(pst, icol, value);
				break;
			case Types.BLOB:
				setBlob(pst, icol, value);
				break;
			case Types.CLOB:
				setClob(pst, icol, value);
				break;
			default:
				throw new UnSupportedTypeException(column, value);
			}

		} catch (SQLException e) {
			DbPlugin.log(e);
			throw new Exception(e);
		}

	}

	protected final String toStringForDisplay(double num) {
		if (Math.ceil(num) == Math.floor(num)) {
			return String.valueOf((int) num);
		} else {
			return String.valueOf(num);
		}
	}

	protected final String toStringForDisplay(BigDecimal num) {
		BigDecimal a = num.setScale(0, BigDecimal.ROUND_CEILING);
		BigDecimal b = num.setScale(0, BigDecimal.ROUND_FLOOR);
		if (a.equals(b)) {
			return a.toString();
		} else {
			return num.toString();
		}
	}

}
