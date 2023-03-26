package com.tulin.v8.ide.views.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.views.navigator.dialog.NewMapperDialog;

import zigen.plugin.db.ui.internal.ITable;

public class NewMapperAction extends Action implements Runnable {
	TreeViewer viewer;

	public NewMapperAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("View.Action.NewMapper.title"));
		this.setToolTipText(Messages.getString("View.Action.NewMapper.text"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("add.gif")));
	}

	public void run() {
		Object element = ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof ITable) {
			ITable table = (ITable) element;
			Shell shell = StudioPlugin.getShell();
			NewMapperDialog dialog = new NewMapperDialog(shell, table);
			int state = dialog.open();
			if (state == IDialogConstants.OK_ID) {

			}
		}
	}
}
