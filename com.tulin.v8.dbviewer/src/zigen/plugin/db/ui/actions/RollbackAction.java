/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class RollbackAction extends Action implements Runnable {

	private SQLExecuteView view;


	public RollbackAction(SQLExecuteView view) {

		this.setText(Messages.getString("RollbackAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RollbackAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_ROLLBACK));
		this.view = view;
	}

	public void run() {
		Transaction trans = null;
		try {

			trans = Transaction.getInstance(view.getConfig());
			if (!trans.isConneting()) {
				DbPlugin.getDefault().showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
				return;
			}

			int transCount = trans.getTransactionCount();
			trans.rollback();
			view.setStatusMessage(createMessage(transCount));

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}

	private String createMessage(int count) {
		return count + Messages.getString("RollbackAction.3"); //$NON-NLS-1$

	}

}
