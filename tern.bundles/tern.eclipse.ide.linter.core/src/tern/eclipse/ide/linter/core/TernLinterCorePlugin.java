package tern.eclipse.ide.linter.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import tern.eclipse.ide.linter.internal.core.TernLinterConfigurationsManager;

public class TernLinterCorePlugin extends Plugin {

	public static final String PLUGIN_ID = "tern.eclipse.ide.linter.core"; //$NON-NLS-1$

	// The shared instance.
	private static TernLinterCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public TernLinterCorePlugin() {
		super();
		plugin = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.start(context);
		TernLinterConfigurationsManager.getManager().initialize();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		TernLinterConfigurationsManager.getManager().destroy();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static TernLinterCorePlugin getDefault() {
		return plugin;
	}

	public ITernLinterConfigurationsManager getTernLinterConfigurationsManager() {
		return TernLinterConfigurationsManager.getManager();
	}
}
