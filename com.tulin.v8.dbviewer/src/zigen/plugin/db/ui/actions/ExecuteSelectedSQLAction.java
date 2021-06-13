/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.text.TextSelection;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class ExecuteSelectedSQLAction extends AbstractExecuteSQLAction {


	private TextSelection selection;

	public ExecuteSelectedSQLAction(IDBConfig config, SQLSourceViewer viewer, String secondaryId, TextSelection selection) {
		super(config, viewer, secondaryId);
		this.selection = selection;
		this.setText(Messages.getString("ExecuteSelectedSQLAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ExecuteSelectedSQLAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLSelectedExecuteActionCommand"); //$NON-NLS-1$
	}

	public void run() {

		try {
			if (selection != null && !"".equals(selection.getText())) { //$NON-NLS-1$
				executeSql(selection.getText());
			} else {
				DbPlugin.getDefault().showWarningMessage(Messages.getString("ExecuteSelectedSQLAction.4")); //$NON-NLS-1$

			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}
	}

}
