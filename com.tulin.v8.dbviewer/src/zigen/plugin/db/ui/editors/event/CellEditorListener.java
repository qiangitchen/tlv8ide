/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.event;

import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.TextCellEditor;

public class CellEditorListener implements ICellEditorListener {

	private TextCellEditor cellEditor;

	public CellEditorListener(TextCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public void applyEditorValue() {
		if (!cellEditor.isValueValid()) {
		}
	}

	public void cancelEditor() {
	}

	public void editorValueChanged(boolean oldValidState, boolean newValidState) {
	}

}
