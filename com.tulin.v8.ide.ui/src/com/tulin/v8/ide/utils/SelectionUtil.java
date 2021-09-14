package com.tulin.v8.ide.utils;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

@SuppressWarnings("rawtypes")
public class SelectionUtil {
	private static TreeNode treenode;

	public static List toList(ISelection selection) {
		if ((selection instanceof IStructuredSelection))
			return ((IStructuredSelection) selection).toList();
		return null;
	}

	public static Object getSingleElement(ISelection s) {
		if (!(s instanceof IStructuredSelection))
			return null;
		IStructuredSelection selection = (IStructuredSelection) s;
		if (selection.size() != 1) {
			return null;
		}
		return selection.getFirstElement();
	}

	public static TreeNode fiendItem(TreeNode item, String editPath) {
		TreeLeaf[] items = item.getChildrens();
		for (int i = 0; i < items.length; i++) {
			Object cuitem = items[i];
			if (cuitem instanceof TreeNode) {
				TreeNode tree = (TreeNode) cuitem;
				if (editPath.equals(tree.getPath())) {
					treenode = tree;
				}
				if ("BIZ".equals(tree.getName()) || "BIZ".equals(tree.getBiz())) {
					treenode = fiendItem(tree, editPath);
				} else if ("UI".equals(tree.getName())
						|| "UI".equals(tree.getBiz())) {
					treenode = fiendItem(tree, editPath);
				} else if ("mobileUI".equals(tree.getName())
						|| "mobileUI".equals(tree.getBiz())) {
					treenode = fiendItem(tree, editPath);
				}
				if (tree.getBiz() == null)
					continue;
			}
		}
		return treenode;
	}

}