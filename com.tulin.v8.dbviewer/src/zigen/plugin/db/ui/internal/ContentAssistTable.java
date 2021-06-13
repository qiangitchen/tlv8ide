/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.Transaction;

public class ContentAssistTable extends TreeNode implements ITable {

	private static final long serialVersionUID = 1L;

	protected IDBConfig dbConfig;

	protected DataBase dataBase;

	protected Schema schema;

	protected ITable table;

	protected Folder folder;

	public ContentAssistTable(IDBConfig dbConfig, String schemaName, String tableName) {
		super(tableName);
		this.dbConfig = dbConfig;
		this.dataBase = new DataBase(dbConfig);
		setSchemaSupport(dataBase);

		if (dataBase.isSchemaSupport()) {
			this.schema = new Schema(schemaName);
		}
		this.table = new Table(tableName);

	}

	private void setSchemaSupport(DataBase dataBase) {
		try {
			Connection con = Transaction.getInstance(dbConfig).getConnection();
			boolean b = SchemaSearcher.isSupport(con);
			this.dataBase.setSchemaSupport(b);
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	private Column[] convertColumns(TreeLeaf[] leafs) {
		List list = new ArrayList(leafs.length);
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Column) {
				list.add((Column) leafs[i]);
			}
		}
		return (Column[]) list.toArray(new Column[0]);

	}

	public Column[] getColumns() {
		return convertColumns(getChildrens());
	}

	public String getLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		if (table.getRemarks() != null && table.getRemarks().length() > 0) {
			sb.append(" [");
			sb.append(table.getRemarks());
			sb.append("]");
		}
		return sb.toString();

	}

	public String getRemarks() {
		return table.getRemarks();
	}

	public String getSchemaName() {
		if (schema != null) {
			return schema.getName();
		} else {
			return null;
		}
	}

	public String getEnclosedName() {
		return SQLUtil.enclose(name, getDataBase().getEncloseChar());
	}

	public String getSqlTableName() {
		StringBuffer sb = new StringBuffer();
		if (getDataBase().isSchemaSupport()) {
			sb.append(getSchema().getEscapedName());
			sb.append(".");
			sb.append(getEnclosedName());
		} else {
			sb.append(getEnclosedName());
		}
		return sb.toString();
	}


	public boolean isSchemaSupport() {
		return dataBase.isSchemaSupport();
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public IDBConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(IDBConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	public TableFKColumn[] getTableFKColumns() {
		if (table != null) {
			return table.getTableFKColumns();
		} else {
			return null;
		}
	}

	public TablePKColumn[] getTablePKColumns() {
		if (table != null) {
			return table.getTablePKColumns();
		} else {
			return null;
		}
	}

	public TableIDXColumn[] getTableUIDXColumns() {
		if (table != null) {
			return table.getTableUIDXColumns();
		} else {
			return null;
		}
	}

	public TableIDXColumn[] getTableNonUIDXColumns() {
		if (table != null) {
			return table.getTableNonUIDXColumns();
		} else {
			return null;
		}
	}

	public void setTableFKColumns(TableFKColumn[] tableFKColumns) {
		this.table.setTableFKColumns(tableFKColumns);
	}

	public void setTablePKColumns(TablePKColumn[] tablePKColumns) {
		this.table.setTablePKColumns(tablePKColumns);
	}

	public void setTableUIDXColumns(TableIDXColumn[] tableUIDXColumns) {
		this.table.setTableUIDXColumns(tableUIDXColumns);
	}

	public void setTableNonUIDXColumns(TableIDXColumn[] tableNonUIDXColumns) {
		this.table.setTableNonUIDXColumns(tableNonUIDXColumns);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ContentsAssistTable:");
		buffer.append(" children: ");
		buffer.append(children);
		buffer.append(" isRoot: ");
		buffer.append(isRoot);
		buffer.append(" isExpanded: ");
		buffer.append(isExpanded);
		buffer.append(" name: ");
		buffer.append(name);
		buffer.append(" parent: ");
		buffer.append(parent);
		buffer.append(" dbConfig: ");
		buffer.append(dbConfig);
		buffer.append(" dataBase: ");
		buffer.append(dataBase);
		buffer.append(" schema: ");
		buffer.append(schema);
		buffer.append(" folder: ");
		buffer.append(folder);
		buffer.append(" table: ");
		buffer.append(table);
		buffer.append("]");
		return buffer.toString();
	}

	public String getFolderName() {
		if (folder == null)
			return "TABLE";

		return folder.getName();
	}

	boolean isEnabled = true;

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean b) {
		this.isEnabled = b;
	}

	public void setRemarks(String remarks) {
		throw new UnsupportedOperationException("ContentAssistTable#setRemarks");
	}

	public TableConstraintColumn[] getTableConstraintColumns() {
		return table.getTableConstraintColumns();
	}

	public void setTableConstraintColumns(TableConstraintColumn[] tableConstraintColumns) {
		table.setTableConstraintColumns(tableConstraintColumns);
	}


}
