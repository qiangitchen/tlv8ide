package tern.eclipse.ide.tools.internal.ui.wizards.webbrowser;

import tern.eclipse.ide.tools.core.webbrowser.orion.OrionOptions;
import tern.eclipse.ide.tools.internal.ui.TernToolsUIMessages;

/**
 * Wizard to create HTML page with Tern and Orion editor.
 */
public class NewOrionWizard extends NewEditorWizard<OrionOptions> {

	public NewOrionWizard() {
		super.setWindowTitle(TernToolsUIMessages.NewOrionWizard_windowTitle);
		// super.setDefaultPageImageDescriptor(ImageResource
		// .getImageDescriptor(ImageResource.IMG_NEWTYPEDEF_WIZ));
	}

	@Override
	protected NewOrionWizardPage createNewFileWizardPage() {
		return new NewOrionWizardPage();
	}

	@Override
	protected OrionOptions createModel() {
		return new OrionOptions();
	}

	@Override
	protected String getTaskLabel() {
		return TernToolsUIMessages.NewOrionWizard_taskLabel;
	}

}
