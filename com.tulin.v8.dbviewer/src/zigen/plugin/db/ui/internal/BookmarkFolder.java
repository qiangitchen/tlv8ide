/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

public class BookmarkFolder extends TreeNode {

	private static final long serialVersionUID = 1L;

	public BookmarkFolder() {
		super();
	}

	public BookmarkFolder(String name) {
		super(name);
	}

	public BookmarkRoot getBookmarkRoot() {
		return getBookmarkRoot(this);
	}

	public BookmarkFolder getBookmarkFolder() {
		return getBookmarkFolder(this);
	}

	private BookmarkRoot getBookmarkRoot(TreeLeaf leaf) {
		if (leaf instanceof BookmarkRoot) {
			return (BookmarkRoot) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkRoot(leaf.getParent());
			} else {
				// return null;
				throw new IllegalStateException("BookmarkFolder#getBookmarkRoot");
			}
		}
	}

	private BookmarkFolder getBookmarkFolder(TreeLeaf leaf) {
		if (leaf instanceof BookmarkFolder) {
			return (BookmarkFolder) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getBookmarkFolder(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		BookmarkFolder castedObj = (BookmarkFolder) o;
		return ((this.name == null ? castedObj.name == null : this.name.equals(castedObj.name)));
	}

}
