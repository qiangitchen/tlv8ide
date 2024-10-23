package tern.eclipse.ide.tools.internal.ui.wizards.webbrowser;

import tern.eclipse.ide.tools.core.generator.IGenerator;
import tern.eclipse.ide.tools.core.webbrowser.ace.AceOptions;
import tern.eclipse.ide.tools.core.webbrowser.ace.HTMLAceEditor;
import tern.eclipse.ide.tools.internal.ui.TernToolsUIMessages;

/**
 * Page to fill Ace information.
 * 
 */
public class NewAceWizardPage extends NewEditorWizardPage<AceOptions> {

	private static final String PAGE = "NewAceWizardPage";

	public NewAceWizardPage() {
		super(PAGE);
		setTitle(TernToolsUIMessages.NewAceWizardPage_title);
		setDescription(TernToolsUIMessages.NewAceWizardPage_description);
	}

	// options.setBaseURL("http://ace.c9.io/");
	// options.setTernBaseURL("http://ternjs.net/");
	// options.setTernAceBaseURL("https://raw.githubusercontent.com/angelozerr/tern.ace/master/lib/");
	// options.setTernAceBaseURL("file://C:/Documents and Settings/azerr/git/tern.ace/lib/");

	@Override
	public IGenerator getGenerator(String lineSeparator) {
		return HTMLAceEditor.create(lineSeparator);
	}
}
