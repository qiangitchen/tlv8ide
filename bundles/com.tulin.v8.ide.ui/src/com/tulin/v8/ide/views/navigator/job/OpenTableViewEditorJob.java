package com.tulin.v8.ide.views.navigator.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.tulin.v8.core.Sys;
import com.tulin.v8.ide.editors.data.DataEditor;
import com.tulin.v8.ide.editors.data.TableViewEditorInput;
import com.tulin.v8.ide.views.navigator.action.Messages;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.jobs.RefreshColumnJob;

public class OpenTableViewEditorJob extends RefreshColumnJob {
	public static final String JOB_NAME = OpenTableViewEditorJob.class.getName();
	private ITable table;

	public OpenTableViewEditorJob(TreeViewer viewer, ITable table) {
		super(viewer, table);
		this.table = table;
		super.setName(Messages.getString("ModelView.OpenTableView.1"));
	}

	private static Object lock = new Object();

	protected IStatus run(IProgressMonitor monitor) {
		try {
			synchronized (lock) {
				IDBConfig config = table.getDbConfig();
				Transaction.getInstance(config).getConnection();
				monitor.beginTask(Messages.getString("ModelView.OpenTableView.2"), 10);
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				showSyncResults(new ShowTableEditorAction(table));
				monitor.done();
			}
		} catch (Exception e) {
			showErrorMessage(Messages.getString("ModelView.OpenTableView.3"), e);
		}
		return Status.OK_STATUS;
	}

	protected class ShowTableEditorAction implements Runnable {
		ITable table;

		public ShowTableEditorAction(ITable table) {
			this.table = table;
		}

		public void run() {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (table instanceof Table) {
				try {
					TableViewEditorInput editorinput = new TableViewEditorInput(table.getDbConfig(), (Table) table);
					IDE.openEditor(page, editorinput, DataEditor.ID);
				} catch (Exception e) {
					Sys.packErrMsg(e.toString());
					e.printStackTrace();
				}
			} else {
				Sys.packErrMsg("类型不支持，Table View编辑器只能打开表或者试图.");
			}
		}
	}

}
