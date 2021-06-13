package zigen.plugin.db.core.rule.oracle;

import java.sql.DatabaseMetaData;

import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultTableInfoSearchFactory;


public class OracleTableInfoSearchFactory extends DefaultTableInfoSearchFactory{

	public OracleTableInfoSearchFactory(DatabaseMetaData meta){
		super(meta);
	}
	public String getTableInfoAllSql(String schema, String[] types) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        CAT.TABLE_NAME");
		sb.append("        ,CAT.TABLE_TYPE");
		sb.append("        ,C.COMMENTS REMARKS");
		sb.append("    FROM");
		sb.append("        ALL_CATALOG CAT");
		sb.append("        ,ALL_TAB_COMMENTS C");
		sb.append("    WHERE");
		sb.append("        CAT.OWNER = C.OWNER(+)");
		sb.append("        AND CAT.TABLE_NAME = C.TABLE_NAME(+)");
		sb.append("        AND CAT.TABLE_TYPE = C.TABLE_TYPE(+)");
		sb.append("        AND CAT.OWNER = '" + SQLUtil.encodeQuotation(schema) + "'");
		if (types.length > 0) {
			sb.append("    AND (");
			for (int i = 0; i < types.length; i++) {
				if (i > 0) {
					sb.append(" OR ");
				}
				sb.append("    CAT.TABLE_TYPE = '" + SQLUtil.encodeQuotation(types[i]) + "'");
			}
			sb.append("    )");
		}

		return sb.toString();
	}

	public String getTableInfoSql(String schema, String tableName, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        CAT.TABLE_NAME");
		sb.append("        ,CAT.TABLE_TYPE");
		sb.append("        ,C.COMMENTS REMARKS");
		sb.append("    FROM");
		sb.append("        ALL_CATALOG CAT");
		sb.append("        ,ALL_TAB_COMMENTS C");
		sb.append("    WHERE");
		sb.append("        CAT.OWNER = C.OWNER (+)");
		sb.append("        AND CAT.TABLE_NAME = C.TABLE_NAME (+)");
		sb.append("        AND CAT.TABLE_TYPE = C.TABLE_TYPE (+)");
		sb.append("        AND CAT.OWNER = '" + SQLUtil.encodeQuotation(schema) + "'");
		sb.append("        AND CAT.TABLE_TYPE = '" + SQLUtil.encodeQuotation(type) + "'");
		sb.append("        AND CAT.TABLE_NAME = '" + SQLUtil.encodeQuotation(tableName) + "'");
		return sb.toString();
	}

	public String getDbName() {
		return null;
	}


}
