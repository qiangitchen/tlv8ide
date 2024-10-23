package com.tulin.v8.webtools.ide.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class AddJavaScriptLibraryWizard extends Wizard implements INewWizard {

	private AddJavaScriptLibraryWizardPage page;
	private IContainer selection;

	@Override
	public boolean performFinish() {
		IContainer container = page.getAddContainer();
		if (container != null) {
			for (String name : page.getSelectedLibraryNames()) {
				JavaScriptLibrariesManager.copyLibrary(name, container);
			}
		}
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object obj = selection.getFirstElement();
		if (obj instanceof IAdaptable && !(obj instanceof IContainer)) {
			obj = ((IAdaptable) obj).getAdapter(IResource.class);
		}
		if (obj instanceof IContainer) {
			this.selection = (IContainer) obj;
		}
	}

	@Override
	public void addPages() {
		page = new AddJavaScriptLibraryWizardPage(selection);
		addPage(page);
	}

}
