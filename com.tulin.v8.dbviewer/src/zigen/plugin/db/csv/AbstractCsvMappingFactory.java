/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.preference.PreferencePage;

public abstract class AbstractCsvMappingFactory implements ICsvMappingFactory {

	protected SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$

	protected SimpleDateFormat timeStampFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); //$NON-NLS-1$

	protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$

	protected boolean convertUnicode;


	protected static final String NULL = "";//$NON-NLS-1$

	protected String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);


	protected boolean nonDoubleQuate;

	public static ICsvMappingFactory getFactory(IDBConfig config, boolean nonDoubleQuate) {
		return getFactory(config.getDriverName(), config.isConvertUnicode(), nonDoubleQuate);
	}


	public static ICsvMappingFactory getFactory(DatabaseMetaData objMet, boolean isConvertUnicode, boolean nonDoubleQuate) {
		try {
			return getFactory(objMet.getDriverName(), isConvertUnicode, nonDoubleQuate);

		} catch (SQLException e) {
			throw new IllegalStateException("Failed get DriverName"); //$NON-NLS-1$
		}

	}

	public static ICsvMappingFactory getFactory(String driverName, boolean isConvertUnicode, boolean nonDoubleQuate) {
		switch (DBType.getType(driverName)) {

		case DBType.DB_TYPE_ORACLE:
			return new OracleCsvMappingFactory(isConvertUnicode, nonDoubleQuate);
		default:
			return new DefaultCsvMappingFactory(isConvertUnicode, nonDoubleQuate);

		}

	}

	protected String convertLineSep(String value) {
		if (value != null && value.length() > 0) {

			if (value.indexOf("\r\n") != -1) { //$NON-NLS-1$
				value = value.replaceAll("\r\n", "\\\\n"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (value.indexOf("\n") != -1) { //$NON-NLS-1$
				value = value.replaceAll("\n", "\\\\n"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (value.indexOf("\r") != -1) { //$NON-NLS-1$
				value = value.replaceAll("\r", "\\\\n"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return value;
	}

	abstract public String getCsvValue(ResultSet rs, int icol) throws Exception;

	protected java.sql.Date toDate(String s) throws Exception {
		try {
			return java.sql.Date.valueOf(s);
		} catch (Exception e) {
			throw e;
		}
	}

	protected java.sql.Time toTime(String s) throws Exception {
		try {
			return java.sql.Time.valueOf(s);
		} catch (Exception e) {
			throw e;
		}
	}

	protected Timestamp toTimestamp(String str) throws Exception {
		DateFormat df = null;
		Date date = null;

		if (str.length() <= 10) {
			df = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		} else {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		}
		date = df.parse(str);
		return new Timestamp(date.getTime());

	}

	protected Timestamp toTimestamp2(String str) throws Exception {
		DateFormat df = null;
		Date date = null;

		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); //$NON-NLS-1$
		date = df.parse(str);
		return new Timestamp(date.getTime());

	}

	protected byte[] toBytes(Object obj) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		return bos.toByteArray();
	}

	protected String toBinary(byte[] bytes) {
		if (bytes == null)
			return null;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			String hx = Integer.toHexString(bytes[i] & 0xff);
			String prefix = ""; //$NON-NLS-1$
			for (int j = hx.length(); j < 2; j++) {
				prefix += '0';
			}
			sb.append(prefix);
			sb.append(hx);
		}
		return sb.toString().toUpperCase();
	}

	protected byte[] toByteArray(InputStream is) {
		ByteArrayOutputStream baos = null;
		byte[] buf = new byte[1024];
		int count = 0;

		try {
			baos = new ByteArrayOutputStream();
			while ((count = is.read(buf)) != -1) {
				if (count > 0)
					baos.write(buf, 0, count);
			}
			return baos.toByteArray();

		} catch (IOException e) {
			DbPlugin.log(e);

		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

}
