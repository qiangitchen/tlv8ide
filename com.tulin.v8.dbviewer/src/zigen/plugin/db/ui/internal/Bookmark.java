/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;

public class Bookmark extends TreeNode implements ITable {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_TABLE = 0;

	public static final int TYPE_VIEW = 1;

	public static final int TYPE_SYNONYM = 2;

	protected IDBConfig dbConfig;

	protected DataBase dataBase;

	protected Schema schema;

	protected Table table;

	protected Folder folder;

	protected int type;

	public Bookmark() {
		super();

	}

	public void copy(Table original) {
		name = new String(original.getName());

		dataBase = (DataBase) original.getDataBase().clone();

		dbConfig = dataBase.getDbConfig();

		if (original.getSchema() != null) {
			schema = (Schema) original.getSchema().clone();
			schema.setParent(dataBase);
			folder = (Folder) original.getFolder().clone();
			folder.setParent(schema);
			table = (Table) original.clone();
			table.setParent(folder);
		}else{
			folder = (Folder) original.getFolder().clone();
			folder.setParent(dataBase);
			table = (Table) original.clone();
			table.setParent(folder);
		}


		if (original instanceof Synonym) {
			type = TYPE_SYNONYM;
		} else if (original instanceof View) {
			type = TYPE_VIEW;
		} else {
			type = TYPE_TABLE;
		}

	}

	public Bookmark(Table table) {
		super();
		copy(table);

		this.isEnabled = false;
	}

	public void update(Table table) {
		this.table.update(table);
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
		if (table != null) {
			return table.getRemarks();
		} else {
			return null;
		}
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
		return (ITable) table;
	}

	public void setTable(ITable table) {
		if (table instanceof Table) {
			this.table = (Table) table;
		} else {
			this.table = null;
		}
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

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public IDBConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(IDBConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	public BookmarkRoot getBookmarkRoot() {
		return getBookmarkRoot(this);
	}

	public BookmarkFolder getBookmarkFolder() {
		return getBookmarkFolder(this);
	}

	private BookmarkRoot getBookmarkRoot(TreeLeaf leaf) {
		if (leaf instanceof BookmarkRoot) {
			return (BookmarkRoot) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkRoot(leaf.getParent());
			} else {
				// return null;
				throw new IllegalStateException("getBookmarkRoot#BookmarkRoot");
			}
		}
	}

	private BookmarkFolder getBookmarkFolder(TreeLeaf leaf) {
		if (leaf instanceof BookmarkFolder) {
			return (BookmarkFolder) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkFolder(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		Bookmark castedObj = (Bookmark) o;

		return ((this.dbConfig == null ? castedObj.dbConfig == null : this.dbConfig.equals(castedObj.dbConfig))
				&& (this.dataBase == null ? castedObj.dataBase == null : this.dataBase.equals(castedObj.dataBase))
				&& (this.schema == null ? castedObj.schema == null : this.schema.equals(castedObj.schema)) && (this.table == null ? castedObj.table == null : this.table
				.equals(castedObj.table)));
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

	public String getFolderName() {
		if (folder == null) {
			if (type == Bookmark.TYPE_TABLE) {
				return "TABLE";
			} else if (type == Bookmark.TYPE_VIEW) {
				return "VIEW";
			} else if (type == Bookmark.TYPE_SYNONYM) {
				return "SYNONYM";
			} else {
				return "TABLE";
			}
		}
		return folder.getName();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Bookmark:");
		buffer.append(" dbConfig: ");
		buffer.append(dbConfig);
		buffer.append(" dataBase: ");
		buffer.append(dataBase);
		buffer.append(" schema: ");
		buffer.append(schema);
		buffer.append(" table: ");
		buffer.append(table);
		buffer.append(" folder: ");
		buffer.append(folder);
		buffer.append(" type: ");
		buffer.append(type);
		buffer.append("]");
		return buffer.toString();
	}

	// boolean isEnabled = true;
	boolean isEnabled = false;

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean b) {
		this.isEnabled = b;
	}

	public boolean isSynonym() {
		return type == Bookmark.TYPE_SYNONYM;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setRemarks(String remarks) {
		if (table != null) {
			this.table.setRemarks(remarks);
		}
	}

	public TableConstraintColumn[] getTableConstraintColumns() {
		return table.getTableConstraintColumns();
	}

	public void setTableConstraintColumns(TableConstraintColumn[] tableConstraintColumns) {
		table.setTableConstraintColumns(tableConstraintColumns);
	}


}
