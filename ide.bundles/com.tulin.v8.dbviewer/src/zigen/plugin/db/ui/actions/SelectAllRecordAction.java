/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPlugin;

public class SelectAllRecordAction extends TableViewEditorAction {

	protected IStructuredSelection selection;

	public SelectAllRecordAction() {
		super();
		setText(Messages.getString("SelectAllRecordAction.0")); //$NON-NLS-1$
		setToolTipText(Messages.getString("SelectAllRecordAction.1")); //$NON-NLS-1$
		// setAccelerator(SWT.CTRL | 'A');
	}

	public void run() {
		try {
			TableViewer viewer = editor.getViewer();
			viewer.getTable().selectAll();
			viewer.getTable().notifyListeners(SWT.Selection, null);
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}
}
