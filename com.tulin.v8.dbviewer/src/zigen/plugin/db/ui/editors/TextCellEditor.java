/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import org.eclipse.swt.widgets.Table;

public class TextCellEditor extends org.eclipse.jface.viewers.TextCellEditor {

	private int columnIndex = 0;

	public TextCellEditor(Table table, int columnIndex) {
		super(table);
		this.columnIndex = columnIndex;
	}

	public Object getInputValue() {
		return doGetValue();
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
