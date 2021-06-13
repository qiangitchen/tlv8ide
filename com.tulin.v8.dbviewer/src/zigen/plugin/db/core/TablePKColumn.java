/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

public class TablePKColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	private int sep = 0;

	private String name = null;

	private String columnName = null;

	public TablePKColumn() {

	}

	public int getSep() {
		return sep;
	}

	public void setSep(int sep) {
		this.sep = sep;
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

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TablePKColumn:");
		buffer.append(" sep: ");
		buffer.append(sep);
		buffer.append(" name: ");
		buffer.append(name);
		buffer.append(" columnName: ");
		buffer.append(columnName);
		buffer.append("]");
		return buffer.toString();
	}
}
