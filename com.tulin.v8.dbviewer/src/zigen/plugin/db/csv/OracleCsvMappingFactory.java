/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.csv;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class OracleCsvMappingFactory extends DefaultCsvMappingFactory implements ICsvMappingFactory {

	public static final int ORACLE_TIMESTAMP = -100;

	public OracleCsvMappingFactory(boolean convertUnicode, boolean nonDoubleQuate) {
		super(convertUnicode, nonDoubleQuate);
	}

	public String getCsvValue(ResultSet rs, int icol) throws SQLException {
		ResultSetMetaData rmd = rs.getMetaData();
		int type = rmd.getColumnType(icol);
		switch (type) {
		default:
			return super.getCsvValue(rs, icol);
		}
	}

	protected String getDate(ResultSet rs, int icol) throws SQLException {
		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull()) {
			return NULL;
		}

		// return timeStampFormat.format(new Date(value.getTime()));
		String temp = timeStampFormat.format(new Date(value.getTime()));

		if (!nonDoubleQuate) {
			return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return temp;
		}
	}

	protected String getTimestamp(ResultSet rs, int icol) throws SQLException {

		Timestamp value = rs.getTimestamp(icol);

		if (rs.wasNull()) {
			return NULL;
		}

		// return timeStampFormat2.format(new Date(value.getTime()));

		String temp = timeStampFormat2.format(new Date(value.getTime()));
		if (!nonDoubleQuate) {
			return "\"" + temp + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return temp;
		}


	}

}
