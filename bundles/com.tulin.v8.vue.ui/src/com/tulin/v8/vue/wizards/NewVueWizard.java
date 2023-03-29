package com.tulin.v8.vue.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewVueWizard extends Wizard implements INewWizard {
	ISelection selection;
	
	ProjectSelectPage projctPage;

	public NewVueWizard() {
		super();
	}

	public NewVueWizard(ISelection selection) {
		super();
		this.selection = selection;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle(Messages.getString("wizards.vue.title"));
	}

	@Override
	public void addPages() {
		projctPage = new ProjectSelectPage();
		addPage(projctPage);
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
