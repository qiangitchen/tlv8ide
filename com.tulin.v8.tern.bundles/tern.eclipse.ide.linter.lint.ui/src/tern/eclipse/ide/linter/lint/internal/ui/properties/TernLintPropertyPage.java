package tern.eclipse.ide.linter.lint.internal.ui.properties;

import tern.eclipse.ide.linter.ui.properties.TernLinterPropertyPage;
import tern.server.TernPlugin;

/**
 * Tern Lint preferences page.
 * 
 */
public class TernLintPropertyPage extends TernLinterPropertyPage {

	public TernLintPropertyPage() {
		super(TernPlugin.lint.getName());
	}

}
