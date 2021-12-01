/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.editors.ITableViewEditor;


public abstract class TableViewEditorAction extends Action implements Runnable, ITableViewEditorAction {

	protected IStructuredSelection selection;

	public void selectionChanged(ISelection _selection) {
		if (_selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) _selection;
		} else {
			this.selection = null;
		}
	}

	protected ITableViewEditor editor;

	protected IDBConfig config;

	public void setActiveEditor(ITableViewEditor target) {
		if (target != null) {
			editor = target;
			config = target.getDBConfig();
		} else {
			editor = null;
			config = null;
		}
	}

	public void setImage(int operation) {
		String imageName = null;
		switch (operation) {
		case ITextOperationTarget.COPY:
			imageName = ISharedImages.IMG_TOOL_COPY;
			setText(Messages.getString("TableViewEditorAction.CopyRecord")); //$NON-NLS-1$
			setAccelerator(SWT.CTRL | 'C');
			break;
		case ITextOperationTarget.PASTE:
			imageName = ISharedImages.IMG_TOOL_PASTE;
			setText(Messages.getString("TableViewEditorAction.PasteRecord")); //$NON-NLS-1$
			setAccelerator(SWT.CTRL | 'V');
			break;
		case ITextOperationTarget.DELETE:
			imageName = org.eclipse.ui.ISharedImages.IMG_TOOL_DELETE;
			setText(Messages.getString("TableViewEditorAction.DeleteRecord")); //$NON-NLS-1$
			setAccelerator(SWT.DEL);
			break;
		case ITextOperationTarget.SELECT_ALL:
			setText(Messages.getString("TableViewEditorAction.SelectAll")); //$NON-NLS-1$
			setAccelerator(SWT.CTRL | 'A');
			imageName = null;
			break;
		default:
			break;
		}

		if (imageName != null) {
			setImageDescriptor(imageName);
		}

	}

	protected void setImageDescriptor(String imageName) {
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(imageName));
	}
}
