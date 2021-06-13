/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;
import zigen.plugin.db.core.rule.IColumnSearcherFactory;
import zigen.plugin.db.ext.oracle.tablespace.OracleIndexColumn;
import zigen.plugin.db.ui.internal.ITable;

public class OracleIndexColumnSearcher {

	public static OracleIndexColumn[] execute(IDBConfig config, ITable table, String indexName) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, table, indexName, config.isConvertUnicode());

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleIndexColumn[] execute(Connection con, ITable table, String indexName, boolean convertUnicode) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		try {
			List list = new ArrayList();
			st = con.createStatement();
			rs = st.executeQuery(getSql(table, indexName));

			String owner = table.getSchemaName();
			String tableName = table.getName();

			// TableColumn[] columns = ColumnSearcher.execute(con, owner, tableName, convertUnicode);
			IDBConfig config = table.getDbConfig();
			IColumnSearcherFactory factory = DefaultColumnSearcherFactory.getFactory(con.getMetaData(), config.isConvertUnicode());
			TableColumn[] columns = factory.execute(con, owner, tableName);


			while (rs.next()) {
				OracleIndexColumn index = new OracleIndexColumn();

				index.setIndex_name(rs.getString("INDEX_NAME")); //$NON-NLS-1$
				index.setTable_owner(rs.getString("TABLE_OWNER")); //$NON-NLS-1$
				index.setTable_name(rs.getString("TABLE_NAME")); //$NON-NLS-1$
				index.setColumn_name(rs.getString("COLUMN_NAME")); //$NON-NLS-1$
				index.setColumn_position(rs.getInt("COLUMN_POSITION")); //$NON-NLS-1$
				index.setColumn_position(rs.getInt("COLUMN_LENGTH")); //$NON-NLS-1$

				setOptionInfo(columns, index);

				list.add(index);
			}
			return (OracleIndexColumn[]) list.toArray(new OracleIndexColumn[0]);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

	}

	private static void setOptionInfo(TableColumn[] columns, OracleIndexColumn indexColumn) {
		for (int i = 0; i < columns.length; i++) {
			TableColumn column = columns[i];
			if (column.getColumnName().equals(indexColumn.getColumn_name())) {
				indexColumn.setColumn_type(column.getTypeName());
				indexColumn.setColumn_length(column.getColumnSize());
				return;
			}
		}
		throw new IllegalStateException("The column name was not corresponding."); //$NON-NLS-1$
	}

	private static String getSql(ITable table, String indexName) {
		String owner = table.getSchemaName();
		String tableName = table.getName();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT"); //$NON-NLS-1$
		sb.append("         INDEX_NAME"); //$NON-NLS-1$
		sb.append("         ,TABLE_OWNER"); //$NON-NLS-1$
		sb.append("         ,TABLE_NAME"); //$NON-NLS-1$
		sb.append("         ,COLUMN_NAME"); //$NON-NLS-1$
		sb.append("         ,COLUMN_POSITION"); //$NON-NLS-1$
		sb.append("         ,COLUMN_LENGTH"); //$NON-NLS-1$
		sb.append("     FROM"); //$NON-NLS-1$
		sb.append("         all_ind_columns"); //$NON-NLS-1$
		sb.append("     WHERE"); //$NON-NLS-1$
		sb.append("         table_owner = '" + SQLUtil.encodeQuotation(owner) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     AND table_name = '" + SQLUtil.encodeQuotation(tableName) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("     AND index_name = '" + SQLUtil.encodeQuotation(indexName) + "'"); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();
	}

}
