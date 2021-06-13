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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.ConfirmConnectDBAction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.TableViewEditorInput;
import zigen.plugin.db.ui.internal.ITable;

public class RecordCountForTableJob extends AbstractJob {

	private Transaction trans;

	private ITable table;

	int dispCount;

	String condition;

	boolean doCalculate = false;

	public RecordCountForTableJob(Transaction trans, ITable table, String condition, int dispCount, boolean doCalculate) {
		super(Messages.getString("RecordCountForTableJob.0")); //$NON-NLS-1$
		this.trans = trans;
		this.table = table;
		this.condition = condition;
		this.dispCount = dispCount;
		this.doCalculate = doCalculate;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {

			if (!doCalculate) {
				showResults(new SetTotalCountAction(dispCount));
				return Status.OK_STATUS;
			}


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

			int timeout = store.getInt(PreferencePage.P_QUERY_TIMEOUT_FOR_COUNT);

			IDBConfig config = table.getDbConfig();
			ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, null);
			String q = factory.createCountAll(condition);

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
			return Status.OK_STATUS;

		} catch (Exception e) {
			DbPlugin.log(e);
			showResults(new SetTotalCountAction(-1));

		} finally {
			// ConnectionManager.closeConnection(con);
		}
		return Status.OK_STATUS;

	}

	protected class SetTotalCountAction implements Runnable {

		long totalCount;

		public SetTotalCountAction(long totalCount) {
			this.totalCount = totalCount;
		}

		public void run() {
			try {
				IWorkbenchPage page = DbPlugin.getDefault().getPage();
				TableViewEditorInput input = new TableViewEditorInput(trans.getConfig(), table);

				IEditorPart editor = DbPlugin.getDefault().getPage().findEditor(input);
				if (editor instanceof TableViewEditorFor31) {
					TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;
					tEditor.setTotalCount(dispCount, totalCount);
				} else {
					DbPlugin.log("A corresponding editor is not found.");
				}
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}
}
