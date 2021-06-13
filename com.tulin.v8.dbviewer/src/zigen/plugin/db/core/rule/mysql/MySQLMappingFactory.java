/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.mysql;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.DefaultMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;

public class MySQLMappingFactory extends DefaultMappingFactory implements IMappingFactory {

	public MySQLMappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

	protected String getTimestamp(ResultSet rs, int icol) throws SQLException {
		// http://dev.mysql.com/doc/refman/5.0/en/connector-j-installing-upgrading.html
		try {
			Timestamp value = rs.getTimestamp(icol);

			if (rs.wasNull())
				return nullSymbol;

			return timeStampFormat.format(value);
		} catch (SQLException e) {
			if ("S1009".equals(e.getSQLState())) {
				return nullSymbol;
			}

			throw e;
		}


	}

	protected String getDate(ResultSet rs, int icol) throws SQLException {
		// http://dev.mysql.com/doc/refman/5.0/en/connector-j-installing-upgrading.html
		try {
			Date value = rs.getDate(icol);

			if (rs.wasNull())
				return nullSymbol;

			return dateFormat.format(value);
		} catch (SQLException e) {
			if ("S1009".equals(e.getSQLState())) {
				return nullSymbol;
			}

			throw e;
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


}
