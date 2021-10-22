/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.views.TreeContentProvider;

public class RemoveBookmarkFolderAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	public RemoveBookmarkFolderAction(StructuredViewer viewer) {

		this.viewer = viewer;
		this.setText(Messages.getString("RemoveBookmarkFolderAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RemoveBookmarkFolderAction.1")); //$NON-NLS-1$

	}

	public void run() {

		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		Iterator iter = selection.iterator();

		IContentProvider obj = viewer.getContentProvider();
		if (obj instanceof TreeContentProvider) {
			TreeContentProvider provider = (TreeContentProvider) obj;
			if (DbPlugin.getDefault().confirmDialog(Messages.getString("RemoveBookmarkFolderAction.2"))) { //$NON-NLS-1$
				while (iter.hasNext()) {
					Object object = iter.next();
					if (object instanceof BookmarkFolder) {
						BookmarkFolder folder = (BookmarkFolder) object;
						folder.getParent().removeChild(folder);
						viewer.refresh(folder.getParent());
					}

				}
			}

		}

	}

}
