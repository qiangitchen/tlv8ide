/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.sql.Connection;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.AbstractStatementFactory;
import zigen.plugin.db.core.rule.DefaultStatementFactory;


public class DataBase extends TreeNode {

	private static final long serialVersionUID = 1L;

	IDBConfig dbConfig = null;

	boolean isConnected = false;

	boolean isSchemaSupport = false;

	String defaultSchema;

	String[] tableType = null;

	Connection con = null;

	char encloseChar;

	public DataBase(IDBConfig dbConfig) {
		super(dbConfig.getDbName());
		this.dbConfig = dbConfig;

		this.encloseChar = AbstractStatementFactory.getFactory(dbConfig).getEncloseChar();
	}

	public IDBConfig getDbConfig() {
		return this.dbConfig;
	}

	public void setDbConfig(IDBConfig dbConfig) {
		this.setName(dbConfig.getDbName());
		this.dbConfig = dbConfig;

	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	public boolean isSchemaSupport() {
		return isSchemaSupport;
	}

	public void setSchemaSupport(boolean isSchemaSupport) {
		this.isSchemaSupport = isSchemaSupport;
	}

	public String[] getTableType() {
		return tableType;
	}

	public void setTableType(String[] tableType) {
		this.tableType = tableType;
	}

	public DataBase() {
		super();
	}

	public Object clone() {
		DataBase inst = new DataBase();
		inst.name = this.name == null ? null : new String(this.name);
		inst.dbConfig = this.dbConfig == null ? null : (IDBConfig) this.dbConfig.clone();
		inst.isConnected = this.isConnected;
		inst.isSchemaSupport = this.isSchemaSupport;
		inst.defaultSchema = this.defaultSchema == null ? null : new String(this.defaultSchema);
		if (this.tableType != null) {
			inst.tableType = new String[this.tableType.length];
			for (int i0 = 0; i0 < this.tableType.length; i0++) {
				inst.tableType[i0] = this.tableType[i0] == null ? null : new String(this.tableType[i0]);
			}
		} else {
			inst.tableType = null;
		}
		inst.encloseChar = this.encloseChar;
		return inst;
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
		DataBase castedObj = (DataBase) o;
		IDBConfig config = castedObj.getDbConfig();
		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig())) {
			return true;
		} else {
			return false;
		}

	}


	public char getEncloseChar() {
		return encloseChar;
	}


	public void setEncloseChar(char encloseChar) {
		this.encloseChar = encloseChar;
	}

}
