/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.ui.internal.DataBase;

public class DisconnectDBJob extends AbstractJob {

	private TreeViewer viewer;
	private DataBase db;
	public DisconnectDBJob(TreeViewer viewer, DataBase db) {		
		super("Disconnect Database");
		this.viewer = viewer;
		this.db = db;

	}

	protected IStatus run(IProgressMonitor monitor) {
		try {						
			db.setEnabled(false);			
			
			Transaction trans = Transaction.getInstance(db.getDbConfig());
			trans.cloesConnection();

			TransactionForTableEditor trans2 = TransactionForTableEditor.getInstance(db.getDbConfig());
			trans2.cloesConnection();


			if (DBType.getType(db.getDbConfig()) == DBType.DB_TYPE_DERBY) {
				shutdown(db.getDbConfig());
			}
						
			db.removeChildAll();			
			db.setConnected(false);
			db.setExpanded(false);
			db.setEnabled(true);			
			
			showResults(new RefreshTreeNodeAction(viewer, db, RefreshTreeNodeAction.MODE_NOTHING));
			
		} catch (Exception e) {

			showErrorMessage("Failed disconnect database", e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;

	}
	

	private void shutdown(IDBConfig config) {
		try {
			ConnectionManager.shutdown(config);

		} catch (SQLException e) {

			if (DBType.getType(config) == DBType.DB_TYPE_DERBY) {
				if (e.getErrorCode() == 50000) {
					showMessage(null, e.getMessage());
					return;
				}
			}
			showErrorMessage(e.getMessage(), e);

		} catch (Exception e) {

			showErrorMessage(e.getMessage(), e);
		}
	}
}
