package com.tulin.v8.ide.navigator.views.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.navigator.views.dialog.EditFlowDrawDialog;
import com.tulin.v8.ide.ui.internal.FlowDraw;

public class RePropertyFlowDrawAction extends Action implements Runnable {
	TreeViewer viewer = null;

	public RePropertyFlowDrawAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.Rename.12"));
	}

	public void run() {
		Shell shell = StudioPlugin.getShell();
		Object obj = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		final FlowDraw node = (FlowDraw) obj;
		EditFlowDrawDialog dialog = new EditFlowDrawDialog(shell, node);
		int state = dialog.open();
		if (state == IDialogConstants.OK_ID) {
			viewer.refresh(node);
		}
	}

}
