/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.views.TreeView;

public class ChangeTransactionIsolationLevelAction implements Runnable {

	private IDBConfig config;

	public ChangeTransactionIsolationLevelAction(IDBConfig config) {
		this.config = config;
	}

	public void setMessageForTreeView(String message) {
		TreeView view = (TreeView) DbPlugin.findView(DbPluginConstant.VIEW_ID_TreeView);
		if (view != null) {
			view.setStatusMessage(config, message);
		}
	}

	public void run() {
		try {
			Transaction trans = Transaction.getInstance(config);
			Connection con = trans.getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			switch (con.getTransactionIsolation()) {
			case Connection.TRANSACTION_READ_COMMITTED:
				break;

			case Connection.TRANSACTION_NONE:
				DbPlugin.getDefault().showWarningMessage(Messages.getString("ChangeTransactionIsolationLevelAction.0")); //$NON-NLS-1$
				break;

			case Connection.TRANSACTION_READ_UNCOMMITTED:
				if (metaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)) {
					con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
					StringBuffer sb = new StringBuffer();
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.1")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.2")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.3")); //$NON-NLS-1$
					// DbPlugin.getDefault().showInformationMessage(sb.toString());
					setMessageForTreeView(sb.toString());

				} else {
					StringBuffer sb = new StringBuffer();
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.4")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.5")); //$NON-NLS-1$
					DbPlugin.getDefault().showWarningMessage(sb.toString());
				}
				break;
			case Connection.TRANSACTION_REPEATABLE_READ:
				if (metaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)) {
					con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
					StringBuffer sb = new StringBuffer();
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.6")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.7")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.8")); //$NON-NLS-1$
					// DbPlugin.getDefault().showInformationMessage(sb.toString());
					setMessageForTreeView(sb.toString());

				} else {
					StringBuffer sb = new StringBuffer();
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.9")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.10")); //$NON-NLS-1$
					DbPlugin.getDefault().showWarningMessage(sb.toString());
				}
				break;
			case Connection.TRANSACTION_SERIALIZABLE:
				if (metaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)) {
					con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
					StringBuffer sb = new StringBuffer();
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.11")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.12")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.13")); //$NON-NLS-1$
					// DbPlugin.getDefault().showInformationMessage(sb.toString());
					setMessageForTreeView(sb.toString());

				} else {
					StringBuffer sb = new StringBuffer();
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.14")); //$NON-NLS-1$
					sb.append(Messages.getString("ChangeTransactionIsolationLevelAction.15")); //$NON-NLS-1$
					DbPlugin.getDefault().showWarningMessage(sb.toString());
				}
				break;

			default:
				DbPlugin.getDefault().showWarningMessage(Messages.getString("ChangeTransactionIsolationLevelAction.16")); //$NON-NLS-1$
				break;
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}
}
