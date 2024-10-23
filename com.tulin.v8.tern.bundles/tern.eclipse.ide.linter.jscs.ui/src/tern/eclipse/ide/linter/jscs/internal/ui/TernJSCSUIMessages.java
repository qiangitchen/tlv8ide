package tern.eclipse.ide.linter.jscs.internal.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Tern JSCS UI messages.
 * 
 */
public final class TernJSCSUIMessages extends NLS {

	private static final String BUNDLE_NAME = "tern.eclipse.ide.linter.jscs.internal.ui.TernJSCSUIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Preferences

	private TernJSCSUIMessages() {
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
		NLS.initializeMessages(BUNDLE_NAME, TernJSCSUIMessages.class);
	}
}
