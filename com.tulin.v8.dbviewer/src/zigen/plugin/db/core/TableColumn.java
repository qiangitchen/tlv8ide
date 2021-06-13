/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

public class TableColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	private int seq;

	private String columnName;

	private int dataType;

	private String typeName;

	private int columnSize;

	private int decimalDigits;

	private String remarks;

	private boolean notNull;

	private String defaultValue;

	private boolean isUniqueKey;

	private boolean withoutParam = false;

	public TableColumn() {}

	public String getColumnName() {
		return (columnName == null) ? "" : columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public String getRemarks() {
		return (remarks == null) ? "" : remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTypeName() {
		return (typeName == null) ? "" : typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public boolean isUniqueKey() {
		return isUniqueKey;
	}

	public void setUniqueKey(boolean isUniqueKey) {
		this.isUniqueKey = isUniqueKey;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableColumn:");
		buffer.append(" seq: ");
		buffer.append(seq);
		buffer.append(" columnName: ");
		buffer.append(columnName);
		buffer.append(" dataType: ");
		buffer.append(dataType);
		buffer.append(" typeName: ");
		buffer.append(typeName);
		buffer.append(" columnSize: ");
		buffer.append(columnSize);
		buffer.append(" decimalDigits: ");
		buffer.append(decimalDigits);
		buffer.append(" remarks: ");
		buffer.append(remarks);
		buffer.append(" notNull: ");
		buffer.append(notNull);
		buffer.append(" defaultValue: ");
		buffer.append(defaultValue);
		buffer.append(" isUniqueKey: ");
		buffer.append(isUniqueKey);
		buffer.append(" withoutParam: ");
		buffer.append(withoutParam);
		buffer.append("]");
		return buffer.toString();
	}

	public String getDefaultValue() {
		return (defaultValue == null) ? "" : defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		TableColumn castedObj = (TableColumn) o;
		return ((this.seq == castedObj.seq) && (this.columnName == null ? castedObj.columnName == null : this.columnName.equals(castedObj.columnName))
				&& (this.dataType == castedObj.dataType) && (this.typeName == null ? castedObj.typeName == null : this.typeName.equals(castedObj.typeName))
				&& (this.columnSize == castedObj.columnSize) && (this.decimalDigits == castedObj.decimalDigits)
				&& (this.remarks == null ? castedObj.remarks == null : this.remarks.equals(castedObj.remarks)) && (this.notNull == castedObj.notNull)
				&& (this.defaultValue == null ? castedObj.defaultValue == null : this.defaultValue.equals(castedObj.defaultValue)) && (this.isUniqueKey == castedObj.isUniqueKey) && (this.withoutParam == castedObj.withoutParam));
	}

	public boolean isWithoutParam() {
		return withoutParam;
	}

	public void setWithoutParam(boolean withoutParam) {
		this.withoutParam = withoutParam;
	}

}
