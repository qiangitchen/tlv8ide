package com.tulin.v8.ide.navigator.views.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.ide.navigator.views.dialog.PropertyViewerDialog;

import zigen.plugin.db.ui.internal.TreeNode;

public class PropertyViewerAction extends Action implements Runnable {
	TreeViewer viewer = null;
	private TreeNode treenode;

	public PropertyViewerAction(TreeViewer viewer) {
		this.viewer = viewer;
		Object obj = (Object) ((StructuredSelection) viewer.getSelection())
				.getFirstElement();
		treenode = (TreeNode) obj;
		this.setText(Messages.getString("View.Action.PropertyViewer.1"));
	}

	public void run() {
		new PropertyViewerDialog(viewer.getControl().getShell(), treenode).open();
	}
}
