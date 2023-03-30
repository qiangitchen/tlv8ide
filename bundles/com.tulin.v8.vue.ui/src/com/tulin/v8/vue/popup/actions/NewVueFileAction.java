package com.tulin.v8.vue.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tulin.v8.vue.Activator;
import com.tulin.v8.vue.wizards.NewVueWizard;

public class NewVueFileAction implements IObjectActionDelegate {

	private ISelection selection = null;

	@Override
	public void run(IAction action) {
		if (selection != null) {
			new WizardDialog(Activator.getShell(), new NewVueWizard(selection)).open();
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
