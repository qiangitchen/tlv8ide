package zigen.plugin.db.core.rule.symfoware;

import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.Map;
import java.util.TreeMap;

import zigen.plugin.db.core.JDBCUnicodeConvertor;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.AbstractTableInfoSearchFactory;
import zigen.plugin.db.core.rule.ColumnInfo;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;
import zigen.plugin.db.core.rule.ITableInfoSearchFactory;

public class SymfowareColumnSearcharFactory extends DefaultColumnSearcherFactory {
	public SymfowareColumnSearcharFactory(DatabaseMetaData meta, boolean convertUnicode) {
		super(meta, convertUnicode);
	}

	static Map typeMap = new TreeMap();
	static {
		typeMap.put("CH", new Integer(Types.CHAR));
		typeMap.put("CV", new Integer(Types.VARCHAR));
		typeMap.put("BL", new Integer(Types.BLOB));
		typeMap.put("CN", new Integer(Types.CHAR));
		typeMap.put("NV", new Integer(Types.CHAR));
		typeMap.put("IN", new Integer(Types.INTEGER));
		typeMap.put("SI", new Integer(Types.SMALLINT));
		typeMap.put("NU", new Integer(Types.DECIMAL));
		typeMap.put("DE", new Integer(Types.DECIMAL));
		typeMap.put("FL", new Integer(Types.FLOAT));
		typeMap.put("DP", new Integer(Types.DOUBLE));
		typeMap.put("RE", new Integer(Types.REAL));
		typeMap.put("TM", new Integer(Types.TIMESTAMP));
		typeMap.put("DT", new Integer(Types.DATE));
		typeMap.put("TI", new Integer(Types.TIME));
		typeMap.put("IT", new Integer(Types.OTHER));
	}

	static Map typeNameMap = new TreeMap();
	static {
		typeNameMap.put("CH", "char");
		typeNameMap.put("CV", "varchar");
		typeNameMap.put("BL", "blob");
		typeNameMap.put("CN", "nchar");
		typeNameMap.put("NV", "nchar varying");
		typeNameMap.put("IN", "int");
		typeNameMap.put("SI", "smallint");
		typeNameMap.put("NU", "numeric");
		typeNameMap.put("DE", "decimal");
		typeNameMap.put("FL", "float");
		typeNameMap.put("DP", "double precision");
		typeNameMap.put("RE", "real");
		typeNameMap.put("TM", "timestamp");
		typeNameMap.put("DT", "date");
		typeNameMap.put("TI", "time");
		typeNameMap.put("IT", "interval");
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

	private String getTypeName(String typeName) {
		String key = typeName.toUpperCase();
		if (typeNameMap.containsKey(key)) {
			return (String) typeNameMap.get(key);
		} else {
			return "Unknown";
		}
	}


	protected void overrideColumnInfo(Map map, TableColumn tCol) throws Exception {
		if (map != null && map.size() > 0) {
			ColumnInfo col = (ColumnInfo) map.get(tCol.getColumnName());
			if (col != null) {
				if (col.getData_precision() == null) {
					tCol.setColumnSize(0);
					tCol.setDecimalDigits(0);
					tCol.setWithoutParam(true);
				} else {
					if (col.getData_precision() != null) {
						tCol.setColumnSize(col.getData_precision().intValue());
					} else {
						tCol.setColumnSize(0);
					}
					if (col.getData_scale() != null) {
						tCol.setDecimalDigits(col.getData_scale().intValue());
					} else {
						tCol.setDecimalDigits(0);
					}
					tCol.setWithoutParam(false);
				}

				if (col.getData_default() != null) {
					tCol.setDefaultValue(col.getData_default().trim());
				}
				String dataType = col.getData_type();
				tCol.setDataType(getJavaType(dataType));
				tCol.setTypeName(getTypeName(dataType));

				String remarks = col.getComments();
				if (convertUnicode) {
					remarks = JDBCUnicodeConvertor.convert(remarks);
				}
				tCol.setRemarks(remarks);
				// if (rs.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls) {
				// column.setNotNull(true);
				// } else {
				// column.setNotNull(false);
				// }

			}
		}
	}


	protected String getCustomColumnInfoSQL(String dbName, String owner, String table) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        TRIM(COL.COLUMN_NAME) ").append(COLUMN_NAME_STR);
		sb.append("        ,COL.DATA_TYPE ").append(DATA_TYPE_STR);
		sb.append("        ,COL.DATA_TYPE ").append(TYPE_NAME_STR); // for dummy
		sb.append("        ,CASE");
		sb.append("            WHEN COL.CHAR_MAX_LENGTH IS NULL THEN COL.NUMERIC_PRECISION");
		sb.append("            ELSE CASE");
		sb.append("                WHEN COL.CHAR_MAX_LENGTH > " + Integer.MAX_VALUE + " THEN " + Integer.MAX_VALUE);
		sb.append("                ELSE COL.CHAR_MAX_LENGTH");
		sb.append("            END");
		sb.append("        END ").append(DATA_PRECISION_STR);
		sb.append("        ,COL.NUMERIC_SCALE ").append(DATA_SCALE_STR);
		sb.append("        ,COL.DEFAULT_VALUE ").append(DATA_DEFAULT_STR);;
		sb.append("        ,COM.COMMENT_VALUE ").append(COMMENTS_STR);
		sb.append("    FROM");
		sb.append("        RDBII_SYSTEM.RDBII_TABLE T");
		sb.append("        ,RDBII_SYSTEM.RDBII_COLUMN COL");
		sb.append("            LEFT OUTER JOIN RDBII_SYSTEM.RDBII_COMMENT COM");
		sb.append("                ON (");
		sb.append("                    COL.DB_CODE = COM.DB_CODE");
		sb.append("                    AND COL.SCHEMA_CODE = COM.SCHEMA_CODE");
		sb.append("                    AND COL.TABLE_CODE = COM.TABLE_CODE");
		sb.append("                    AND COL.COLUMN_CODE = COM.COLUMN_CODE");
		sb.append("                    AND COM.COMMENT_TYPE = 'CL'");
		sb.append("                )");
		sb.append("    WHERE");
		sb.append("        T.DB_NAME = '" + SQLUtil.encodeQuotation(dbName) + "'");
		sb.append("        AND T.SCHEMA_NAME = '" + SQLUtil.encodeQuotation(owner) + "'");
		sb.append("        AND T.TABLE_NAME = '" + SQLUtil.encodeQuotation(table) + "'");
		sb.append("        AND T.TABLE_CODE = COL.TABLE_CODE");
		sb.append("    ORDER BY");
		sb.append("        COL.ORDINAL_POSITION");


		return sb.toString();
	}
}
