package zigen.plugin.db.core.rule.oracle;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.ColumnInfo;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;

public class OracleColumnSearcharFactory extends DefaultColumnSearcherFactory {

	public OracleColumnSearcharFactory(DatabaseMetaData meta, boolean convertUnicode) {
		super(meta, convertUnicode);
	}

	protected void overrideColumnInfo(Map map, TableColumn tCol) throws Exception {
		if (map != null && map.size() > 0) {
			ColumnInfo col = (ColumnInfo) map.get(tCol.getColumnName());

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

			tCol.setRemarks(col.getComments());
		}
	}

	protected String getCustomColumnInfoSQL(String dbName, String owner, String table) {
		int databaseProductMajorVersion = getDatabaseMajorVersion();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        COL.COLUMN_NAME ").append(COLUMN_NAME_STR);
		sb.append("        ,COL.DATA_TYPE ").append(DATA_TYPE_STR);
		// sb.append(" ,COL.DATA_PRECISION");
		if (databaseProductMajorVersion <= 8) {
			// for Oracle8i
			sb.append("        ,DECODE(COL.DATA_TYPE, 'NUMBER', COL.DATA_PRECISION, COL.DATA_LENGTH) ").append(DATA_PRECISION_STR);
		} else {
			// for Oracle 9i, 10g
			sb.append("        ,DECODE(CHAR_LENGTH, 0, COL.DATA_PRECISION, CHAR_LENGTH) ").append(DATA_PRECISION_STR); // Oracle9i
		}
		sb.append("        ,COL.DATA_SCALE ").append(DATA_SCALE_STR);
		sb.append("        ,COL.DATA_DEFAULT ").append(DATA_DEFAULT_STR);
		sb.append("        ,COM.COMMENTS ").append(COMMENTS_STR);
		sb.append("    FROM");
		sb.append("        ALL_TAB_COLUMNS COL");
		sb.append("        ,ALL_COL_COMMENTS COM");
		sb.append("    WHERE");
		sb.append("        COL.OWNER = COM.OWNER");
		sb.append("        AND COL.TABLE_NAME = COM.TABLE_NAME");
		sb.append("        AND COL.COLUMN_NAME = COM.COLUMN_NAME");
		sb.append("        AND COL.OWNER = '" + SQLUtil.encodeQuotation(owner) + "'");
		sb.append("        AND COL.TABLE_NAME = '" + SQLUtil.encodeQuotation(table) + "'");
		sb.append("    ORDER BY");
		sb.append("        COL.COLUMN_ID");
		return sb.toString();
	}

}