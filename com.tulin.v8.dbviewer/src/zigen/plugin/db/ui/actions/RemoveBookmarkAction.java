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
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.views.TreeContentProvider;

public class RemoveBookmarkAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	public RemoveBookmarkAction(StructuredViewer viewer) {

		this.viewer = viewer;
		this.setText(Messages.getString("RemoveBookmarkAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RemoveBookmarkAction.1")); //$NON-NLS-1$

	}

	public void run() {

		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		Iterator iter = selection.iterator();

		IContentProvider obj = viewer.getContentProvider();
		if (obj instanceof TreeContentProvider) {
			TreeContentProvider provider = (TreeContentProvider) obj;

			if (DbPlugin.getDefault().confirmDialog(Messages.getString("RemoveBookmarkAction.2"))) { //$NON-NLS-1$

				while (iter.hasNext()) {
					Object object = iter.next();
					if (object instanceof Bookmark) {
						Bookmark bm = (Bookmark) object;

						BookmarkRoot root = bm.getBookmarkRoot();
						BookmarkFolder folder = bm.getBookmarkFolder();
						if (folder == null) {
							root.removeChild(bm);
						} else {
							folder.removeChild(bm);
						}

						provider.setBookmarkRoot(root);
					}

				}

			}

			viewer.refresh();
		}

	}

}
