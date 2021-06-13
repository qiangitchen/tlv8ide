/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace.wizard;

import java.math.BigDecimal;

import zigen.plugin.db.ext.oracle.tablespace.CalcTableSpace;
import zigen.plugin.db.ui.internal.Table;

public class TableItem implements IItem {

	Table table;

	private CalcTableSpace tableSpace;

	boolean checked = false;

	long recordSize = 0;

	int pctFree = 10;

	int dbBlockSize = 0;

	public BigDecimal getTableSpaceSize() {
		if (tableSpace != null) {
			return tableSpace.getTableSpaceSize();
		} else {
			return null;
		}
	}

	public double getSafeCoefficient() {
		if (tableSpace != null) {
			return tableSpace.getSafeCoefficient();
		} else {
			return 0;
		}
	}

	public BigDecimal getTableSpaceSafeSize() {
		if (tableSpace != null) {
			return tableSpace.getTableSpaceSafeSize();
		} else {
			return null;
		}
	}

	public TableItem(Table table) {
		this(table, false);
	}

	public TableItem(Table table, boolean b) {
		this.table = table;
		this.checked = b;
	}

	public void setCalcTableSpace(CalcTableSpace tableSpace) throws Exception {
		this.tableSpace = tableSpace;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getTableName() {
		return table.getName();
	}

	public String getIndexName() {
		return null;
	}

	public long getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(long recordSize) {
		this.recordSize = recordSize;
	}

	public int getPctFree() {
		return pctFree;
	}

	public void setPctFree(int pctFree) {
		this.pctFree = pctFree;
	}

}
