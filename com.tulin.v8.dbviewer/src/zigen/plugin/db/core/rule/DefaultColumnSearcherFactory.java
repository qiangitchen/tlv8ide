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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableColumn;


public class DefaultColumnSearcherFactory extends AbstractColumnSearcherFactory implements IColumnSearcherFactory {

	public DefaultColumnSearcherFactory(DatabaseMetaData meta, boolean convertUnicode){
		this.objMeta = meta;
		this.convertUnicode = convertUnicode;
	}

	public TableColumn[] execute(Connection con, String schemaPattern, String tableName) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;

		try {
			Map map = getCustomColumnInfoMap(con, schemaPattern, tableName, convertUnicode);

			DatabaseMetaData objMet = con.getMetaData();
			// TablePKColumn[] pks = ConstraintSearcher.getPKColumns(con, schemaPattern, tableName);
			if (schemaPattern != null) {
				rs = objMet.getColumns(null, schemaPattern, tableName, "%"); //$NON-NLS-1$
			} else {
				rs = objMet.getColumns(null, "%", tableName, "%"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			int seq = 1;
			while (rs.next()) {
				TableColumn column = new TableColumn();

				column.setSeq(seq);

				column.setColumnName(rs.getString(COLUMN_NAME));
				column.setDataType(rs.getShort(DATA_TYPE)); // Types.VARCHAR
				column.setTypeName(rs.getString(TYPE_NAME));
				column.setColumnSize(rs.getInt(COLUMN_SIZE));
				column.setDecimalDigits(rs.getInt(DECIMAL_DIGITS));

				String defaultValue = getDefaultValue(rs, convertUnicode);
				if (defaultValue != null) {
					column.setDefaultValue(defaultValue);
				}

				// column.setRemarks(rs.getString(REMARKS));
				String remarks = rs.getString("REMARKS"); //$NON-NLS-1$
				if (convertUnicode) {
					remarks = JDBCUnicodeConvertor.convert(remarks);
				}
				column.setRemarks(remarks);

				if (rs.getInt(NULLABLE) == DatabaseMetaData.columnNoNulls) {
					column.setNotNull(true);
				} else {
					column.setNotNull(false);
				}

				overrideColumnInfo(map, column);

				list.add(column);

				seq++;
			}

			return (TableColumn[]) list.toArray(new TableColumn[0]);

		} catch (Exception e) {
			throw e;
		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}
	}

	protected void overrideColumnInfo(Map map, TableColumn column) throws Exception {
		;
	}

	protected String getDefaultValue(ResultSet rs, boolean convertUnicode) {
		String defaultValue = null;
		try {
			defaultValue = rs.getString("COLUMN_DEF"); //$NON-NLS-1$

			if (defaultValue != null)
				defaultValue = defaultValue.trim();

			if (convertUnicode) {
				return JDBCUnicodeConvertor.convert(defaultValue);
			}
		} catch (SQLException e) {
			// DbPlugin.log(e);
		}
		return defaultValue;

	}

	protected boolean convertUnicode;
	protected DatabaseMetaData objMeta;

	public void setConvertUnicode(boolean convertUnicode) {
		this.convertUnicode = convertUnicode;
	}

	public void setDatabaseMetaData(DatabaseMetaData meta){
		this.objMeta = meta;
	}

	protected String getCustomColumnInfoSQL(String dbName, String owner, String table) {
		return null;
	}

	public int getDatabaseMajorVersion() {
		int version = 0;
		try {
			version = objMeta.getDatabaseMajorVersion();
		} catch (SQLException e) {
			System.err.println("DefaultColumnSearcherFactory#getDatabaseMajorVersion is Error > " + e.getMessage());
		}
		return version;
	}

}
