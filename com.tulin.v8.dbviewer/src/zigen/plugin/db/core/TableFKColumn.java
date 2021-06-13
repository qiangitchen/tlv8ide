/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

public class TableFKColumn extends TablePKColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pkSchema = null;

	private String pkTableName = null;

	private String pkColumnName = null;

	private String pkName = null;

	private boolean isCasucade = false;

	public boolean isCasucade() {
		return isCasucade;
	}

	public void setCasucade(boolean isCasucade) {
		this.isCasucade = isCasucade;
	}

	public String getPkColumnName() {
		return pkColumnName;
	}

	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getPkSchema() {
		return pkSchema;
	}

	public void setPkSchema(String pkSchema) {
		this.pkSchema = pkSchema;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TableFKColumn:");
		buffer.append(" pkSchema: ");
		buffer.append(pkSchema);
		buffer.append(" pkTableName: ");
		buffer.append(pkTableName);
		buffer.append(" pkColumnName: ");
		buffer.append(pkColumnName);
		buffer.append(" pkName: ");
		buffer.append(pkName);
		buffer.append(" isCasucade: ");
		buffer.append(isCasucade);
		buffer.append("]");
		return buffer.toString();
	}
}
