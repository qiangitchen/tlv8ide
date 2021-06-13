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
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.jobs.LoadColumnsJob;
import zigen.plugin.db.ui.jobs.OpenEditorJob;

public class LoadColumnAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public LoadColumnAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("LoadColumnAction.0")); //$NON-NLS-1$
		this.setEnabled(true);

	}

	public void run() {
		try {

			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object element = (Object) iter.next();
				if (element instanceof Folder) {
					Folder folder = (Folder) element;
					IDBConfig config = folder.getDbConfig();

					if (folder.getName().equalsIgnoreCase("TABLE")) { //$NON-NLS-1$

						TreeLeaf[] ts = folder.getChildrens();

						ITable[] tables = new ITable[ts.length];
						System.arraycopy(ts, 0, tables, 0, ts.length);

						LoadColumnsJob job = new LoadColumnsJob(viewer, config, tables);
						job.setPriority(OpenEditorJob.SHORT);
						job.setUser(true);
						job.schedule();
					}

				}
			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

}
