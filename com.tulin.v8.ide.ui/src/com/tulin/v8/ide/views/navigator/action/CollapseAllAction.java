package com.tulin.v8.ide.views.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.ide.StudioPlugin;

public class CollapseAllAction extends Action implements Runnable {

	TreeViewer viewer = null;

	@SuppressWarnings("static-access")
	public CollapseAllAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("View.Action.Collapse.1"));
		this.setToolTipText(Messages.getString("View.Action.Collapse.2"));
		this.setEnabled(true);

		this.setImageDescriptor(StudioPlugin.getDefault()
				.getImageDescriptor("icons/collapseAll.gif"));
	}

	public void run() {
		viewer.collapseAll();
	}
}
