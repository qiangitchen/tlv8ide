/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.InsertSQLInvoker;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableElementSearcher;
import zigen.plugin.db.core.TableManager;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.core.UpdateSQLInvoker;
import zigen.plugin.db.ui.actions.MaxRecordException;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.event.PasteRecordMonitor;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

public class RecordUpdateJob extends AbstractJob {

	public static final String JOB_NAME = RecordUpdateJob.class.getName();

	private ITableViewEditor editor;

	private TableViewer viewer;

	private TableElement element;

	private ITable table;

	private TransactionForTableEditor trans = null;

	public RecordUpdateJob(ITableViewEditor editor) {
		super(Messages.getString("RecordSearchJob.0")); //$NON-NLS-1$
		this.editor = editor;
		this.viewer = editor.getViewer();
		this.table = editor.getTableNode();
		this.trans = TransactionForTableEditor.getInstance(table.getDbConfig());
	}

	public void setTargetTableElement(TableElement element) {
		this.element = element;
	}

	protected IStatus run(IProgressMonitor monitor) {
		Connection con;

		if (element.isModify()) {
			int rowAffected = 0;
			try {
				TimeWatcher tw = new TimeWatcher();
				tw.start();
				con = trans.getConnection();

				if (element.isNew()) {
					rowAffected = InsertSQLInvoker.invoke(con, table, element.getColumns(), element.getItems());
				} else {
					rowAffected = UpdateSQLInvoker.invoke(con, table, element.getModifiedColumns(), element.getModifiedItems(), element.getUniqueColumns(), element
							.getUniqueItems());
				}
				tw.stop();


				trans.commit();
				tw.start();

				if (rowAffected < 1) {
					showWarningMessage(Messages.getString("RecordUpdateJob.0")); //$NON-NLS-1$
				} else if (rowAffected == 1) {
					// showResults(new RefreshElementAction(con, true));
					Display.getDefault().syncExec(new RefreshElementAction(con, true));
				} else {
					// showResults(new RefreshAllAction(con, element.getTable()));
					Display.getDefault().syncExec(new RefreshAllAction(con, element.getTable()));
				}

				tw.stop();

			} catch (SQLException e) {
				showWarningMessage(e.getMessage());
			} catch (Exception e) {
				if (trans != null) {
					trans.rollback();
				}
				if (!PasteRecordMonitor.isPasting()) {
					DbPlugin.getDefault().showErrorDialog(e);
				}
				showErrorMessage(Messages.getString("RecordSearchJob.1"), e); //$NON-NLS-1$
			}

		}

		return Status.OK_STATUS;
	}

	class RefreshElementAction implements Runnable {

		Connection con;

		boolean isNew;

		public RefreshElementAction(Connection con, boolean isNew) {
			this.con = con;
			this.isNew = isNew;
		}

		public void run() {
			try {

				TableElement updatedElem = TableElementSearcher.findElement(con, element, isNew);

				if (updatedElem != null) {
					TableViewerManager.update(viewer, element, updatedElem);
				}

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}

	class RefreshAllAction implements Runnable {

		Connection con;

		ITable table;

		public RefreshAllAction(Connection con, ITable table) {
			this.con = con;
			this.table = table;
		}

		public void run() {
			try {

				TableElement[] elements;
				try {
					elements = TableManager.invoke(con, table, null);
					viewer.setInput(elements);

				} catch (MaxRecordException e) {
					viewer.setInput(e.getTableElements());
					DbPlugin.getDefault().showWarningMessage(e.getMessage());
				}

				viewer.refresh();

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}

}
