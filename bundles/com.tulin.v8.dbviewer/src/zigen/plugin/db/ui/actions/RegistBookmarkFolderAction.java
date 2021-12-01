/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;

public class RegistBookmarkFolderAction extends Action implements Runnable {

	public class FolderNameValidator implements IInputValidator {

		final String name = Messages.getString("RegistBookmarkFolderAction.0"); //$NON-NLS-1$

		public String isValid(String str) {
			if (str == null || str.trim().equals(Messages.getString("RegistBookmarkFolderAction.1"))) { //$NON-NLS-1$
				return name + Messages.getString("RegistBookmarkFolderAction.2"); //$NON-NLS-1$
			}
			return null;
		}
	}

	TreeViewer viewer = null;

	public RegistBookmarkFolderAction(TreeViewer viewer) {

		this.viewer = viewer;
		this.setText(Messages.getString("RegistBookmarkFolderAction.3")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RegistBookmarkFolderAction.4")); //$NON-NLS-1$

	}

	public void run() {
		Shell shell = DbPlugin.getDefault().getShell();
		String name = ""; //$NON-NLS-1$

		InputDialog dialog = new InputDialog(shell,
				Messages.getString("RegistBookmarkFolderAction.6"), Messages.getString("RegistBookmarkFolderAction.7"), name, new FolderNameValidator()); //$NON-NLS-1$ //$NON-NLS-2$

		int rc = dialog.open();
		if (rc == InputDialog.CANCEL) {
			return;

		} else {

			Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();

			BookmarkFolder folder = new BookmarkFolder(dialog.getValue());

			if (element instanceof BookmarkRoot) {
				BookmarkRoot parent = (BookmarkRoot) element;
				parent.addChild(folder);
				viewer.expandToLevel(folder, 1);
				viewer.refresh(parent);

			} else if (element instanceof BookmarkFolder) {
				BookmarkFolder parent = (BookmarkFolder) element;
				parent.addChild(folder);
				viewer.expandToLevel(folder, 1);
				viewer.refresh(parent);
			}
		}

	}
}
