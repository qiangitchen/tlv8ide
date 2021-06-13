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
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.ui.editors.exceptions.NotFoundSynonymInfoException;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.views.TreeContentProvider;

public class RefreshColumnJob extends AbstractLoadColumnJob {

	private static Object lock = new Object();

	protected TreeViewer treeViewer;

	protected ITable table;

	protected TablePKColumn[] pks = null;

	protected TableFKColumn[] fks = null;

	protected TableConstraintColumn[] cons = null;

	protected TableIDXColumn[] uidxs = null;

	protected TableIDXColumn[] nonuidxs = null;

	public RefreshColumnJob(TreeViewer viewer, ITable table) {
		super(Messages.getString("RefreshColumnJob.0")); //$NON-NLS-1$
		this.treeViewer = viewer;
		this.table = table;
	}

	protected IStatus run(IProgressMonitor monitor) {
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		Connection con = null;
		IDBConfig config = table.getDbConfig();
		try {
			synchronized (table) {
				monitor.beginTask(Messages.getString("RefreshColumnJob.6"), 6); //$NON-NLS-1$
				// Connection con =
				// Transaction.getInstance(config).getConnection();
				con = ConnectionManager.getConnection(config);

				monitor.beginTask(Messages.getString("RefreshColumnJob.5"), 6); //$NON-NLS-1$

				if (!loadColumnInfo(monitor, con, table)) {
					table.setExpanded(false);
					return Status.CANCEL_STATUS;
				}

				table.setExpanded(true);
				showResults(new RefreshTreeNodeAction(treeViewer, table));
			}

		} catch (NotFoundSynonymInfoException e) {
			table.setEnabled(false);
			table.removeChildAll();
			showResults(new RefreshTreeNodeAction(treeViewer, table));
			showErrorMessage(Messages.getString("RefreshColumnJob.1"), e); //$NON-NLS-1$

		} catch (Exception e) {
			table.setExpanded(false);
			showErrorMessage(Messages.getString("RefreshColumnJob.2"), e); //$NON-NLS-1$

		} finally {
			ConnectionManager.closeConnection(con);
		}
		tw.stop();
		return Status.OK_STATUS;
	}

	public DataBase findDataBase(Bookmark bookmark) {
		if (treeViewer != null) {
			IContentProvider cp = treeViewer.getContentProvider();
			if (cp instanceof TreeContentProvider) {
				TreeContentProvider tcp = (TreeContentProvider) cp;
				return tcp.findDataBase(bookmark);
			}
		}
		return null;

	}
}
