/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class ExecuteSQLAction extends AbstractExecuteSQLAction implements Runnable {

	public ExecuteSQLAction(IDBConfig config, SQLSourceViewer viewer, String secondaryId) {
		super(config, viewer, secondaryId);
		this.setText(Messages.getString("ExecuteSQLAction.ExecuteAllSQL")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ExecuteSQLAction.ExecuteAllSQLToolTip")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLExecuteActionCommand"); //$NON-NLS-1$
	}

	public void run() {
		try {
			executeSql(getAllSql());

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}
	}

}
