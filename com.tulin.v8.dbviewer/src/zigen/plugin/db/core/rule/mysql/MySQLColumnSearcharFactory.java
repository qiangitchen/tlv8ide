package zigen.plugin.db.core.rule.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;

@SuppressWarnings({"rawtypes","unchecked"})
public class MySQLColumnSearcharFactory extends DefaultColumnSearcherFactory {

	public MySQLColumnSearcharFactory(DatabaseMetaData meta, boolean convertUnicode) {
		super(meta, convertUnicode);
	}

	static Map typeMap = new TreeMap();

	static {
		typeMap.put("BIT", Types.BIT);
		typeMap.put("TINYINT", Types.TINYINT);
		typeMap.put("SMALLINT", Types.SMALLINT);
		typeMap.put("MEDIUMINT", Types.INTEGER);
		typeMap.put("INT", Types.INTEGER);
		typeMap.put("INTEGER", Types.INTEGER);
		typeMap.put("BIGINT", Types.BIGINT);
		typeMap.put("INT24", Types.INTEGER);
		typeMap.put("REAL", Types.DOUBLE);
		typeMap.put("FLOAT", Types.REAL);
		typeMap.put("DECIMAL", Types.DECIMAL);
		typeMap.put("NUMERIC", Types.DECIMAL);
		typeMap.put("DOUBLE", Types.DOUBLE);
		typeMap.put("CHAR", Types.CHAR);
		typeMap.put("VARCHAR", Types.VARCHAR);
		typeMap.put("DATE", Types.DATE);
		typeMap.put("TIME", Types.TIME);
		typeMap.put("YEAR", Types.DATE);
		typeMap.put("TIMESTAMP", Types.TIMESTAMP);
		typeMap.put("DATETIME", Types.TIMESTAMP);
		typeMap.put("TINYBLOB", Types.BINARY);
		typeMap.put("BLOB", Types.LONGVARBINARY);
		typeMap.put("MEDIUMBLOB", Types.LONGVARBINARY);
		typeMap.put("LONGBLOB", Types.LONGVARBINARY);
		typeMap.put("TINYTEXT", Types.VARCHAR);
		typeMap.put("TEXT", Types.LONGVARCHAR);
		typeMap.put("MEDIUMTEXT", Types.LONGVARCHAR);
		typeMap.put("LONGTEXT", Types.LONGVARCHAR);
		typeMap.put("ENUM", Types.CHAR);
		typeMap.put("SET", Types.CHAR);
		typeMap.put("GEOMETRY", Types.BINARY);
	}

	private int getJavaType(String typeName) {
		String key = typeName.toUpperCase();
		if (typeMap.containsKey(key)) {
			Integer i = (Integer) typeMap.get(key);
			return i.intValue();
		} else {
			return java.sql.Types.OTHER;
		}
	}

	public TableColumn[] execute(Connection con, String schemaPattern, String tableName) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;

		try {
			DatabaseMetaData objMet = con.getMetaData();

			if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {

				st = con.createStatement();
				rs = st.executeQuery(getColumnsSQL(schemaPattern, tableName));
				int seq = 1;
				while (rs.next()) {
					TableColumn column = new TableColumn();

					column.setSeq(seq);

					column.setColumnName(rs.getString("COLUMN_NAME"));
					column.setTypeName(rs.getString("TYPE_NAME"));
					column.setColumnSize(rs.getInt("COLUMN_SIZE"));
					column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
					column.setDefaultValue(rs.getString("COLUMN_DEF"));

					column.setDataType(getJavaType(column.getTypeName()));

					String remarks = rs.getString("REMARKS"); //$NON-NLS-1$
					if (convertUnicode) {
						remarks = JDBCUnicodeConvertor.convert(remarks);
					}
					column.setRemarks(remarks);
					if (rs.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls) {
						column.setNotNull(true);
					} else {
						column.setNotNull(false);
					}

					list.add(column);

					seq++;
				}
				return (TableColumn[]) list.toArray(new TableColumn[0]);
			} else {
				return super.execute(con, schemaPattern, tableName);
			}

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}

	}

	private String getColumnsSQL(String schemaPattern, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        COLUMN_NAME");
		sb.append("        ,0 DATA_TYPE"); // -- dummy
		sb.append("        ,CASE");
		sb.append("            WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned')");
		sb.append("            ELSE DATA_TYPE");
		sb.append("        END AS TYPE_NAME");
		sb.append("        ,CASE");
		sb.append("            WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION");
		sb.append("            ELSE CASE");
		sb.append("                WHEN CHARACTER_MAXIMUM_LENGTH > " + Integer.MAX_VALUE + " THEN " + Integer.MAX_VALUE);
		sb.append("                ELSE CHARACTER_MAXIMUM_LENGTH");
		sb.append("            END");
		sb.append("        END AS COLUMN_SIZE");
		sb.append("        ,NUMERIC_SCALE AS DECIMAL_DIGITS");
		sb.append("        ,COLUMN_COMMENT AS REMARKS");
		sb.append("        ,CASE");
		sb.append("            WHEN IS_NULLABLE = 'NO' THEN 0");
		sb.append("            ELSE 1");
		sb.append("        END NULLABLE");
		sb.append("        ,COLUMN_DEFAULT AS COLUMN_DEF");
		sb.append("    FROM");
		sb.append("        information_schema.COLUMNS");
		sb.append("    WHERE");
		sb.append("        TABLE_SCHEMA = '" + SQLUtil.encodeQuotation(schemaPattern) + "'");
		sb.append("        AND TABLE_NAME = '" + SQLUtil.encodeQuotation(tableName) + "'");
		sb.append("    ORDER BY");
		sb.append("        TABLE_SCHEMA");
		sb.append("        ,TABLE_NAME");
		sb.append("        ,ORDINAL_POSITION");
		return sb.toString();

	}


}
