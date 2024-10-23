package tern.eclipse.ide.internal.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import tern.eclipse.ide.core.ITernRepositoryManager;
import tern.eclipse.ide.core.TernCorePlugin;
import tern.eclipse.ide.core.preferences.TernCorePreferenceConstants;

/**
 * Tern core prereferences initializer.
 *
 */
@SuppressWarnings("deprecation")
public class TernCorePreferenceInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope()
				.getNode(TernCorePlugin.PLUGIN_ID);
		// By default Tern with Node.js is used.
		node.put(TernCorePreferenceConstants.TERN_SERVER_TYPE,
				"tern.eclipse.ide.server.nodejs");
		// trace server on console
		node.putBoolean(TernCorePreferenceConstants.TRACE_ON_CONSOLE, false);
		// disable loading plugins from the project root,
		// see
		// https://github.com/marijnh/tern/commit/154b0587a64eea193d124005e03d80065ac310e2
		node.putBoolean(TernCorePreferenceConstants.LOADING_LOCAL_PLUGINS,
				false);
		// tern repository used is "default" which is stored inside
		// tern.core/node_modules/tern
		node.put(TernCorePreferenceConstants.USED_REPOSITORY_NAME,
				ITernRepositoryManager.DEFAULT_REPOSITORY_NAME);
		// default modules
		node.put(TernCorePreferenceConstants.DEFAULT_TERN_MODULES,
				TernCorePreferenceConstants.DEFAULT_TERN_MODULES_VALUE);
	}

}