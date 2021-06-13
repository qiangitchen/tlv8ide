/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.event.TableKeyAdapter;
import zigen.plugin.db.ui.editors.event.TableKeyEventHandler;

public class PasteRecordDataAction extends TableViewEditorAction implements IWorkbenchWindowActionDelegate {

	public PasteRecordDataAction() {
		setImage(ITextOperationTarget.PASTE);
		setEnabled(false);

	}

	public void run() {
		try {
			TableKeyEventHandler handler = new TableKeyEventHandler((TableViewEditorFor31) editor);

			TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);
			keyAdapter.createNewElement();

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {

			editor.getViewer().getControl().notifyListeners(SWT.Selection, null);
		}

	}

	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (editor.getViewer() == null) {
			setEnabled(false);
		} else {
			TableKeyEventHandler handler = new TableKeyEventHandler((TableViewEditorFor31) editor);
			TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);

			boolean canPaste = keyAdapter.canCreateNewElement();
			setEnabled(canPaste);

		}
	}

	protected IWorkbenchWindow window;

	public void selectionChanged(IAction action, ISelection selection) {}

	public void run(IAction action) {}

	public void dispose() {}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
