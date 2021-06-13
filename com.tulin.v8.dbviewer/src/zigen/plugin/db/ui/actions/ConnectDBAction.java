/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.jobs.ConnectDBJob;

public class ConnectDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public ConnectDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("ConnectDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ConnectDBAction.1")); //$NON-NLS-1$
		this.setEnabled(true);

	}

	public void run() {
		try {

			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object element = (Object) iter.next();
				if (element instanceof DataBase) {
					DataBase db = (DataBase) element;
					if (!db.isConnected()) {
						db.setConnected(true);
						ConnectDBJob job = new ConnectDBJob(viewer, db);
						job.setPriority(ConnectDBJob.SHORT);
						job.setUser(false);
						job.setSystem(false);
						job.schedule();
					} else {
					}
				}
			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

}
