package tern.eclipse.ide.linter.lint.internal.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Tern Lint UI messages.
 * 
 */
public final class TernLintUIMessages extends NLS {

	private static final String BUNDLE_NAME = "tern.eclipse.ide.linter.lint.internal.ui.TernLintUIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Preferences

	private TernLintUIMessages() {
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
		NLS.initializeMessages(BUNDLE_NAME, TernLintUIMessages.class);
	}
}
