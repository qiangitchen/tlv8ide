/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PartInitException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.PluginSettingsManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.views.TreeContentProvider;
import zigen.plugin.db.ui.views.TreeView;

public class ConfirmConnectDBAction extends Action implements Runnable {

	PluginSettingsManager pluginSettingsManager = DbPlugin.getDefault().getPluginSettingsManager();

	Transaction trans = null;

	IPreferenceStore store;

	public ConfirmConnectDBAction(Transaction trans) {
		this.trans = trans;
		this.store = DbPlugin.getDefault().getPreferenceStore();
	}

	public void run() {
		try {
			IDBConfig config = trans.getConfig();

			boolean b = store.getBoolean(PreferencePage.P_NO_CONFIRM_CONNECT_DB);
			if (b) {
				connect(config);
			} else {
				StringBuffer sb = new StringBuffer();
				sb.append(config.getDbName());
				sb.append(Messages.getString("ConfirmConnectDBAction.1")); //$NON-NLS-1$
				String msg = sb.toString();
				String opt = Messages.getString("ConfirmConnectDBAction.0"); //$NON-NLS-1$
				MessageDialogWithToggle dialog = DbPlugin.getDefault().confirmDialogWithToggle(msg, opt, false);
				final int YES = 2;
				if (dialog.getReturnCode() == YES) {
					store.setValue(PreferencePage.P_NO_CONFIRM_CONNECT_DB, dialog.getToggleState());
					connect(config);
				}
			}


		} catch (PartInitException e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private void connect(IDBConfig config) throws PartInitException {
		TreeView view = (TreeView) DbPlugin.findView(DbPluginConstant.VIEW_ID_TreeView);
		if (view == null) {
			view = (TreeView) DbPlugin.showView(DbPluginConstant.VIEW_ID_TreeView);
		}

		TreeContentProvider cp = view.getContentProvider();
		Root root = cp.getRoot();
		TreeLeaf node = root.getChild(config.getDbName());
		if (node != null && node instanceof DataBase) {
			DataBase db = (DataBase) node;
			db.setConnected(true);
			ConnectDBJob job = new ConnectDBJob(view.getTreeViewer(), db);
			job.setPriority(ConnectDBJob.SHORT);
			job.setUser(false);
			job.setSystem(false);
			job.schedule();
			try {
				job.join();
			} catch (InterruptedException e) {
				DbPlugin.log(e);
			}


		}
	}

}
