package tern.eclipse.ide.linter.eslint.internal.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Tern ESLint UI messages.
 * 
 */
public final class TernESLintUIMessages extends NLS {

	private static final String BUNDLE_NAME = "tern.eclipse.ide.linter.eslint.internal.ui.TernESLintUIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Preferences

	private TernESLintUIMessages() {
	}

	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (MissingResourceException x) {
			fResourceBundle = null;
		}
		return fResourceBundle;
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, TernESLintUIMessages.class);
	}
}
