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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.views.TreeContentProvider;

public class RemoveDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public RemoveDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("RemoveDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RemoveDBAction.1")); //$NON-NLS-1$
		this.setEnabled(true);

		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_DELETE));;
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		StringBuffer sb = new StringBuffer();
		sb.append(Messages.getString("RemoveDBAction.2")); //$NON-NLS-1$
		sb.append(Messages.getString("RemoveDBAction.3")); //$NON-NLS-1$

		if (DbPlugin.getDefault().confirmDialog(sb.toString())) {

			IContentProvider cp = viewer.getContentProvider();
			if (cp instanceof TreeContentProvider) {
				TreeContentProvider tcp = (TreeContentProvider) cp;
				BookmarkRoot bmroot = tcp.getBookmarkRoot();

				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof DataBase) {
						DataBase db = (DataBase) obj;

						removeBookmark(bmroot, db);

						DBConfigManager.remove(db.getDbConfig());
						db.getParent().removeChild(db);
						viewer.refresh();
						viewer.getControl().notifyListeners(SWT.Selection, null);

						DbPlugin.fireStatusChangeListener(db.getDbConfig(), IStatusChangeListener.EVT_RemoveSchemaFilter);

					}
				}


				DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);


			}

		}

	}

	private void removeBookmark(BookmarkFolder folder, DataBase targetDataBase) {
		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof Bookmark) {
				Bookmark bm = (Bookmark) leaf;
				if (bm.getDataBase().equals(targetDataBase)) {
					folder.removeChild(bm);
				}
			} else if (leaf instanceof BookmarkFolder) {
				removeBookmark((BookmarkFolder) leaf, targetDataBase);
			}
		}
	}

}
