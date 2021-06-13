/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.sql.Connection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;

public class PurgeRecyclebinAction extends Action implements Runnable {

	public static final String SQL = "PURGE RECYCLEBIN"; //$NON-NLS-1$

	private TreeViewer viewer = null;

	public PurgeRecyclebinAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("PurgeRecyclebinAction.1")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("PurgeRecyclebinAction.2")); //$NON-NLS-1$
		// this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPluginConstant.IMG_CODE_EXECUTE));

	}

	public void run() {
		Connection con = null;
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof Schema) {
			Schema schema = (Schema) element;
			IDBConfig config = schema.getDbConfig();
			try {
				if (DbPlugin.getDefault().confirmDialog(Messages.getString("PurgeRecyclebinAction.3"))) { //$NON-NLS-1$
					con = ConnectionManager.getConnection(config);
					SQLInvoker.executeUpdate(con, SQL);

					TreeLeaf leaf = schema.getChild("TABLE"); //$NON-NLS-1$
					if (leaf instanceof Folder) {
						RefreshFolderJob job = new RefreshFolderJob(viewer, (Folder) leaf);
						job.setPriority(RefreshFolderJob.SHORT);
						job.schedule();
					}
				}

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			} finally {
				ConnectionManager.closeConnection(con);
			}
		}

	}

}
