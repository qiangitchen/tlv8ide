/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;

public class CollapseAllAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public CollapseAllAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText("Collapse All");
		this.setToolTipText("Collapse All");
		this.setEnabled(true);

		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_COLLAPSE_ALL));
	}

	public void run() {
		viewer.collapseAll();
	}

}
