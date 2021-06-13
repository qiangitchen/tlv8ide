package com.tulin.v8.ide.navigator.views.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.wizards.folder.NewFolderWizard;

public class NewFolderAction extends Action implements Runnable {
	TreeViewer viewer = null;

	public NewFolderAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.NewFile.2"));
		setImageDescriptor(ImageDescriptor.createFromImage(PlatformUI
				.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_FOLDER)));
	}

	public void run() {
		Shell shell = StudioPlugin.getShell();
		NewFolderWizard wizard = new NewFolderWizard(viewer);
		WizardDialog dialog2 = new WizardDialog(shell, wizard);
		dialog2.open();
	}
}
