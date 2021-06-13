/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.event;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.ui.editors.ITableViewEditor;

public class TableSortListener extends SelectionAdapter {

	protected ImageCacher ic = ImageCacher.getInstance();

	protected ITableViewEditor editor = null;

	protected boolean desc = true;

	protected int columnIndex;

	protected TableColumn col;

	public TableSortListener(ITableViewEditor editor, int columnIndex) {
		this.editor = editor;
		this.columnIndex = columnIndex;
		this.col = editor.getViewer().getTable().getColumn(columnIndex);
	}

	public void widgetSelected(SelectionEvent e) {
		TableColumn col = (TableColumn) e.widget;
		Table table = col.getParent();
		if (!desc) {
			editor.getViewer().setSorter(new TableColumnSorter(columnIndex, desc));
			desc = true;
			try {
				table.setSortColumn(col);
				table.setSortDirection(SWT.UP);

			} catch (Throwable ex) {
				;// NoSuchMethodException by Eclipse3.1
			}

		} else {
			editor.getViewer().setSorter(new TableColumnSorter(columnIndex, desc));
			desc = false;

			try {
				table.setSortColumn(col);
				table.setSortDirection(SWT.DOWN);
			} catch (Throwable ex) {
				;// NoSuchMethodException by Eclipse3.1
			}

		}

		editor.changeColumnColor();
	}

	protected class TableColumnSorter extends ViewerSorter {

		boolean isDesc = false;

		int index;

		public TableColumnSorter(int index, boolean isDesc) {
			this.index = index;
			this.isDesc = isDesc;
		}

		public int compare(Viewer viewer, Object o1, Object o2) {

			TableElement first = (TableElement) o1;
			TableElement second = (TableElement) o2;

			if (first.isNew() && second.isNew()) {
				return 0;
			} else if (first.isNew()) {
				return 1;
			} else if (second.isNew()) {
				return -1;
			} else {

				if (index == 0) {
					int no1 = first.getRecordNo();
					int no2 = second.getRecordNo();

					if (no1 < no2) {
						if (isDesc) {
							return (1);
						} else {
							return (-1);
						}
					} else if (no1 > no2) {
						if (isDesc) {
							return (-1);
						} else {
							return (1);
						}
					} else {
						return (0);
					}

				} else {
					String v1 = (String) first.getItems()[index - 1];
					String v2 = (String) second.getItems()[index - 1];
					try {
						BigDecimal d1 = new BigDecimal(v1);
						BigDecimal d2 = new BigDecimal(v2);

						if (isDesc) {
							return (d2.compareTo(d1));
						} else {
							return (d1.compareTo(d2));
						}


					} catch (NumberFormatException ex) {
						if (isDesc) {
							return (v2.compareTo(v1));
						} else {
							return (v1.compareTo(v2));
						}

					}
				}

			}

		}

	}
}
