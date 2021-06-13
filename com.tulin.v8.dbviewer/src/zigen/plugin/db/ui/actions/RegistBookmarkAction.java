/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.ui.bookmark.BookmarkDialog;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.views.TreeContentProvider;

public class RegistBookmarkAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public RegistBookmarkAction(TreeViewer viewer) {

		this.viewer = viewer;
		this.setText(Messages.getString("RegistBookmarkAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RegistBookmarkAction.1")); //$NON-NLS-1$

	}

	public void run() {
		IContentProvider obj = viewer.getContentProvider();

		if (obj instanceof TreeContentProvider) {
			TreeContentProvider provider = (TreeContentProvider) obj;
			BookmarkDialog dialog = new BookmarkDialog(DbPlugin.getDefault().getShell(), provider);
			int ret = dialog.open();

			if (ret == Dialog.OK) {
				TreeNode selectedNode = dialog.getSelectedNode();
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				setBookmark(selection, selectedNode);

			}

		}

		viewer.refresh();

	}

	private void setBookmark(IStructuredSelection selection, TreeNode node) {
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object object = iter.next();
			if (object instanceof Table) {
				Table table = (Table) object;
				Bookmark bm = new Bookmark(table);
				node.addChild(bm);

				if (bm.getChildren().size() == 0) {
					TableColumn tColumn = new TableColumn();
					tColumn.setColumnName(DbPluginConstant.TREE_LEAF_LOADING);
					bm.addChild(new Column(tColumn));
				}
			}
		}

	}

}
