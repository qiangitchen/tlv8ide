/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.jface.viewers.TableViewer;

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
import zigen.plugin.db.ui.editors.exceptions.UpdateException;
import zigen.plugin.db.ui.internal.ITable;

public class RecordUpdateThread implements Runnable {

	private ITableViewEditor editor;

	private TableViewer viewer;

	private TableElement element;

	private ITable table;

	public RecordUpdateThread(ITableViewEditor editor, TableElement element) {
		this.editor = editor;
		this.viewer = editor.getViewer();
		this.element = element;
		this.table = element.getTable();
	}

	public void run() {
		if (element.isModify()) {
			TransactionForTableEditor trans = null;
			int rowAffected = 0;
			try {

				trans = TransactionForTableEditor.getInstance(table.getDbConfig());

				if (element.isNew()) {
					rowAffected = insert(trans.getConnection());
				} else {
					rowAffected = update(trans.getConnection());
				}
				trans.commit();

				if (rowAffected == 0) {
					SQLException ex = new SQLException(Messages.getString("RecordUpdateThread.2")); //$NON-NLS-1$
					// throw new ZeroUpdateException("");
					throw ex;
				}

			} catch (Exception e) {
				if (trans != null) {
					trans.rollback();
				}
				if (!PasteRecordMonitor.isPasting()) {
					DbPlugin.getDefault().showErrorDialog(e);
				}
				throw new UpdateException(Messages.getString("RecordUpdateThread.0"), e.getCause()); //$NON-NLS-1$
			}

		}


	}

	private int insert(Connection con) throws Exception {
		// String sql = SQLCreator.createInsertSql(table, element.getColumns(), element.getItems());

		int rowAffected = 0;
		try {

			rowAffected = InsertSQLInvoker.invoke(con, table, element.getColumns(), element.getItems());

			if (rowAffected > 0) {
				// TableEditorLogUtil.successLog(sql);
			} else {
				// TableEditorLogUtil.failureLog(sql);
			}

			TableElement updatedElem = TableElementSearcher.findElement(con, element, true);

			if (updatedElem != null) {
				TableViewerManager.update(viewer, element, updatedElem);
			} else {
				;
			}
		} catch (Exception e) {
			// System.err.println(e.getMessage());
			// e.printStackTrace();
			// TableEditorLogUtil.failureLog(sql);
			throw e;
		}

		return rowAffected;
	}

	private int update(Connection con) throws Exception {

		// String sql = SQLCreator.createUpdateSql(table, element.getModifiedColumns(), element.getModifiedItems(), element.getUniqueColumns(), element.getUniqueItems());

		int rowAffected = 0;

		try {
			TimeWatcher tw = new TimeWatcher();
			tw.start();
			rowAffected = UpdateSQLInvoker.invoke(con, table, element.getModifiedColumns(), element.getModifiedItems(), element.getUniqueColumns(), element.getUniqueItems());
			tw.stop();

			StringBuffer sb = new StringBuffer();

			if (rowAffected == 0) {
				// TableEditorLogUtil.failureLog(sql);
				sb.append(Messages.getString("RecordUpdateThread.3")); //$NON-NLS-1$
				sb.append(Messages.getString("RecordUpdateThread.4")); //$NON-NLS-1$
				DbPlugin.getDefault().showWarningMessage(sb.toString());

			} else if (rowAffected == 1) {
				tw.start();
				// TableEditorLogUtil.successLog(sql);
				tw.stop();
				tw.start();
				TableElement updatedElem = TableElementSearcher.findElement(con, element, true);
				tw.stop();

				tw.start();
				if (updatedElem != null) {
					TableViewerManager.update(viewer, element, updatedElem);
				} else {
					sb.append(Messages.getString("RecordUpdateThread.5")); //$NON-NLS-1$
					sb.append(Messages.getString("RecordUpdateThread.6")); //$NON-NLS-1$
					DbPlugin.getDefault().showWarningMessage(sb.toString());
				}
				tw.stop();

			} else {
				// TableEditorLogUtil.successLog(sql);
				sb.append(Messages.getString("RecordUpdateThread.7") + rowAffected + Messages.getString("RecordUpdateThread.8")); //$NON-NLS-1$ //$NON-NLS-2$
				DbPlugin.getDefault().showWarningMessage(sb.toString());
				reload(con, table);
			}
		} catch (Exception e) {
			// System.err.println(e.getMessage());
			// TableEditorLogUtil.failureLog(sql);
			throw e;
		}
		return rowAffected;
	}

	private void reload(Connection con, ITable table) throws Exception {

		TimeWatcher tw = new TimeWatcher();
		tw.start();

		TableElement[] elements;
		try {
			elements = TableManager.invoke(con, table, editor.getCondition());
			viewer.setInput(elements);

		} catch (MaxRecordException e) {
			viewer.setInput(e.getTableElements());
			DbPlugin.getDefault().showWarningMessage(e.getMessage());
		}

		viewer.refresh();
		tw.stop();
	}
}
