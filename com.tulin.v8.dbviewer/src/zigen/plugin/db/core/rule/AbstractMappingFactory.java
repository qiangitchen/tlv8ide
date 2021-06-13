/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.db2.DB2MappingFactory;
import zigen.plugin.db.core.rule.derby.DerbyMappingFactory;
import zigen.plugin.db.core.rule.mysql.MySQLMappingFactory;
import zigen.plugin.db.core.rule.oracle.OracleMappingFactory;
import zigen.plugin.db.core.rule.postgresql.PostgreSQLMappingFactory;
import zigen.plugin.db.core.rule.sqlite.SqliteMappingFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareMappingFactory;
import zigen.plugin.db.preference.PreferencePage;

public abstract class AbstractMappingFactory implements IMappingFactory {

	protected SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	protected SimpleDateFormat timeStampFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	protected boolean convertUnicode;

	protected String nullSymbol;

	public static IMappingFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName(), config.isConvertUnicode());
	}


	public static IMappingFactory getFactory(DatabaseMetaData objMet, boolean isConvertUnicode) {
		try {
			return getFactory(objMet.getDriverName(), isConvertUnicode);

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}

	}

	private static Map map = new HashMap();

	public static IMappingFactory getFactory(String driverName, boolean isConvertUnicode) {
		IMappingFactory factory = null;

		String key = driverName + ":" + isConvertUnicode;

		if (map.containsKey(key)) {
			factory = (IMappingFactory) map.get(key);
			factory.setConvertUnicode(isConvertUnicode);
		} else {
			switch (DBType.getType(driverName)) {

				case DBType.DB_TYPE_ORACLE:
					factory = new OracleMappingFactory(isConvertUnicode);
					break;
				case DBType.DB_TYPE_DB2:
					factory = new DB2MappingFactory(isConvertUnicode);
					break;
				case DBType.DB_TYPE_SYMFOWARE:
					factory = new SymfowareMappingFactory(isConvertUnicode);
					break;
				case DBType.DB_TYPE_MYSQL:
					factory = new MySQLMappingFactory(isConvertUnicode);
					break;
				case DBType.DB_TYPE_POSTGRESQL:
					factory = new PostgreSQLMappingFactory(isConvertUnicode);
					break;
				case DBType.DB_TYPE_DERBY:
					factory = new DerbyMappingFactory(isConvertUnicode);
					break;
				case DBType.DB_TYPE_SQLITE:
					factory = new SqliteMappingFactory(isConvertUnicode);
					break;
				default:
					factory = new DefaultMappingFactory(isConvertUnicode);
					break;
			}

			map.put(key, factory);
		}

		factory.setNullSymbol(DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL));
		return factory;

	}

	abstract public boolean canModifyDataType(int dataType);

	abstract public Object getObject(ResultSet rs, int icol) throws Exception;

	abstract public void setObject(PreparedStatement pst, int icol, TableColumn column, Object value) throws Exception;

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
			if (str.indexOf("/") > 0) {
				df = new SimpleDateFormat("yyyy/MM/dd");
			} else {
				df = new SimpleDateFormat("yyyy-MM-dd");
			}
		} else {
			if (str.indexOf("/") > 0) {
				df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			} else {
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
		}
		date = df.parse(str);
		return new Timestamp(date.getTime());

	}

	protected Timestamp toTimestamp2(String str) throws Exception {
		DateFormat df = null;
		Date date = null;

		if (str.indexOf("/") > 0) {
			df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		} else {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}


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
			String prefix = "";
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

	public String getNullSymbol() {
		return nullSymbol;
	}

	public void setNullSymbol(String nullSymbol) {
		this.nullSymbol = nullSymbol;
	}

	public void setConvertUnicode(boolean convertUnicode) {
		this.convertUnicode = convertUnicode;
	}

}
