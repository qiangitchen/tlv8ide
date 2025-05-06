package tern.eclipse.ide.linter.jscs.internal.ui.properties;

import tern.eclipse.ide.linter.ui.properties.TernLinterPropertyPage;
import tern.server.TernPlugin;

/**
 * Tern JSCS preferences page.
 * 
 */
public class TernJSCSPropertyPage extends TernLinterPropertyPage {

	public TernJSCSPropertyPage() {
		super(TernPlugin.jscs.getName());
	}

}
