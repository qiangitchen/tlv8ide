/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IViewPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ui.views.HistoryView;

public class SQLHistoryAddWithProgress implements IRunnableWithProgress {

	SQLHistoryManager mgr;

	SQLHistory history;

	public SQLHistoryAddWithProgress(SQLHistory history) {
		this.history = history;
		this.mgr = DbPlugin.getDefault().getHistoryManager();

	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.beginTask("Add SQL History...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
		try {
			if (mgr.addHistory(history)) {
				IViewPart part = DbPlugin.findView(DbPluginConstant.VIEW_ID_HistoryView);
				if (part instanceof HistoryView) {
					HistoryView hv = (HistoryView) part;
					hv.updateHistoryView(history);
					DbPlugin.fireStatusChangeListener(hv, IStatusChangeListener.EVT_UpdateHistory);
				}
			}
		} catch (IOException e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} finally {
			monitor.done();
		}
	}


}
