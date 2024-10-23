/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.INode;
import zigen.plugin.db.ui.internal.ITable;

public class RefreshTreeNodeAction implements Runnable {

	public static final int MODE_NOTHING = 0;

	public static final int MODE_EXPAND = 1;

	public static final int MODE_COLLAPSE = 2;

	private TreeViewer viewer;

	private INode treeNode;

	private int mode = MODE_NOTHING;

	public RefreshTreeNodeAction(TreeViewer viewer, INode treeNode, int mode) {
		this.viewer = viewer;
		this.treeNode = treeNode;
		this.mode = mode;
	}

	public RefreshTreeNodeAction(TreeViewer viewer, INode treeNode) {
		this.viewer = viewer;
		this.treeNode = treeNode;
	}

	public void run() {
		if (treeNode != null) {

			switch (mode) {
			case MODE_NOTHING:
				;
				break;
			case MODE_EXPAND:
				viewer.expandToLevel(treeNode, 1);
				break;
			case MODE_COLLAPSE:
				viewer.collapseToLevel(treeNode, 1);
				break;
			default:
				break;
			}

			viewer.refresh(treeNode);


			viewer.getControl().notifyListeners(SWT.Selection, null);

			if (treeNode instanceof DataBase) {
				if (mode == RefreshTreeNodeAction.MODE_EXPAND) {
					DbPlugin.fireStatusChangeListener(((DataBase) treeNode).getDbConfig(), IStatusChangeListener.EVT_AddSchemaFilter);
				}
			} else if (treeNode instanceof ITable) {
				DbPlugin.fireStatusChangeListener((ITable) treeNode, IStatusChangeListener.EVT_RefreshTable);
			}
		}

	}
}
