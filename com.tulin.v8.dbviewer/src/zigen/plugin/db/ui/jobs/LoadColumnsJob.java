/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.ui.editors.exceptions.NotFoundSynonymInfoException;
import zigen.plugin.db.ui.internal.ITable;

public class LoadColumnsJob extends AbstractLoadColumnJob {

	TreeViewer viewer;

	ITable[] tables;

	IDBConfig config;

	int total = 0;

	StringBuffer err = new StringBuffer();

	public LoadColumnsJob(TreeViewer viewer, IDBConfig config, ITable[] tables) {
		super("Loading All column information...");
		this.viewer = viewer;
		this.tables = tables;
		this.config = config;
		if (tables != null) {
			this.total = tables.length * 6;
		}
	}

	protected IStatus run(IProgressMonitor monitor) {
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		Connection con = null;
		try {
			con = ConnectionManager.getConnection(config);

			monitor.beginTask("Refresh Column...", total);

			for (int i = 0; i < tables.length; i++) {
				ITable table = tables[i];
				if (!table.isExpanded()) {
					if (DBType.getType(config) == DBType.DB_TYPE_ORACLE && config.getDataBaseProductMajorVersion() == 10) {
						if (!table.getName().startsWith("BIN$")) { //$NON-NLS-1$
							loadTable(monitor, con, table, total);
						} else {
							monitor.worked(1);
							continue;
						}
					} else {
						loadTable(monitor, con, table, total);

					}
				}

			}

		} catch (InterruptedException e) {
			return Status.CANCEL_STATUS;

		} catch (Exception e) {
			showErrorMessage(Messages.getString("RefreshColumnJob.2"), e);// //$NON-NLS-1$
			return Status.CANCEL_STATUS;

		} finally {
			ConnectionManager.closeConnection(con);
			monitor.done();

		}
		tw.stop();
		return Status.OK_STATUS;
	}

	private void loadTable(IProgressMonitor monitor, Connection con, ITable table, int total) throws Exception {
		try {
			monitor.subTask(Messages.getString("LoadColumnsJob.3") + table.getSqlTableName()); //$NON-NLS-1$
			synchronized (table) {
				con = ConnectionManager.getConnection(config);
				if (!loadColumnInfo(monitor, con, table)) {
					table.setExpanded(false);
					throw new InterruptedException();
				}

				table.setExpanded(true);
				showResults(new RefreshTreeNodeAction(viewer, table, RefreshTreeNodeAction.MODE_EXPAND));
			}

		} catch (NotFoundSynonymInfoException e) {
			table.setEnabled(false);
			table.removeChildAll();
			showResults(new RefreshTreeNodeAction(viewer, table));
			// showErrorMessage(Messages.getString("RefreshColumnJob.1"), e);// //$NON-NLS-1$
			throw e;

		} catch (Exception e) {
			table.setExpanded(false);
			// showErrorMessage(Messages.getString("RefreshColumnJob.2"), e);// //$NON-NLS-1$
			throw e;
		}
	}
}
