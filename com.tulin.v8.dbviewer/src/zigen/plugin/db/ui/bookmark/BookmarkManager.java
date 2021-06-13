/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.bookmark;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.DefaultXmlManager;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

public class BookmarkManager extends DefaultXmlManager {

	private BookmarkRoot bookmarkRoot;

	public void setBookmarkRoot(BookmarkRoot bookmarkRoot) {
		this.bookmarkRoot = bookmarkRoot;
	}

	public BookmarkRoot getBookmarkRoot() {
		return bookmarkRoot;
	}

	public BookmarkManager(IPath path) {
		super(path, DbPluginConstant.FN_BOOKMARK);
		bookmarkRoot = load();
		if (bookmarkRoot == null) {
			bookmarkRoot = new BookmarkRoot();
		}
	}

	public BookmarkRoot load() {
		try {
			Object obj = super.loadXml();
			if (obj instanceof BookmarkRoot) {
				return (BookmarkRoot) obj;
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return null;
	}

	public void save() {
		try {
			removeNode();
			super.saveXml(bookmarkRoot);
		} catch (IOException e) {
			DbPlugin.log(e);
		}

	}

	private void removeNode() {
		try {

			bookmarkRoot.setParent(null);
			removeColumnNode(bookmarkRoot);
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	private void removeColumnNode(TreeNode node) {
		TreeLeaf[] leafs = node.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];

			if (leaf instanceof Bookmark) {
				Bookmark bm = (Bookmark) leaf;

				bm.removeChildAll();

				TableColumn tColumn = new TableColumn();
				tColumn.setColumnName(DbPluginConstant.TREE_LEAF_LOADING);
				bm.addChild(new Column(tColumn));

				bm.setExpanded(false);

			} else if (leaf instanceof TreeNode) {
				removeColumnNode((TreeNode) leaf);
			}

		}
	}
}
