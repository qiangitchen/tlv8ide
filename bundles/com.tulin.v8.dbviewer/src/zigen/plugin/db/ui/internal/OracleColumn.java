/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;

public class OracleColumn extends Column {

	private static final long serialVersionUID = 1L;

	public OracleColumn() {
		super();
	}

	public OracleColumn(TableColumn column) {
		super(column);
	}

	public OracleColumn(TableColumn column, TablePKColumn pkColumn, TableFKColumn[] fkColumns) {
		super(column, pkColumn, fkColumns);
	}

	public void update(OracleColumn node) {
		super.update(node);
	}

	public String getColumnLabel() {
		StringBuffer sb = new StringBuffer();

		sb.append(column.getColumnName());
		sb.append(" ");
		sb.append(column.getTypeName().toLowerCase());

		// if (isVisibleColumnSize()) {
		if (isVisibleColumnSize() && !column.isWithoutParam()) {
			if (column.getDecimalDigits() == 0) {
				sb.append("(" + column.getColumnSize() + ")");
			} else {
				sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
			}
		}
		if (pkColumn != null) {
			sb.append(" PK");
		}
		if (fkColumns != null && fkColumns.length > 0) {
			sb.append(" FK");
		}

		if (DbPlugin.getDefault().getPreferenceStore().getBoolean(DBTreeViewPreferencePage.P_DISPLAY_COL_COMMENT)) {
			if (column.getRemarks() != null && column.getRemarks().length() > 0) {
				sb.append(" [");
				sb.append(column.getRemarks());
				sb.append("]");
			}

		}
		return sb.toString();

	}

}
