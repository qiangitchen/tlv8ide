package tern.eclipse.ide.tools.internal.ui.wizards;

import org.eclipse.ui.INewWizard;

import tern.eclipse.ide.tools.core.generator.Options;

/**
 * Abstract wizard to create a file.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public abstract class NewFileWizard<T extends Options> extends TernWizard<T>
		implements INewWizard {

	public NewFileWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		NewFileWizardPage<T> page = createNewFileWizardPage();
		super.addPage(page);
		super.addOperation(new NewFileOperation(page));
	}

	protected abstract NewFileWizardPage createNewFileWizardPage();
}
