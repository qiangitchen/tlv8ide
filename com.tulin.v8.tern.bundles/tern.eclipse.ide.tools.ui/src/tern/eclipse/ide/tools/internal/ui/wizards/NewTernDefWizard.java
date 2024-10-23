package tern.eclipse.ide.tools.internal.ui.wizards;

import tern.eclipse.ide.tools.core.generator.TernDefOptions;
import tern.eclipse.ide.tools.internal.ui.ImageResource;
import tern.eclipse.ide.tools.internal.ui.TernToolsUIMessages;

/**
 * Wizard to create Tern JSON type definitions.
 * 
 * @see http://ternjs.net/doc/manual.html#typedef
 * 
 */
public class NewTernDefWizard extends NewFileWizard<TernDefOptions> {

	public NewTernDefWizard() {
		super.setWindowTitle(TernToolsUIMessages.NewTernDefWizard_windowTitle);
		super.setDefaultPageImageDescriptor(ImageResource
				.getImageDescriptor(ImageResource.IMG_NEWTYPEDEF_WIZ));
	}

	@Override
	protected NewTernDefWizardPage createNewFileWizardPage() {
		return new NewTernDefWizardPage();
	}

	@Override
	protected TernDefOptions createModel() {
		return new TernDefOptions();
	}

	@Override
	protected String getTaskLabel() {
		return TernToolsUIMessages.NewTernDefWizard_taskLabel;
	}

}
