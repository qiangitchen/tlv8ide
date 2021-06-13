/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.io.Serializable;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class DDL implements IDDL, Serializable {

	private static final long serialVersionUID = 1L;

	boolean isSchemaSupport;

	String dbName;

	String schemaName;

	String targetName;

	String ddl;

	ITable table;

	public DDL() {}


	public DDL(ITable table) {
		setTable(table);
	}

	private String getDDLString(ITable tableNode) {
		String result = ""; //$NON-NLS-1$
		try {
			if (tableNode != null) {
				IDBConfig config = tableNode.getDbConfig();
				ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactoryNoCache(config, tableNode);
				factory.setVisibleSchemaName(false);
				result = factory.createDDL();
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return result;
	}

	public String getDisplayedName() {
		StringBuffer sb = new StringBuffer();
		if (isSchemaSupport) {
			sb.append(schemaName + "." + targetName); //$NON-NLS-1$
		} else {
			sb.append(targetName);
		}
		return sb.toString();
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDdl() {
		return ddl;
	}

	public void setDdl(String ddl) {
		this.ddl = ddl;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String tableName) {
		this.targetName = tableName;
	}

	public boolean isSchemaSupport() {
		return isSchemaSupport;
	}

	public void setSchemaSupport(boolean isSchemaSupport) {
		this.isSchemaSupport = isSchemaSupport;
	}

	public String getType() {
		return table.getFolderName();
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
		this.dbName = table.getDbConfig().getDbName();
		this.schemaName = table.getSchemaName();
		this.targetName = table.getName();
		this.isSchemaSupport = table.getDataBase().isSchemaSupport();
		this.ddl = getDDLString(table);
	}

}
