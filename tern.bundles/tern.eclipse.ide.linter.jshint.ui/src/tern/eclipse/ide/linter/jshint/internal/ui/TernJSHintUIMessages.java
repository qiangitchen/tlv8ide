package tern.eclipse.ide.linter.jshint.internal.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Tern JSHint UI messages.
 * 
 */
public final class TernJSHintUIMessages extends NLS {

	private static final String BUNDLE_NAME = "tern.eclipse.ide.linter.jshint.internal.ui.TernJSHintUIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	private TernJSHintUIMessages() {
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
		NLS.initializeMessages(BUNDLE_NAME, TernJSHintUIMessages.class);
	}
}
