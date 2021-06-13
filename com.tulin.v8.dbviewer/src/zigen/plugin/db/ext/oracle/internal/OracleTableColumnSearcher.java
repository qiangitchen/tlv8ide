/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;
import zigen.plugin.db.core.rule.IColumnSearcherFactory;
import zigen.plugin.db.ext.oracle.tablespace.OracleTableColumn;
import zigen.plugin.db.ui.internal.Table;

public class OracleTableColumnSearcher {

	public static OracleTableColumn[] execute(IDBConfig config, Table table) throws Exception {
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			return execute(con, table, config.isConvertUnicode());

		} catch (Exception e) {
			throw e;
		}
	}

	public static OracleTableColumn[] execute(Connection con, Table table, boolean convertUnicode) throws Exception {
		try {
			String owner = table.getSchemaName();
			String tableName = table.getName();

			// TableColumn[] columns = ColumnSearcher.execute(con, owner, tableName, convertUnicode);
			IDBConfig config = table.getDbConfig();
			IColumnSearcherFactory factory = DefaultColumnSearcherFactory.getFactory(con.getMetaData(), config.isConvertUnicode());
			TableColumn[] columns = factory.execute(con, owner, tableName);

			List list = new ArrayList(columns.length);

			for (int i = 0; i < columns.length; i++) {
				TableColumn column = columns[i];

				OracleTableColumn col = new OracleTableColumn();
				col.setColumn_name(column.getColumnName());
				col.setColumn_length(column.getColumnSize());
				col.setColumn_type(column.getTypeName());
				col.setColumn_position(column.getSeq());

				list.add(col);

			}

			return (OracleTableColumn[]) list.toArray(new OracleTableColumn[0]);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		}

	}

}
