package tern.eclipse.ide.tools.internal.ui.wizards.webbrowser;

import tern.eclipse.ide.tools.core.generator.IGenerator;
import tern.eclipse.ide.tools.core.webbrowser.codemirror.CodeMirrorOptions;
import tern.eclipse.ide.tools.core.webbrowser.codemirror.HTMLCodeMirrorEditor;
import tern.eclipse.ide.tools.internal.ui.TernToolsUIMessages;

/**
 * Page to fill CodeMirror information.
 * 
 */
public class NewCodeMirrorWizardPage extends
		NewEditorWizardPage<CodeMirrorOptions> {

	private static final String PAGE = "NewCodeMirrorWizardPage";

	public NewCodeMirrorWizardPage() {
		super(PAGE);
		setTitle(TernToolsUIMessages.NewCodeMirrorWizardPage_title);
		setDescription(TernToolsUIMessages.NewCodeMirrorWizardPage_description);
	}

	@Override
	public IGenerator getGenerator(String lineSeparator) {
		return HTMLCodeMirrorEditor.create(lineSeparator);
	}
}
