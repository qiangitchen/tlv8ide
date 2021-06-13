/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.sqlserver;

import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class SQLServerSQLCreatorFactory extends DefaultSQLCreatorFactory {

	public SQLServerSQLCreatorFactory(ITable table) {
		super(table);
	}

	public String createSelect(String _condition, int limit) {
		StringBuffer sb = new StringBuffer();

		if (limit > 0) {
			sb.append("SELECT TOP ");
			sb.append(++limit);
		} else {
			sb.append("SELECT ");
		}

		sb.append(" * FROM ");
		sb.append(getTableNameWithSchemaForSQL(table, isVisibleSchemaName));

		String[] conditions = SQLFormatter.splitOrderCause(_condition);
		String condition = conditions[0];
		String orderBy = conditions[1];

		if (condition != null && !"".equals(condition.trim())) {
			sb.append(" WHERE " + condition);
		}

		// ORDER BY
		if (orderBy != null && !"".equals(orderBy)) { //$NON-NLS-1$
			sb.append(" " + orderBy); //$NON-NLS-1$
		}

		return sb.toString();
	}

	public String[] createAddColumnDDL(Column column) {
		return null;
	}

	public String createCommentOnColumnDDL(Column column) {
		return null;
	}

	public String createCommentOnTableDDL(String commnets) {
		return null;
	}

	public String[] createDropColumnDDL(Column column, boolean cascadeConstraints) {
		return null;
	}

	public String[] createModifyColumnDDL(Column from, Column to) {
		return null;
	}

	public String createRenameColumnDDL(Column from, Column to) {
		return null;
	}

	public String createRenameTableDDL(String newTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("sp_rename");
		sb.append(" '" + SQLUtil.encodeQuotation(table.getSqlTableName()) + "'");
		sb.append(", '" + SQLUtil.encodeQuotation(newTableName) + "'");
		return sb.toString();
	}

	public boolean supportsModifyColumnSize(String columnType) {
		return false;
	}

	public boolean supportsModifyColumnType() {
		return false;
	}

	public boolean supportsRemarks() {
		return false;
	}

	public boolean supportsDropColumnCascadeConstraints() {
		return false;
	}

	// SQLServer is true
	public boolean supportsRollbackDDL() {
		return true;
	}
}
