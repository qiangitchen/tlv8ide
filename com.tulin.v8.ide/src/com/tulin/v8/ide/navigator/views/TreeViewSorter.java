package com.tulin.v8.ide.navigator.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.TreeNode;

public class TreeViewSorter extends ViewerComparator {

	// 返回整数：obj1移到obj2之前；零则obj1和obj2的位置不动；负数则obj1移到obj2之后
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof Root && e2 instanceof Root) {
			return (0);
		} else if (e2 instanceof Root) {
			return (1);
		} else if (e1 instanceof Column && e2 instanceof Column) {
			Column c1 = (Column) e1;
			Column c2 = (Column) e2;
			if (c1.getColumn() != null && c2.getColumn() != null) {
				TableColumn tc1 = (TableColumn) c1.getColumn();
				TableColumn tc2 = (TableColumn) c2.getColumn();
				if (tc1.getSeq() < tc2.getSeq()) {
					return (-1);
				} else if (tc2.getSeq() < tc1.getSeq()) {
					return (1);
				} else {
					return (0);
				}
			}
			return (0);
		} else if (e1 instanceof Folder && e2 instanceof Folder) {
			// Folder folder1 = (Folder) e1;
			// Folder folder2 = (Folder) e2;
			return (0);
		} else if (e1 instanceof TreeNode && e2 instanceof TreeNode) {
			TreeNode f = (TreeNode) e1;
			TreeNode w = (TreeNode) e2;
			if ("process".equals(w.getTvtype())) {
				return (1);
			}
			if ("folder".equals(f.getTvtype()) && "folder".equals(w.getTvtype())) {
				return super.compare(viewer, e1, e2);
			} else if ("file".equals(f.getTvtype()) && "folder".equals(w.getTvtype())) {
				return (1);
			}
			return (-1);
		} else {
			return super.compare(viewer, e1, e2);
		}
	}
}
