package com.tulin.v8.ide.editors.data;

import com.tulin.v8.ide.StructureComposition;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.Table;

public class TableViewEditorInput extends zigen.plugin.db.ui.editors.TableViewEditorInput {
	private String tbtype;

	public TableViewEditorInput(IDBConfig config, Table table) {
		super(config, table);
		this.tbtype = table.getTvtype();
	}

	public TableViewEditorInput(IDBConfig config, String tableName, String tbtype) {
		super(config, new Table(tableName, config));
		this.tbtype = tbtype;
	}

	public String getText() {
		return StructureComposition.writeTablePermision(getConfig(), getConfig().getDbName(), getName(), tbtype);
	}

}
