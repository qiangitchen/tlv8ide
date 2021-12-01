/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;

public class Column extends TreeLeaf {

	private static final long serialVersionUID = 1L;

	protected TableColumn column;

	protected TablePKColumn pkColumn = null;

	protected TableFKColumn[] fkColumns = null;

	public boolean isNotNull() {
		return column.isNotNull();
	}

	public Column(TableColumn column) {
		super(column.getColumnName());
		this.column = column;
	}

	public Column(TableColumn column, TablePKColumn pkColumn, TableFKColumn[] fkColumns) {
		this(column);
		this.pkColumn = pkColumn;
		this.fkColumns = fkColumns;
	}

	public void update(Column node) {
		this.column = node.column;
		this.pkColumn = node.pkColumn;
		this.fkColumns = node.fkColumns;
	}

	public void update(TableColumn column, TablePKColumn pkColumn, TableFKColumn[] fkColumns) {
		this.name = column.getColumnName();
		this.column = column;
		this.pkColumn = pkColumn;
		this.fkColumns = fkColumns;
	}

	public TableColumn getColumn() {
		return column;
	}

	public String getName() {
		return column.getColumnName();
	}

	public String getColumnLabel() {
		StringBuffer sb = new StringBuffer();

		sb.append(column.getColumnName());
		sb.append(" ");
		sb.append(column.getTypeName().toLowerCase());

		if (isVisibleColumnSize()) {
			if (column.getDecimalDigits() == 0) {
				sb.append("(" + column.getColumnSize() + ")");
			} else {
				sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
			}
		} else {
			;
		}

		if (pkColumn != null) {
			sb.append(" PK");
		}
		if (fkColumns != null && fkColumns.length > 0) {
			sb.append(" FK");
		}

		if (DbPlugin.getDefault().getPreferenceStore().getBoolean(DBTreeViewPreferencePage.P_DISPLAY_COL_COMMENT)) {
			if (column.getRemarks() != null && column.getRemarks().length() > 0) {
				sb.append(" [");
				sb.append(column.getRemarks());
				sb.append("]");
			}

		}

		return sb.toString();

	}

	public boolean isVisibleColumnSize() {
		IDBConfig config = null;
		ITable table = null;

		if (getParent() instanceof Bookmark) {
			Bookmark bk = (Bookmark) getParent();
			config = bk.getDbConfig();
			table = bk;
		} else if (getParent() instanceof ContentAssistTable) {
			ContentAssistTable cat = (ContentAssistTable) getParent();
			config = cat.getDbConfig();
			table = cat.getTable();
		} else {
			config = getDbConfig();
			table = getTable();
		}

		ISQLCreatorFactory factory;
		if (config != null && table != null) {
			factory = AbstractSQLCreatorFactory.getFactory(config, table);
			return factory.isVisibleColumnSize(column.getTypeName());

		} else {
			return true;
		}
	}

	public String getLogicalColumnLabel() {
		StringBuffer sb = new StringBuffer();

		if (column.getRemarks() != null && column.getRemarks().length() > 0) {
			sb.append(column.getRemarks());
		} else {
			sb.append(column.getColumnName());
		}
		sb.append(":");
		sb.append(column.getTypeName().toLowerCase());

		if (column.getDecimalDigits() == 0) {
			sb.append("(" + column.getColumnSize() + ")");
		} else {
			sb.append("(" + column.getColumnSize() + "," + column.getDecimalDigits() + ")");
		}

		if (pkColumn != null) {
			sb.append(" <Primary Key>");
		}
		// if(fkColumns != null && fkColumns.length>0){ sb.append(" <Foreign
		// Key>");}

		return sb.toString();

	}

	public TableFKColumn[] getFkColumns() {
		return fkColumns;
	}

	public TablePKColumn getPkColumn() {
		return pkColumn;
	}

	public boolean hasPrimaryKey() {
		return (pkColumn != null);
	}

	public boolean hasForeignKey() {
		return (fkColumns != null && fkColumns.length > 0);
	}

	public Column() {
		super();
	}

	public void setColumn(TableColumn column) {
		this.column = column;
	}

	public void setFkColumns(TableFKColumn[] fkColumns) {
		this.fkColumns = fkColumns;
	}

	public void setPkColumn(TablePKColumn pkColumn) {
		this.pkColumn = pkColumn;
	}

	public String getSize() {
		StringBuffer sb = new StringBuffer();
		if (isVisibleColumnSize() && !column.isWithoutParam()) {
			if (column.getDecimalDigits() == 0) {
				sb.append(column.getColumnSize());
			} else {
				sb.append(column.getColumnSize());
				sb.append(",");
				sb.append(column.getDecimalDigits());
			}
		}
		return sb.toString();
	}

	public int getDataType() {
		return column.getDataType();

	}

	public int getDecimalDigits() {
		return column.getDecimalDigits();
	}

	public String getDefaultValue() {
		return column.getDefaultValue();
	}

	public String getRemarks() {
		return column.getRemarks();
	}

	public int getSeq() {
		return column.getSeq();
	}

	public String getTypeName() {
		return column.getTypeName().toUpperCase();

	}

	public void setName(String columnName) {
		this.column.setColumnName(columnName);
	}

	public void setSize(String size) {
		try {
			if (size != null && !"".equals(size) && isVisibleColumnSize()) {
				int comma = size.indexOf(',');
				if (comma > 0) {
					int _size = Integer.parseInt(size.substring(0, comma));
					int _degits = Integer.parseInt(size.substring(comma + 1));
					column.setColumnSize(_size);
					column.setDecimalDigits(_degits);

				} else {
					column.setColumnSize(Integer.parseInt(size));
					column.setDecimalDigits(0);

				}
			}
		} catch (NumberFormatException e) {
			;
		}

	}

	public void setTypeName(String typeName) {
		this.column.setTypeName(typeName);
	}

	public void setDefaultValue(String defaultValue) {
		this.column.setDefaultValue(defaultValue);
	}

	public void setRemark(String remarks) {
		this.column.setRemarks(remarks);
	}

	public void setNotNull(boolean notNull) {
		this.column.setNotNull(notNull);
	}


}
