/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.ui.editors.internal.ColumnFilterInfo;
import zigen.plugin.db.ui.editors.internal.TableColumnFilterAction;

public class TableFilterJob extends AbstractJob {

	private ColumnFilterInfo[] filterInfo;

	private TableViewer tableViewer;

	public TableFilterJob(TableViewer tableViewer, ColumnFilterInfo[] filterInfo) {
		super(Messages.getString("TableFilterJob.0")); //$NON-NLS-1$
		this.filterInfo = filterInfo;
		this.tableViewer = tableViewer;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			showResults(new TableColumnFilterAction(tableViewer, filterInfo));

		} catch (Exception e) {
			showErrorMessage(Messages.getString("TableFilterJob.1"), e); //$NON-NLS-1$

		} finally {
			// updateResponseTime(getTotalTime());
		}
		return Status.OK_STATUS;

	}

}
