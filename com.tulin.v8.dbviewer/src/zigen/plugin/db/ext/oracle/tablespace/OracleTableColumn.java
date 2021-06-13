/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace;

import java.io.Serializable;

public class OracleTableColumn implements Serializable, IColumn {

	private static final long serialVersionUID = 1L;

	protected String table_owner;

	protected String table_name;

	protected String column_name;

	protected int column_position;

	protected int column_length;

	protected String column_type;

	public OracleTableColumn() {}

	public String getTable_owner() {
		return this.table_owner;
	}

	public void setTable_owner(String table_owner) {
		this.table_owner = table_owner;
	}

	public String getTable_name() {
		return this.table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getColumn_name() {
		return this.column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public int getColumn_position() {
		return this.column_position;
	}

	public void setColumn_position(int column_position) {
		this.column_position = column_position;
	}

	public int getColumn_length() {
		return this.column_length;
	}

	public void setColumn_length(int column_length) {
		this.column_length = column_length;
	}

	public String getColumn_type() {
		return column_type;
	}

	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableColumn:"); //$NON-NLS-1$
		buffer.append(" table_owner: "); //$NON-NLS-1$
		buffer.append(table_owner);
		buffer.append(" table_name: "); //$NON-NLS-1$
		buffer.append(table_name);
		buffer.append(" column_name: "); //$NON-NLS-1$
		buffer.append(column_name);
		buffer.append(" column_position: "); //$NON-NLS-1$
		buffer.append(column_position);
		buffer.append(" column_length: "); //$NON-NLS-1$
		buffer.append(column_length);
		buffer.append(" column_type: "); //$NON-NLS-1$
		buffer.append(column_type);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}
}
