package com.tulin.v8.ide.ui.editors.data.action;

import com.tulin.v8.ide.ui.editors.data.DataEditor;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class SetDbDdlViewAction implements Runnable {
	private ITable table;
	private DataEditor editor;

	public SetDbDdlViewAction(DataEditor editor, ITable tableNode) {
		this.table = tableNode;
		this.editor = editor;
	}

	@Override
	public void run() {
		DataEditor dataeditor = (DataEditor) editor;
		IDBConfig config = table.getDbConfig();
		ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactory(
				config, table);
		dataeditor.getDdlViewer().getDocument().set(factory.createDDL());
	}

}
