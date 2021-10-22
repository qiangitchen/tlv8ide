/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.bookmark;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.TreeItem;

import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

public class DropBookmarkAdapter extends DropTargetAdapter {


	TreeViewer viewer;

	IStructuredSelection selection;

	public DropBookmarkAdapter(TreeViewer viewer) {
		this.viewer = viewer;
	}

	public void dragOver(DropTargetEvent e) {
		setEventDetail(e);
		e.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
	}

	public void dropAccept(DropTargetEvent e) {
		if (e.item == null) {
			e.detail = DND.DROP_NONE;
		} else {
			Object obj = ((TreeItem) e.item).getData();
			if (obj instanceof TreeLeaf) {
				if (obj instanceof BookmarkRoot || obj instanceof BookmarkFolder) {
					e.detail = DND.DROP_MOVE;
				} else {
					e.detail = DND.DROP_NONE;
				}
			} else {
				e.detail = DND.DROP_NONE;
			}
			setEventDetail(e);

		}
	}

	private void setEventDetail(DropTargetEvent e) {
		if (e.item == null) {
			e.detail = DND.DROP_NONE;
			return;
		}

		if (!(e.item instanceof TreeItem)) {
			e.detail = DND.DROP_NONE;
			return;
		}

		if (!(e.item.getData() instanceof TreeNode)) {
			e.detail = DND.DROP_NONE;
			return;
		}

		Object target = ((TreeItem) e.item).getData();

		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			TreeLeaf leaf = (TreeLeaf) iter.next();

			if (!(leaf instanceof Bookmark) && !(leaf instanceof BookmarkFolder)) {
				e.detail = DND.DROP_NONE;
				return;
			}

			if (leaf.equals((TreeLeaf) target)) {
				e.detail = DND.DROP_NONE;
				return;
			}
			if (isMyChild(leaf, (TreeLeaf) target)) {
				e.detail = DND.DROP_NONE;
				return;
			}

		}

		if (target instanceof BookmarkRoot || target instanceof BookmarkFolder) {
			e.detail = DND.DROP_MOVE;
		} else {
			e.detail = DND.DROP_NONE;
		}
	}

	private boolean isMyChild(TreeLeaf parent, TreeLeaf target) {
		if (target.getParent() == null) {
			return false;
		} else {
			TreeNode wkParent = target.getParent();
			if (parent.equals(wkParent)) {
				return true;
			} else {
				return isMyChild(parent, wkParent);
			}

		}
	}

	public void dragEnter(DropTargetEvent e) {
		selection = (IStructuredSelection) viewer.getSelection();
		if (e.detail == DND.DROP_DEFAULT) {
			e.detail = DND.DROP_MOVE;
		}
	}

	public void dragOperationChanged(DropTargetEvent e) {
		if (e.detail == DND.DROP_DEFAULT) {
			e.detail = DND.DROP_MOVE;
		}
	}

	public void drop(DropTargetEvent e) {

		if (e.data == null) {
			e.detail = DND.DROP_NONE;
			return;
		}

		Object obj = ((TreeItem) e.item).getData();
		if (obj instanceof TreeNode) {
			TreeNode target = (TreeNode) obj;

			if (TreeLeafListTransfer.getInstance().isSupportedType(e.currentDataType)) {

				TreeLeaf[] leafs = (TreeLeaf[]) e.data;

				for (int i = 0; i < leafs.length; i++) {
					TreeLeaf leaf = leafs[i];
					target.addChild(leaf);
					viewer.expandToLevel(leaf, 1);

				}

				viewer.refresh(target);
			} else {
				throw new RuntimeException("UnExpected Error."); //$NON-NLS-1$
			}

		} else {
			throw new IllegalArgumentException("UnExpected Object. " + obj.getClass().getName()); //$NON-NLS-1$
		}

	}
}
