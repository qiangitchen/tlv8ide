/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

public class TableIDXColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	protected int ordinal_position = 0;

	protected String name = null;

	protected String columnName = null;

	protected boolean nonUnique = false;

	protected String indexType = null;

	public TableIDXColumn() {

	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNonUnique() {
		return nonUnique;
	}

	public void setNonUnique(boolean nonUnique) {
		this.nonUnique = nonUnique;
	}

	public int getOrdinal_position() {
		return ordinal_position;
	}

	public void setOrdinal_position(int ordinal_position) {
		this.ordinal_position = ordinal_position;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableIDXColumn:");
		buffer.append(" ordinal_position: ");
		buffer.append(ordinal_position);
		buffer.append(" name: ");
		buffer.append(name);
		buffer.append(" columnName: ");
		buffer.append(columnName);
		buffer.append(" nonUnique: ");
		buffer.append(nonUnique);
		buffer.append(" indexType: ");
		buffer.append(indexType);
		buffer.append("]");
		return buffer.toString();
	}

}
