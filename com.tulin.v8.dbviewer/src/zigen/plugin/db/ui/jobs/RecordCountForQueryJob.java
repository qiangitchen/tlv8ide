/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.ConfirmConnectDBAction;
import zigen.plugin.db.ui.editors.QueryViewEditor2;
import zigen.plugin.db.ui.editors.QueryViewEditorInput;

public class RecordCountForQueryJob extends AbstractJob {

	int timeoutSec = 5;

	private Transaction trans;

	private String sqlString;

	String secondarlyId;

	int dispCount;

	public RecordCountForQueryJob(Transaction trans, String sqlString, String secondarlyId, int dispCount) {
		super(Messages.getString("RecordCountForQueryJob.0")); //$NON-NLS-1$
		this.trans = trans;
		this.sqlString = sqlString;
		this.secondarlyId = secondarlyId;
		this.dispCount = dispCount;
	}

	protected IStatus run(IProgressMonitor monitor) {
		// long count = 0;
		try {
			if (!trans.isConneting()) {
				Display.getDefault().syncExec(new ConfirmConnectDBAction(trans));
				if (!trans.isConneting()) {
					showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
					return Status.CANCEL_STATUS;
				}
			}

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			TimeWatcher tw = new TimeWatcher();
			tw.start();

			int timeout = store.getInt(PreferencePage.P_QUERY_TIMEOUT_FOR_COUNT);

			ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(trans.getConfig(), null);
			String q = factory.createCountForQuery(sqlString);

			TotalRecordCountSearchThread t = new TotalRecordCountSearchThread(trans, q, timeout);
			Thread th = new Thread(t);
			th.start();

			if (timeout > 0) {
				th.join(timeout * 1000);
			} else {
				th.join();
			}
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			if (t.isComplete) {
				showResults(new SetTotalCountAction(t.count));
			} else {
				showResults(new SetTotalCountAction(-1));
			}

			tw.stop();
		} catch (Exception e) {
			DbPlugin.log(e);
			showResults(new SetTotalCountAction(-1));

		} finally {
		}
		return Status.OK_STATUS;

	}

	protected class SetTotalCountAction implements Runnable {

		long count;

		public SetTotalCountAction(long count) {
			this.count = count;
		}

		public void run() {
			try {

				IWorkbenchPage page = DbPlugin.getDefault().getPage();
				QueryViewEditorInput input = new QueryViewEditorInput(trans.getConfig(), sqlString, secondarlyId);
				IEditorPart editor = IDE.openEditor(page, input, DbPluginConstant.EDITOR_ID_QueryView2, false);
				if (editor instanceof QueryViewEditor2) {
					QueryViewEditor2 tEditor = (QueryViewEditor2) editor;
					tEditor.setTotalCount(dispCount, count);

				}
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}
}
