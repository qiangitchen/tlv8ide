/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.ITable;

public class TableElement implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	protected ITable table;

	protected int recordNo;

	protected TableColumn[] columns;

	protected Object[] items;

	protected TableColumn[] uniqueColumns;

	protected Object[] uniqueItems;

	protected Object[] orgItems;

	protected List modifiedList = new ArrayList();

	protected boolean isNew = false;

	protected boolean canModify = true;

	protected TablePKColumn[] pks;

	protected List fks;

	protected boolean updatedDataBase;

	public void copy(TableElement src) {
		this.items = src.items;
		this.orgItems = src.orgItems;
	}

	public boolean hasTablePKColumn() {
		return (pks != null);
	}

	public boolean hasTableFKColumn() {
		return (fks != null);
	}

	public void setTablePKColumn(TablePKColumn[] pks) {
		this.pks = pks;
	}

	public void setTableFKColumn(TableFKColumn[] fks) {
		this.fks = convertTableFKColumn(fks);
	}

	public List convertTableFKColumn(TableFKColumn[] fks) {

		List result = new ArrayList();

		String temp = "";
		for (int i = 0; i < fks.length; i++) {

			TableFKColumn fkc = fks[i];
			temp = fkc.getName();
			List list = new ArrayList();

			for (int k = i; k < fks.length; k++) {
				TableFKColumn _fkc = fks[k];
				if (!temp.equals(_fkc.getName())) {
					break;
				} else {
					list.add(_fkc);
					i += k;
				}
				temp = _fkc.getName();
			}
			result.add((TableFKColumn[]) list.toArray(new TableFKColumn[0]));
		}

		return result;
	}

	public void addMofiedColumn(int colIndex) {
		if (columns != null || colIndex <= columns.length - 1)
			if (!modifiedList.contains(columns[colIndex])) {
				this.modifiedList.add(columns[colIndex]);
			}
	}

	public boolean isModify() {
		return (modifiedList.size() > 0);
	}

	public boolean isNew() {
		return this.isNew;
	}

	public TableColumn[] getModifiedColumns() {
		return (TableColumn[]) this.modifiedList.toArray(new TableColumn[0]);
	}

	public Object[] getModifiedItems() {
		Object[] modifiedValues = new Object[modifiedList.size()];
		for (int i = 0; i < modifiedList.size(); i++) {
			modifiedValues[i] = getItem((TableColumn) modifiedList.get(i));
		}
		return modifiedValues;
	}

	public Object getItem(TableColumn column) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals(column)) {
				return items[i];
			}
		}
		return null;
	}

	public Object getItem(int index) {
		return items[index];
	}

	public TableElement(int recordNo, TableColumn[] columns, Object[] items) {
		this.recordNo = recordNo;
		this.columns = columns;
		this.items = items;
	}

	public TableElement(ITable table, int recordNo, TableColumn[] columns, Object[] items, TableColumn[] uniqueColumns, Object[] uniqueItems) {
		this.table = table;
		this.recordNo = recordNo;
		this.columns = columns;

		this.uniqueColumns = uniqueColumns;
		this.uniqueItems = uniqueItems;

		this.items = items;

		if (items != null) {
			this.orgItems = new Object[items.length];
			System.arraycopy(items, 0, this.orgItems, 0, items.length);
		}
	}

	public int getRecordNo() {
		return recordNo;
	}

	public Object[] getItems() {
		return items;
	}

	public TableColumn[] getColumns() {
		return columns;
	}

	public TableColumn[] getUniqueColumns() {
		return uniqueColumns;
	}

	public Object[] getUniqueItems() {
		return uniqueItems;
	}

	protected Object padding(int index, Object obj) {

		if (obj instanceof String) {
			String value = String.valueOf(obj);

			String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
			if (!value.equals(nullSymbol)) {
				TableColumn column = columns[index];
				int type = column.getDataType();
				int size = column.getColumnSize();
				switch (type) {
				case Types.CHAR:
					return StringUtil.padding(value, size);
				}
			}
		}

		return obj;
	}

	public void updateItems(int index, Object obj) {

		// items[index] = obj;
		items[index] = padding(index, obj);

		addMofiedColumn(index);
	}

	public boolean isCanModify() {
		return canModify;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableElement:");
		buffer.append(" table: ");
		buffer.append(table);
		buffer.append(" recordNo: ");
		buffer.append(recordNo);
		buffer.append(" { ");
		for (int i0 = 0; columns != null && i0 < columns.length; i0++) {
			buffer.append(" columns[" + i0 + "]: ");
			buffer.append(columns[i0]);
		}
		buffer.append(" } ");
		buffer.append(" { ");
		for (int i0 = 0; items != null && i0 < items.length; i0++) {
			buffer.append(" items[" + i0 + "]: ");
			buffer.append(items[i0]);
		}
		buffer.append(" } ");
		buffer.append(" { ");
		for (int i0 = 0; uniqueColumns != null && i0 < uniqueColumns.length; i0++) {
			buffer.append(" uniqueColumns[" + i0 + "]: ");
			buffer.append(uniqueColumns[i0]);
		}
		buffer.append(" } ");
		buffer.append(" { ");
		for (int i0 = 0; uniqueItems != null && i0 < uniqueItems.length; i0++) {
			buffer.append(" uniqueItems[" + i0 + "]: ");
			buffer.append(uniqueItems[i0]);
		}
		buffer.append(" } ");
		buffer.append(" modifiedList: ");
		buffer.append(modifiedList);
		buffer.append(" isNew: ");
		buffer.append(isNew);
		buffer.append(" canModify: ");
		buffer.append(canModify);
		buffer.append("]");
		return buffer.toString();
	}

	public Object[] getOrgItems() {
		return orgItems;
	}

	public void modifyUniqueItems() {
		Object[] uniequeItems = new Object[uniqueColumns.length];

		for (int i = 0; i < uniqueColumns.length; i++) {
			TableColumn uCol = uniqueColumns[i];

			for (int k = 0; k < columns.length; k++) {
				TableColumn col = columns[k];

				if (col.getColumnName().equals(uCol.getColumnName())) {
					uniequeItems[i] = items[k];
					break;
				}
			}
		}

		this.uniqueItems = uniequeItems;
	}

	public void clearMofiedColumn() {
		modifiedList = new ArrayList();
	}

	public boolean isUpdatedDataBase() {
		return updatedDataBase;
	}

	public void setUpdatedDataBase(boolean updatedDataBase) {
		this.updatedDataBase = updatedDataBase;
	}

	public void setRecordNo(int recordNo) {
		this.recordNo = recordNo;
	}

	public void isNew(boolean isNew) {
		this.isNew = isNew;
	}

}
