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

public class MySQLColumnSearcharFactory extends DefaultColumnSearcherFactory {

	public MySQLColumnSearcharFactory(DatabaseMetaData meta, boolean convertUnicode) {
		super(meta, convertUnicode);
	}

	static Map typeMap = new TreeMap();

	static {
		typeMap.put("BIT", new Integer(Types.BIT));
		typeMap.put("TINYINT", new Integer(Types.TINYINT));
		typeMap.put("SMALLINT", new Integer(Types.SMALLINT));
		typeMap.put("MEDIUMINT", new Integer(Types.INTEGER));
		typeMap.put("INT", new Integer(Types.INTEGER));
		typeMap.put("INTEGER", new Integer(Types.INTEGER));
		typeMap.put("BIGINT", new Integer(Types.BIGINT));
		typeMap.put("INT24", new Integer(Types.INTEGER));
		typeMap.put("REAL", new Integer(Types.DOUBLE));
		typeMap.put("FLOAT", new Integer(Types.REAL));
		typeMap.put("DECIMAL", new Integer(Types.DECIMAL));
		typeMap.put("NUMERIC", new Integer(Types.DECIMAL));
		typeMap.put("DOUBLE", new Integer(Types.DOUBLE));
		typeMap.put("CHAR", new Integer(Types.CHAR));
		typeMap.put("VARCHAR", new Integer(Types.VARCHAR));
		typeMap.put("DATE", new Integer(Types.DATE));
		typeMap.put("TIME", new Integer(Types.TIME));
		typeMap.put("YEAR", new Integer(Types.DATE));
		typeMap.put("TIMESTAMP", new Integer(Types.TIMESTAMP));
		typeMap.put("DATETIME", new Integer(Types.TIMESTAMP));
		typeMap.put("TINYBLOB", new Integer(Types.BINARY));
		typeMap.put("BLOB", new Integer(Types.LONGVARBINARY));
		typeMap.put("MEDIUMBLOB", new Integer(Types.LONGVARBINARY));
		typeMap.put("LONGBLOB", new Integer(Types.LONGVARBINARY));
		typeMap.put("TINYTEXT", new Integer(Types.VARCHAR));
		typeMap.put("TEXT", new Integer(Types.LONGVARCHAR));
		typeMap.put("MEDIUMTEXT", new Integer(Types.LONGVARCHAR));
		typeMap.put("LONGTEXT", new Integer(Types.LONGVARCHAR));
		typeMap.put("ENUM", new Integer(Types.CHAR));
		typeMap.put("SET", new Integer(Types.CHAR));
		typeMap.put("GEOMETRY", new Integer(Types.BINARY));
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
