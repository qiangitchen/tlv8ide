package tern.eclipse.ide.linter.eslint.internal.ui.properties;

import tern.eclipse.ide.linter.ui.properties.TernLinterPropertyPage;
import tern.server.TernPlugin;

/**
 * Tern ESLint preferences page.
 * 
 */
public class TernESLintPropertyPage extends TernLinterPropertyPage {

	public TernESLintPropertyPage() {
		super(TernPlugin.eslint.getName());
	}

}
