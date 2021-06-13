/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

public class DiffContentProvider implements ITreeContentProvider {

	public static final String FOLDER_INCLUDE_ONLY_TARGET = Messages.getString("DiffContentProvider.0"); //$NON-NLS-1$

	public static final String FOLDER_INCLUDE_ONLY_ORIGIN = Messages.getString("DiffContentProvider.1"); //$NON-NLS-1$

	public static final String FOLDER_BOTH_DIFFERENCE = Messages.getString("DiffContentProvider.2"); //$NON-NLS-1$

	public static final String FOLDER_BOTH_SAME = Messages.getString("DiffContentProvider.3"); //$NON-NLS-1$

	private Root invisibleRoot;

	private TreeViewer viewer;

	Table[] tables = null;

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {

		if (newInput != null) {
			invisibleRoot = new Root("invisible", true); //$NON-NLS-1$

			Folder folder1 = new Folder(FOLDER_INCLUDE_ONLY_TARGET);
			Folder folder2 = new Folder(FOLDER_INCLUDE_ONLY_ORIGIN);
			Folder folder3 = new Folder(FOLDER_BOTH_DIFFERENCE);
			Folder folder4 = new Folder(FOLDER_BOTH_SAME);

			invisibleRoot.addChild(folder1);
			invisibleRoot.addChild(folder2);
			invisibleRoot.addChild(folder3);
			invisibleRoot.addChild(folder4);

			this.viewer = (TreeViewer) v;

			if (newInput instanceof IDDLDiff[]) {
				IDDLDiff[] diffs = (IDDLDiff[]) newInput;

				int cnt1 = 0;
				int cnt2 = 0;
				int cnt3 = 0;
				int cnt4 = 0;

				for (int i = 0; i < diffs.length; i++) {
					IDDLDiff diff = diffs[i];

					switch (diff.getResultType()) {
					case IDDLDiff.TYPE_INCLUDE_ONLY_TARGET:
						folder1.addChild((TreeLeaf) diff);
						cnt1++;
						break;
					case IDDLDiff.TYPE_INCLUDE_ONLY_ORIGN:
						folder2.addChild((TreeLeaf) diff);
						cnt2++;
						break;
					case IDDLDiff.TYPE_BOTH_DIFFERENCE:
						folder3.addChild((TreeLeaf) diff);
						cnt3++;
						break;
					case IDDLDiff.TYPE_BOTH_SAME:
						folder4.addChild((TreeLeaf) diff);
						cnt4++;
						break;
					default:
						invisibleRoot.addChild((TreeLeaf) diff);
						break;
					}
				}

				folder1.setName(FOLDER_INCLUDE_ONLY_TARGET + " (" + cnt1 + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				folder2.setName(FOLDER_INCLUDE_ONLY_ORIGIN + " (" + cnt2 + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				folder3.setName(FOLDER_BOTH_DIFFERENCE + " (" + cnt3 + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				folder4.setName(FOLDER_BOTH_SAME + " (" + cnt4 + ")"); //$NON-NLS-1$ //$NON-NLS-2$


			}

		}

	}

	public void dispose() {}

	public Object[] getElements(Object inputElement) {
		return getChildren(invisibleRoot);
	}

	public Object getParent(Object element) {
		if (element instanceof TreeLeaf) {
			return ((TreeLeaf) element).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TreeNode) {
			return ((TreeNode) parentElement).getChildrens();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		if (element instanceof TreeNode)
			return ((TreeNode) element).hasChildren();
		return false;
	}

	public Root getInvisibleRoot() {
		return invisibleRoot;
	}
}
