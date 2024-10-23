/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.BookmarkFolder;

public class RenameBookmarkFolderAction extends Action implements Runnable {

	private StructuredViewer viewer = null;

	public RenameBookmarkFolderAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("RenameBookmarkFolderAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RenameBookmarkFolderAction.1")); //$NON-NLS-1$
	}

	public void run() {

		Shell shell = DbPlugin.getDefault().getShell();
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();

		if (element instanceof BookmarkFolder) {
			BookmarkFolder folder = (BookmarkFolder) element;
			try {
				InputDialog dialog = new InputDialog(shell,
						Messages.getString("RenameBookmarkFolderAction.2"), Messages.getString("RenameBookmarkFolderAction.3"), folder.getName(), null); //$NON-NLS-1$ //$NON-NLS-2$
				int rc = dialog.open();
				if (rc == InputDialog.CANCEL) {
					return;
				}
				folder.setName(dialog.getValue());

				viewer.refresh(folder);

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			}
		}

	}

}
