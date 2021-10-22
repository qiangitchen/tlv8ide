package com.tulin.v8.ide.views.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.internal.FlowFolder;
import com.tulin.v8.ide.utils.ActionUtils;
import com.tulin.v8.ide.views.navigator.dialog.NewFlowFolderDialog;

import zigen.plugin.db.ui.internal.TreeNode;

public class NewFlowFolderAction extends Action implements Runnable {
	TreeViewer viewer = null;

	public NewFlowFolderAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.NewFile.9"));
		setImageDescriptor(ImageDescriptor
				.createFromImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER)));
	}

	public void run() {
		Shell shell = StudioPlugin.getShell();
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		TreeNode node = (TreeNode) obj;
		NewFlowFolderDialog dialog = new NewFlowFolderDialog(shell, node);
		int state = dialog.open();
		if (state == IDialogConstants.OK_ID) {
			if (obj instanceof FlowFolder) {
				ActionUtils.loadFlowFolder((FlowFolder) obj);
			} else {
				ActionUtils.refreshProcess(node);
			}
			viewer.refresh(node);
		}
	}
}
