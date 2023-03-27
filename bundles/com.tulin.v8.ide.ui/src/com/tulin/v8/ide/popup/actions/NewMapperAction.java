package com.tulin.v8.ide.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.wizards.NewMapperWizard;

public class NewMapperAction implements IObjectActionDelegate {
	private ISelection selection = null;

	@Override
	public void run(IAction action) {
		if (selection != null) {
			new WizardDialog(StudioPlugin.getShell(), new NewMapperWizard(selection)).open();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
