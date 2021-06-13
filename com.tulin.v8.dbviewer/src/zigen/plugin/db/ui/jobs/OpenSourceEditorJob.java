/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.ext.oracle.internal.OpenSourceEdirotAction;

public class OpenSourceEditorJob extends AbstractJob {

	private StructuredViewer viewer;

	public OpenSourceEditorJob(StructuredViewer viewer) {
		super(Messages.getString("OpenSourceEditorJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Open Editor...", 10);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			showResults(new OpenSourceEdirotAction(viewer));

			monitor.done();
			return Status.OK_STATUS;

		} catch (Exception e) {
			showErrorMessage(Messages.getString("OpenSourceEditorJob.1"), e); //$NON-NLS-1$

		}

		return Status.OK_STATUS;
	}

}
