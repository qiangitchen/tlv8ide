package tern.eclipse.ide.tools.internal.ui.wizards.webbrowser;

import tern.eclipse.ide.tools.core.webbrowser.codemirror.CodeMirrorOptions;
import tern.eclipse.ide.tools.internal.ui.TernToolsUIMessages;

/**
 * Wizard to create HTML page with Tern and CodeMirror editor.
 */
public class NewCodeMirrorWizard extends NewEditorWizard<CodeMirrorOptions> {

	public NewCodeMirrorWizard() {
		super.setWindowTitle(TernToolsUIMessages.NewCodeMirrorWizard_windowTitle);
		// super.setDefaultPageImageDescriptor(ImageResource
		// .getImageDescriptor(ImageResource.IMG_NEWTYPEDEF_WIZ));
	}

	@Override
	protected NewCodeMirrorWizardPage createNewFileWizardPage() {
		return new NewCodeMirrorWizardPage();
	}

	@Override
	protected CodeMirrorOptions createModel() {
		return new CodeMirrorOptions();
	}

	@Override
	protected String getTaskLabel() {
		return TernToolsUIMessages.NewCodeMirrorWizard_taskLabel;
	}

}
