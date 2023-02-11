package com.tulin.v8.ureport.ui.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tulin.v8.ureport.Activator;
import com.tulin.v8.ureport.wizards.UReportFileNewWizard;

public class NewUReportFileAction implements IObjectActionDelegate {
	private ISelection selection = null;

	@Override
	public void run(IAction action) {
		if (selection != null) {
			new WizardDialog(Activator.getShell(), new UReportFileNewWizard(selection)).open();
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
