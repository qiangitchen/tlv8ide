/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLHistory;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class PrevSQLExecAction extends Action implements Runnable {

	SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();

	private SQLExecuteView view;

	public PrevSQLExecAction(SQLExecuteView view) {

		this.setText(Messages.getString("PrevSQLExecAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("PrevSQLExecAction.1")); //$NON-NLS-1$
		this.setActionDefinitionId("zigen.plugin.SQLBackCommand"); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_BACK));
		this.view = view;
	}

	public void run() {

		SQLHistory history = mgr.prevHisotry();

		if (history != null) {
			String sql = mgr.loadContents(history);
			view.getSqlViewer().getDocument().set(sql);
			view.getSqlViewer().invalidateTextPresentation();
		} else {
			view.getSqlViewer().getDocument().set(""); //$NON-NLS-1$
		}
		view.updateHistoryButton();

	}
}
