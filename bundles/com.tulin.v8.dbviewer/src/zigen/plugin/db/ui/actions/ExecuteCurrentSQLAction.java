/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class ExecuteCurrentSQLAction extends AbstractExecuteSQLAction {

	public ExecuteCurrentSQLAction(IDBConfig config, SQLSourceViewer viewer, String secondaryId) {
		super(config, viewer, secondaryId);
		this.setText(Messages.getString("ExecuteCurrentSQLAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ExecuteCurrentSQLAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLCurrentExecuteActionCommand"); //$NON-NLS-1$
	}

	public void run() {
		try {
			String sql = getCurrentSql();
			executeSql(sql);
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}
}
