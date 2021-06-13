/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;

public interface ITable extends INode {

	public abstract String getLabel();

	public abstract String getRemarks();

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getSqlTableName();

	public abstract IDBConfig getDbConfig();

	public abstract String getSchemaName();

	public abstract boolean isSchemaSupport();

	public abstract boolean isExpanded();

	public abstract void setExpanded(boolean b);

	public abstract Column[] getColumns();

	public abstract void addChild(TreeLeaf child);

	public abstract void removeChild(TreeLeaf child);

	public abstract void removeChildAll();

	public abstract TreeLeaf[] getChildrens();

	public abstract TreeLeaf getChild(String name);

	public abstract TablePKColumn[] getTablePKColumns();

	public abstract void setTablePKColumns(TablePKColumn[] tablePKColumns);

	public abstract TableFKColumn[] getTableFKColumns();

	public abstract void setTableFKColumns(TableFKColumn[] tableFKColumns);

	public abstract TableConstraintColumn[] getTableConstraintColumns();

	public abstract void setTableConstraintColumns(TableConstraintColumn[] tableConstraintColumns);

	public TableIDXColumn[] getTableUIDXColumns();

	public void setTableUIDXColumns(TableIDXColumn[] tableUIDXColumns);

	public TableIDXColumn[] getTableNonUIDXColumns();

	public void setTableNonUIDXColumns(TableIDXColumn[] tableNonUIDXColumns);

	public abstract String getFolderName();

	public abstract DataBase getDataBase();

	public abstract Schema getSchema();

	public abstract Folder getFolder();

	public abstract void setRemarks(String remarks);

	public abstract String getEnclosedName();

}
