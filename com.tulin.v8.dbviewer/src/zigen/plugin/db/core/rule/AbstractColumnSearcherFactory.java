/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.rule.mysql.MySQLColumnSearcharFactory;
import zigen.plugin.db.core.rule.oracle.OracleColumnSearcharFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareColumnSearcharFactory;

public abstract class AbstractColumnSearcherFactory implements IColumnSearcherFactory {

	public static final String COLUMN_NAME_STR = "COLUMN_NAME";

	public static final String DATA_TYPE_STR = "DATA_TYPE";

	public static final String TYPE_NAME_STR = "TYPE_NAME";

	public static final String DATA_PRECISION_STR = "DATA_PRECISION";

	public static final String DATA_SCALE_STR = "DATA_SCALE";

	public static final String DATA_DEFAULT_STR = "DATA_DEFAULT";

	public static final String NULLABLE_STR = "NULLABLE";

	public static final String COMMENTS_STR = "COMMENTS";

	private static Map map = new HashMap();


	public static IColumnSearcherFactory getFactory(DatabaseMetaData objMet, boolean isConvertUnicode) {
		IColumnSearcherFactory factory = null;

		String driverName = "";
		try {
			driverName = objMet.getDriverName();
		} catch (SQLException e) {
			e.printStackTrace();
			return new DefaultColumnSearcherFactory(objMet, isConvertUnicode);
		}


		String key = driverName + ":" + isConvertUnicode;
		if (map.containsKey(key)) {
			factory = (IColumnSearcherFactory) map.get(key);
			factory.setConvertUnicode(isConvertUnicode);
			factory.setDatabaseMetaData(objMet);

		} else {
			switch (DBType.getType(driverName)) {
				case DBType.DB_TYPE_ORACLE:
					factory = new OracleColumnSearcharFactory(objMet,isConvertUnicode);
					break;
				case DBType.DB_TYPE_MYSQL:
					factory = new MySQLColumnSearcharFactory(objMet, isConvertUnicode);
					break;
				case DBType.DB_TYPE_SYMFOWARE:
					factory = new SymfowareColumnSearcharFactory(objMet, isConvertUnicode);
					break;
				default:
					factory = new DefaultColumnSearcherFactory(objMet, isConvertUnicode);
					break;
			}

			map.put(key, factory);
		}

		return factory;

	}

	abstract protected String getCustomColumnInfoSQL(String dbName, String owner, String table);

	protected Map getCustomColumnInfoMap(Connection con, String owner, String table, boolean convertUnicode) throws Exception {
		Map map = new HashMap();
		ResultSet rs = null;
		Statement st = null;
		try {
			ITableInfoSearchFactory factory = AbstractTableInfoSearchFactory.getFactory(con.getMetaData());
			String dbName = factory.getDbName();
			String sql = getCustomColumnInfoSQL(dbName, owner, table);
			if (sql == null)
				return map;
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ColumnInfo info = new ColumnInfo();
				info.setColumn_name(rs.getString(COLUMN_NAME_STR));
				info.setData_type(rs.getString(DATA_TYPE_STR));
				info.setData_precision(rs.getBigDecimal(DATA_PRECISION_STR));
				info.setData_scale(rs.getBigDecimal(DATA_SCALE_STR));
				info.setData_default(rs.getString(DATA_DEFAULT_STR));
				info.setComments(rs.getString(COMMENTS_STR));


				if (convertUnicode) {
					info.setColumn_name(JDBCUnicodeConvertor.convert(info.getColumn_name()));
					info.setData_type(JDBCUnicodeConvertor.convert(info.getData_type()));
					info.setData_default(JDBCUnicodeConvertor.convert(info.getData_default()));
					info.setComments(JDBCUnicodeConvertor.convert(info.getComments()));
				}
				map.put(info.getColumn_name(), info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return map;
	}


}
