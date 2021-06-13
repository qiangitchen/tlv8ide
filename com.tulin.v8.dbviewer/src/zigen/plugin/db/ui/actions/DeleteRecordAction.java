/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.sql.Connection;
import java.util.Iterator;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DeleteSQLInvoker;
import zigen.plugin.db.core.SQLCreator;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.RecordCountForTableJob;

public class DeleteRecordAction extends TableViewEditorAction {

	protected IStructuredSelection selection;

	public DeleteRecordAction() {
		setEnabled(false);
		setImage(ITextOperationTarget.DELETE);
	}

	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (editor.getViewer() == null) {
			setEnabled(false);
		} else {
			selection = (IStructuredSelection) editor.getViewer().getSelection();
			if (selection.size() > 0) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	public void run() {
		// Connection con = null;
		int rowAffected = 0;
		try {

			TimeWatcher tw = new TimeWatcher();
			tw.start();
			ITable table = editor.getTableNode();

			TransactionForTableEditor trans = TransactionForTableEditor.getInstance(table.getDbConfig());
			Connection con = trans.getConnection();

			IStructuredSelection selection = (IStructuredSelection) editor.getViewer().getSelection();

			Iterator iter = selection.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TableElement) {
					TableElement elem = (TableElement) obj;

					if (elem.isNew()) {
						rowAffected++;

					} else {

						TableColumn[] uniqueColumns = elem.getUniqueColumns();
						Object[] uniqueItems = elem.getUniqueItems();

						//String sql = SQLCreator.createDeleteSql(table, uniqueColumns, uniqueItems);
						int row = DeleteSQLInvoker.invoke(con, table, uniqueColumns, uniqueItems);
						rowAffected += row;

					}
				}
			}
			tw.stop();

			if (DbPlugin.getDefault().confirmDialog(rowAffected + Messages.getString("DeleteRecordAction.0"))) { //$NON-NLS-1$
				trans.commit();
				removeElement(selection);

			} else {
				trans.rollback();
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}

	private void removeElement(IStructuredSelection selection) {
		ITableViewEditor editor = DbPlugin.getActiveTableViewEditor();
		if (editor != null) {
			TableViewer viewer = editor.getViewer();

			TableViewerManager.remove(viewer, selection.toArray());

			TableElement[] elements = (TableElement[]) viewer.getInput();
			int dispCnt = elements.length - 1;
			editor.setTotalCount(dispCnt, -1); //$NON-NLS-1$
			ITable tableNode = editor.getTableNode();
			String condition = editor.getCondition();
			RecordCountForTableJob job2 = new RecordCountForTableJob(Transaction.getInstance(config), tableNode, condition, dispCnt, true);
			job2.setUser(false);
			job2.schedule();

		} else {
			throw new IllegalStateException("TableViewer not found"); //$NON-NLS-1$
		}

	}

}
