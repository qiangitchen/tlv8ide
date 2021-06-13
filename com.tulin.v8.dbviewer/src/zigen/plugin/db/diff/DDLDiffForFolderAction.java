/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.Folder;

public class DDLDiffForFolderAction extends Action implements Runnable {

	private boolean showDialog = true;

	private TreeViewer viewer = null;

	private Folder left = null;

	private Folder right = null;

	public DDLDiffForFolderAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DDLDiffForFolderAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DDLDiffForFolderAction.1")); //$NON-NLS-1$
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof Folder) {
					Folder f = (Folder) obj;
					if (index == 0) {
						left = f;
						index++;
					} else if (index == 1) {
						right = f;
						index++;
					} else {
						break;
					}
				}
			}

			if (index == 2) {

				DDLDiffForFolderJob job = new DDLDiffForFolderJob(viewer, left, right);
				// job.setPriority(DDLDiffJob.SHORT);
				job.setPriority(DDLDiffForSchemaJob.SHORT);
				// job.setPriority(DDLDiffJob.DECORATE);
				job.setUser(showDialog);
				job.schedule();

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
