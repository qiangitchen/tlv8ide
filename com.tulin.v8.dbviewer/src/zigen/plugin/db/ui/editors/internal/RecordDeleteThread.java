/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import java.sql.Connection;
import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DeleteSQLInvoker;
import zigen.plugin.db.core.SQLCreator;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.internal.ITable;

public class RecordDeleteThread implements Runnable {

	private ITableViewEditor editor;

	private TableViewer viewer;

	private ITable table;

	private RecordDeleteThread(ITableViewEditor editor, ITable table) {
		this.editor = editor;
		this.viewer = editor.getViewer();
		this.table = table;
	}

	public void run() {
		TransactionForTableEditor trans = null;
		int rowAffected = 0;
		try {
			trans = TransactionForTableEditor.getInstance(table.getDbConfig());

			rowAffected = delete(trans.getConnection());

			if (DbPlugin.getDefault().confirmDialog(rowAffected + Messages.getString("RecordDeleteThread.0"))) { //$NON-NLS-1$
				trans.commit();
				removeElement();
			} else {
				trans.rollback();
			}
			editor.changeColumnColor();

		} catch (Exception e) {
			if (trans != null) {
				trans.rollback();
			}
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}

	private int delete(Connection con) throws Exception {
		int rowAffected = 0;
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof TableElement) {
				TableElement elem = (TableElement) obj;
				TableColumn[] uniqueColumns = elem.getUniqueColumns();
				Object[] uniqueItems = elem.getUniqueItems();

				String sql = SQLCreator.createDeleteSql(table, uniqueColumns, uniqueItems);

				int row = DeleteSQLInvoker.invoke(con, table, uniqueColumns, uniqueItems);
				if (row > 0) {
					TableEditorLogUtil.successLog(sql);
				} else {
					TableEditorLogUtil.failureLog(sql);
				}
				rowAffected += row;

			}
		}
		return rowAffected;
	}

	private void removeElement() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof TableElement) {
				TableViewerManager.remove(viewer, (TableElement) obj);
			}
		}

	}
}
