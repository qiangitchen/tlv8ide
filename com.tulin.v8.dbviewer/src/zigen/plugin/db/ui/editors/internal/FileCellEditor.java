/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FileCellEditor extends DialogCellEditor {

	Button result;

	boolean opened = false;

	public FileCellEditor(Composite parent) {
		super(parent);
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		return this;
	}

	public Object getInputValue() {
		return doGetValue();
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	protected Button createButton(Composite parent) {
		result = new Button(parent, SWT.DOWN);
		result.setText("..."); //$NON-NLS-1$
		return result;
	}

	public void addKeyListener(KeyListener listener) {
		result.addKeyListener(listener);
	}

	public void addTraverseListener(TraverseListener listener) {
		result.addTraverseListener(listener);
	}

}
