package com.tulin.v8.ide.navigator.views.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.wizards.folder.NewFileWizard;

public class NewCSSFileAction extends Action implements Runnable {
	TreeViewer viewer = null;

	public NewCSSFileAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.NewCSSFile.1"));
		setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin
				.getIcon("css.gif")));
	}

	public void run() {
		Shell shell = StudioPlugin.getShell();
		NewFileWizard wizard = new NewFileWizard(viewer, "css");
		WizardDialog dialog2 = new WizardDialog(shell, wizard);
		dialog2.open();
	}
}