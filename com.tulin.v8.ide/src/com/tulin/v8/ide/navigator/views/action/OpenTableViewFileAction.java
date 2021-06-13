package com.tulin.v8.ide.navigator.views.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.navigator.views.job.OpenTableViewEditorJob;

import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Table;

public class OpenTableViewFileAction extends Action implements Runnable {
	TreeViewer viewer;

	public OpenTableViewFileAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.openfile.19"));
		setText(Messages.getString("View.Action.openfile.20"));
		setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("table.gif")));
	}

	public void run() {
		openFile();
	}

	private void openFile() {
		ISelection selection = viewer.getSelection();
		StudioPlugin.setSelection(selection);
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj instanceof Table) {
			ITable itable = (ITable) obj;
			OpenTableViewEditorJob opeditor = new OpenTableViewEditorJob(viewer, itable);
			opeditor.setPriority(OpenTableViewEditorJob.SHORT);
			opeditor.setUser(false);
			opeditor.schedule();
		}
	}

}
