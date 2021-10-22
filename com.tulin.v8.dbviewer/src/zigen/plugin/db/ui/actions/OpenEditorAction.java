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

import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.OpenEditorJob;

public class OpenEditorAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public OpenEditorAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("OpenEditorAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("OpenEditorAction.1")); //$NON-NLS-1$

	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (element instanceof ITable) {
				ITable table = (ITable) element;

				OpenEditorJob job = new OpenEditorJob(viewer, table);
				job.setPriority(OpenEditorJob.SHORT);
				job.setUser(false);
				job.schedule();

			} else {
				throw new IllegalStateException("Double-clicking by other elements"); //$NON-NLS-1$
			}
		}

	}

}
