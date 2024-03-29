package com.tulin.v8.echarts.ui.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tulin.v8.echarts.ui.Activator;
import com.tulin.v8.echarts.ui.wizards.ChartNewWizard;

public class NewEChatsFileAction implements IObjectActionDelegate {
	private ISelection selection = null;

	@Override
	public void run(IAction action) {
		if (selection != null) {
			new WizardDialog(Activator.getShell(), new ChartNewWizard(selection)).open();
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
