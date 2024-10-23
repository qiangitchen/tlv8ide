package tern.eclipse.ide.server.nodejs.internal.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Eclipse preference initializer for tern node.js core.
 * 
 */
public class TernNodejsCorePreferenceInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		TernNodejsCorePreferenceConstants.initializeDefaultValues();
	}

}