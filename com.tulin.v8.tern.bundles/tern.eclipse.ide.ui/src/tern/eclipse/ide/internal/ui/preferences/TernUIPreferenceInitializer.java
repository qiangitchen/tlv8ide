package tern.eclipse.ide.internal.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import tern.eclipse.ide.ui.TernUIPlugin;

/**
 * Tern UI preferences initializer.
 *
 */
public class TernUIPreferenceInitializer extends AbstractPreferenceInitializer {

	@SuppressWarnings("deprecation")
	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope()
				.getNode(TernUIPlugin.PLUGIN_ID);

		// Content Assist - Insertion
		node.putBoolean(
				TernUIPreferenceConstants.GENERATE_ANONYMOUS_FUNCTION_CONTENT_ASSIST,
				true);
		node.putInt(TernUIPreferenceConstants.INDENT_SIZE_CONTENT_ASSIST,
				TernUIPreferenceConstants.INDENT_SIZE_CONTENT_ASSIST_DEFAULT);
		node.putBoolean(TernUIPreferenceConstants.INDENT_TABS_CONTENT_ASSIST,
				TernUIPreferenceConstants.INDENT_TABS_CONTENT_ASSIST_DEFAULT);

		// Content Assist - Filtering
		node.putBoolean(
				TernUIPreferenceConstants.EXPAND_FUNCTION_CONTENT_ASSIST, true);
		node.putBoolean(
				TernUIPreferenceConstants.OMIT_OBJECT_PROTOTYPE_CONTENT_ASSIST,
				false);
		node.putBoolean(TernUIPreferenceConstants.GUESS_CONTENT_ASSIST, true);
	}

}