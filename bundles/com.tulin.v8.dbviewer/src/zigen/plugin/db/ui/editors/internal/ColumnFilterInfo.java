/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.OracleColumn;

public class ColumnFilterInfo {

	private Column column;

	private TableColumn fTableColumn;

	private boolean checked = true;

	public ColumnFilterInfo(Column col) {
		this.column = col;
		this.fTableColumn = col.getColumn();
	}

	public ColumnFilterInfo() {}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getColumnName() {
		return fTableColumn.getColumnName();
	}

	public String getTypeName() {
		return fTableColumn.getTypeName().toLowerCase();
	}

	public String getSize() {
		return column.getSize();
	}

	public boolean isNotNull() {
		return fTableColumn.isNotNull();
	}

	public boolean isPrimaryKey() {
		return (column.getPkColumn() != null);
	}

	public String getDefaultValue() {
		return fTableColumn.getDefaultValue();
	}

	public String getCommentName() {
		if (column instanceof OracleColumn) {
			return ((OracleColumn) column).getColumn().getRemarks();
		} else {
			return column.getRemarks();
		}
	}

	private int sortNo = 0;

	private boolean isDesc = false;

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public boolean isDesc() {
		return isDesc;
	}

	public void setDesc(boolean isDesc) {
		this.isDesc = isDesc;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column col) {
		this.column = col;
		this.fTableColumn = col.getColumn();
	}


}
