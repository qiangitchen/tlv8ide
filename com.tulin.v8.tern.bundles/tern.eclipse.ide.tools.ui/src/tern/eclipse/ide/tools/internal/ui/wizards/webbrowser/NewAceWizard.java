package tern.eclipse.ide.tools.internal.ui.wizards.webbrowser;

import tern.eclipse.ide.tools.core.webbrowser.ace.AceOptions;
import tern.eclipse.ide.tools.internal.ui.TernToolsUIMessages;

/**
 * Wizard to create HTML page with Tern and Ace editor.
 */
public class NewAceWizard extends NewEditorWizard<AceOptions> {

	public NewAceWizard() {
		super.setWindowTitle(TernToolsUIMessages.NewAceWizard_windowTitle);
		// super.setDefaultPageImageDescriptor(ImageResource
		// .getImageDescriptor(ImageResource.IMG_NEWTYPEDEF_WIZ));
	}

	@Override
	protected NewAceWizardPage createNewFileWizardPage() {
		return new NewAceWizardPage();
	}

	@Override
	protected AceOptions createModel() {
		return new AceOptions();
	}

	@Override
	protected String getTaskLabel() {
		return TernToolsUIMessages.NewAceWizard_taskLabel;
	}

}
