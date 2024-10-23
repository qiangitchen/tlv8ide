package tern.eclipse.ide.linter.jshint.internal.ui.properties;

import tern.eclipse.ide.linter.ui.properties.TernLinterPropertyPage;
import tern.server.TernPlugin;

/**
 * Tern JSHint preferences page.
 * 
 */
public class TernJSHintPropertyPage extends TernLinterPropertyPage {

	public TernJSHintPropertyPage() {
		super(TernPlugin.jshint.getName());
	}

}
