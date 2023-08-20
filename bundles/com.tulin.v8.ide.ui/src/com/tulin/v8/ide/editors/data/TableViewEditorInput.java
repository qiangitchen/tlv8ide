package com.tulin.v8.ide.editors.data;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.tulin.v8.ide.StructureComposition;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Table;

public class TableViewEditorInput implements IEditorInput {
	private String tooltip;
	private String name;
	private String tbtype;
	private IDBConfig config;
	private String schemaName;
	private Table table;

	public TableViewEditorInput(IDBConfig config, Table table) {
		super();
		this.config = config;
		this.schemaName = config.getSchema();
		this.table = table;
		this.name = table.getName();
		this.tbtype = table.getTvtype();
		this.tooltip = table.getName() + " [" + config.getUserId() + " : " + config.getDbName() + "]";

		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(config.getDbName());
		sb.append("] ");
		sb.append(table.getSqlTableName());
		if (table.getRemarks() != null && !"".equals(table.getRemarks().trim())) {
			sb.append(" [");
			sb.append(table.getRemarks());
			sb.append("]");
		}
		this.tooltip = sb.toString();
	}

	public TableViewEditorInput(IDBConfig config, String tableName, String tbtype) {
		super();
		this.config = config;
		this.schemaName = config.getSchema();
		this.name = tableName;
		this.tbtype = tbtype;
		this.tooltip = tableName + " [" + config.getUserId() + " : " + config.getDbName() + "]";
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return tooltip;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public boolean equals(Object o) {
		boolean b = false;
		if (o == this)
			return true;

		if (o instanceof TableViewEditorInput) {
			TableViewEditorInput input = (TableViewEditorInput) o;

			if (schemaName != null && input.schemaName != null) {
				b = (config.equals(input.config) && schemaName.equals(input.schemaName) && name.equals(input.name)
						&& table.getClass() == input.table.getClass());
				return b;
			} else if (schemaName == null && input.schemaName == null) {
				b = (config.equals(input.config) && name.equals(input.name));
				return b;
			} else {
				return false;
			}

		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public IDBConfig getConfig() {
		return config;
	}

	public ITable getTable() {
		return table;
	}

	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getText() {
		return StructureComposition.writeTablePermision(config, schemaName, name, tbtype);
	}

}
