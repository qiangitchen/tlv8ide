package zigen.plugin.db.core.rule.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zigen.plugin.db.core.ConstraintSeqSorter;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.rule.DefaultConstraintSearcherFactory;

public class MySQLConstraintSearcharFactory extends DefaultConstraintSearcherFactory {

	public MySQLConstraintSearcharFactory() {
		super();
	}

	public TablePKColumn[] getPKColumns(Connection con, String schemaPattern, String tableName) throws Exception {
		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;

		try {
			DatabaseMetaData objMet = con.getMetaData();

			if (DBType.getType(objMet) == DBType.DB_TYPE_MYSQL && objMet.getDatabaseMajorVersion() >= 5) {

				st = con.createStatement();
				rs = st.executeQuery(getSQL(schemaPattern, tableName));
				int i = 0;
				while (rs.next()) {
					i++;
					TablePKColumn column = new TablePKColumn();
					column.setSep(rs.getInt("KEY_SEQ")); //$NON-NLS-1$
					column.setColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$
					column.setName(rs.getString("PK_NAME")); //$NON-NLS-1$
					list.add(column);
				}

				Collections.sort(list, new ConstraintSeqSorter());

				return (TablePKColumn[]) list.toArray(new TablePKColumn[0]);

			} else {
				return super.getPKColumns(con, schemaPattern, tableName);
			}

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);
		}

	}

	private static String getSQL(String schemaPattern, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("        TABLE_SCHEMA AS TABLE_CAT");
		sb.append("        ,NULL AS TABLE_SCHEM");
		sb.append("        ,TABLE_NAME");
		sb.append("        ,COLUMN_NAME");
		sb.append("        ,SEQ_IN_INDEX AS KEY_SEQ");
		sb.append("        ,'PRIMARY' AS PK_NAME");
		sb.append("    FROM");
		sb.append("        INFORMATION_SCHEMA.STATISTICS");
		sb.append("    WHERE");
		sb.append("        INDEX_NAME = 'PRIMARY'");
		sb.append("        AND TABLE_SCHEMA = '" + SQLUtil.encodeQuotation(schemaPattern) + "'");
		sb.append("        AND TABLE_NAME = '" + SQLUtil.encodeQuotation(tableName) + "'");
		sb.append("    ORDER BY");
		sb.append("        TABLE_SCHEMA");
		sb.append("        ,TABLE_NAME");
		sb.append("        ,INDEX_NAME");
		sb.append("        ,SEQ_IN_INDEX");
		return sb.toString();

	}


}
