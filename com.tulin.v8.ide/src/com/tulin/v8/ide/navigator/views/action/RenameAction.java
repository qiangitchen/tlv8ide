package com.tulin.v8.ide.navigator.views.action;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.navigator.views.dialog.RenameDialog;
import com.tulin.v8.ide.ui.editors.process.element.FlowFolderElement;
import com.tulin.v8.ide.ui.editors.process.element.ProcessDrawElement;
import com.tulin.v8.ide.ui.internal.FlowDraw;
import com.tulin.v8.ide.ui.internal.FlowFolder;
import com.tulin.v8.ide.utils.SelectionUtil;

import zigen.plugin.db.ui.internal.TreeNode;

public class RenameAction extends Action implements Runnable {
	TreeViewer viewer = null;
	String newname;

	public RenameAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("View.Action.Rename.1"));
	}

	public void run() {
		ISelection selection = this.viewer.getSelection();
		Object obj = SelectionUtil.getSingleElement(selection);
		if (obj != null) {
			TreeNode node = (TreeNode) obj;
			RenameDialog dialog = new RenameDialog(
					StudioPlugin.getShell(), node);
			int state = dialog.open();
			if (state == IDialogConstants.OK_ID) {
				newname = dialog.getNewName();
				ensure(node);
			}
		} else {
			Sys.printErrMsg(Messages.getString("View.Action.Rename.2"));
		}
	}

	void ensure(TreeNode node) {
		if (newname == null || "".equals(newname)) {
			MessageDialog.openError(StudioPlugin.getShell(),
					Messages.getString("View.Action.Rename.3"),
					Messages.getString("View.Action.Rename.7"));
			return;
		}
		if (!node.getName().equals(newname)) {
			if (node instanceof FlowDraw) {
				FlowDraw draw = (FlowDraw) node;
				draw.setName(newname);
				ProcessDrawElement element = draw.getElement();
				element.setSprocessname(newname);
				element.update();
				viewer.refresh(node);
			} else if (node instanceof FlowFolder) {
				FlowFolder dfolder = (FlowFolder) node;
				dfolder.setName(newname);
				FlowFolderElement element = dfolder.getElement();
				element.setSname(newname);
				element.update();
				viewer.refresh(node);
			} else if ("file".equals(node.getTvtype())) {
				renameFile(node, newname);
			} else if ("folder".equals(node.getTvtype())) {
				renameFolder(node, newname);
			}
		}
	}

	void renameFile(TreeNode node, String name) {
		try {
			File file = new File(node.getPath());
			File filer = new File(node.getPath().replace(node.getName(), name));
			if (filer.exists()) {
				MessageDialog.openError(viewer.getControl().getShell(),
						Messages.getString("View.Action.Rename.3"),
						Messages.getString("View.Action.Rename.8") + name
								+ Messages.getString("View.Action.Rename.9"));
				return;
			}
			file.renameTo(filer);
			node.setName(name);
			node.setPath(filer.getAbsolutePath());
			viewer.refresh(node);
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
		}
	}

	void renameFolder(TreeNode node, String name) {
		try {
			File file = new File(node.getPath());
			File filer = new File(node.getPath().replace(node.getName(), name));
			if (filer.exists()) {
				MessageDialog.openError(viewer.getControl().getShell(),
						Messages.getString("View.Action.Rename.3"),
						Messages.getString("View.Action.Rename.10") + name
								+ Messages.getString("View.Action.Rename.11"));
				return;
			}
			file.renameTo(filer);
			node.setName(name);
			node.setPath(filer.getAbsolutePath());
			ActionUtils.refreshFolder(node, filer, true);
			viewer.refresh(node);
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
		}
	}

}
