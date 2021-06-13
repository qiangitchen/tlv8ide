package zigen.plugin.db.core.rule.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConstraintNameSorter;
import zigen.plugin.db.core.ConstraintSeqSorter;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.rule.DefaultConstraintSearcherFactory;

public class OracleConstraintSearcharFactory extends DefaultConstraintSearcherFactory {

	public OracleConstraintSearcharFactory() {
		super();
	}

	private String getOtherCounstraintQuery(String schemaPattern, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT"); //$NON-NLS-1$
		sb.append("        CON.CONSTRAINT_NAME CONSTRAINT_NAME"); //$NON-NLS-1$
		sb.append("        ,COL.COLUMN_POSITION AS ORDINAL_POSITION"); //$NON-NLS-1$
		sb.append("        ,COL.COLUMN_NAME"); //$NON-NLS-1$
		sb.append("        ,IND.UNIQUENESS"); //$NON-NLS-1$
		sb.append("        ,IND.INDEX_TYPE"); //$NON-NLS-1$
		sb.append("        ,CON.SEARCH_CONDITION"); //$NON-NLS-1$
		sb.append("        ,CON.TABLE_NAME"); //$NON-NLS-1$
		sb.append("        ,DECODE(CON.CONSTRAINT_TYPE, 'P','PRIMARY KEY', 'R','FOREIGN KEY', 'C','CHECK', 'U', 'UNIQUE', 'UNKNOWN') CONSTRAINT_TYPE");

		sb.append("    FROM"); //$NON-NLS-1$
		sb.append("        ALL_INDEXES IND"); //$NON-NLS-1$
		sb.append("        ,ALL_CONSTRAINTS CON"); //$NON-NLS-1$
		sb.append("        ,ALL_IND_COLUMNS COL"); //$NON-NLS-1$
		sb.append("    WHERE"); //$NON-NLS-1$
		sb.append("        CON.OWNER = '" + SQLUtil.encodeQuotation(schemaPattern) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("        AND CON.TABLE_NAME = '" + SQLUtil.encodeQuotation(tableName) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
//		sb.append("        AND CON.GENERATED = 'USER NAME'"); //$NON-NLS-1$
		sb.append("        AND (CON.GENERATED = 'USER NAME' OR (CON.GENERATED = 'GENERATED NAME' AND COLUMN_POSITION IS NOT NULL))"); //$NON-NLS-1$
		
		sb.append("        AND CON.OWNER = COL.INDEX_OWNER(+)"); //$NON-NLS-1$
		sb.append("        AND CON.CONSTRAINT_NAME = COL.INDEX_NAME(+)"); //$NON-NLS-1$
		sb.append("        AND CON.OWNER = COL.TABLE_OWNER(+)"); //$NON-NLS-1$
		sb.append("        AND CON.OWNER = IND.TABLE_OWNER(+)"); //$NON-NLS-1$
		sb.append("        AND CON.TABLE_NAME = IND.TABLE_NAME(+)"); //$NON-NLS-1$
		sb.append("        AND CON.CONSTRAINT_NAME = IND.INDEX_NAME(+)"); //$NON-NLS-1$
		sb.append("        AND CON.CONSTRAINT_TYPE NOT IN('P','R')"); // PK,
		// //$NON-NLS-1$
		return sb.toString();

	}

	public TableConstraintColumn[] getConstraintColumns(Connection con, String schemaPattern, String tableName) throws Exception {
		List list = new ArrayList();

		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();

			TimeWatcher tw = new TimeWatcher();
			tw.start();
			String sql = getOtherCounstraintQuery(schemaPattern, tableName);
			rs = st.executeQuery(sql);
			tw.stop();


			while (rs.next()) {
				String constraintName = rs.getString("CONSTRAINT_NAME"); //$NON-NLS-1$

				if (constraintName != null) {

					TableConstraintColumn column = new TableConstraintColumn();

					column.setName(constraintName);
					column.setOrdinal_position(rs.getInt("ORDINAL_POSITION")); //$NON-NLS-1$
					column.setColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$

					if ("UNIQUE".equals(rs.getString("UNIQUENESS"))) { //$NON-NLS-1$ //$NON-NLS-2$
						column.setNonUnique(false);
					} else {
						column.setNonUnique(true);
					}
					column.setIndexType(rs.getString("INDEX_TYPE")); //$NON-NLS-1$
					column.setSearch_condition(rs.getString("SEARCH_CONDITION")); //$NON-NLS-1$
					column.setConstraintType(rs.getString("CONSTRAINT_TYPE")); //$NON-NLS-1$

					list.add(column);
				}

			}

			Collections.sort(list, new ConstraintSeqSorter());
			Collections.sort(list, new ConstraintNameSorter());

			return (TableConstraintColumn[]) list.toArray(new TableConstraintColumn[0]);

		} catch (SQLException e) {
			DbPlugin.log(e);
			return new TableConstraintColumn[0];

		} catch (Exception e) {
			throw e;

		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
	}

}
