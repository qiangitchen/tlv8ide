/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.ui.editors.ITableViewEditor;

public class TableDefaultSortListener extends TableSortListener {

	public TableDefaultSortListener(ITableViewEditor editor, int columnIndex) {
		super(editor, columnIndex);
	}

	public void widgetSelected(SelectionEvent e) {
		TableColumn col = (TableColumn) e.widget;
		Table table = col.getParent();

		editor.getViewer().setSorter(new TableColumnSorter(columnIndex, false));
		editor.changeColumnColor();

		try {
			table.setSortDirection(SWT.NONE);
		} catch (Throwable ex) {
			;// NoSuchMethodException by Eclipse3.1
		}
	}

}
