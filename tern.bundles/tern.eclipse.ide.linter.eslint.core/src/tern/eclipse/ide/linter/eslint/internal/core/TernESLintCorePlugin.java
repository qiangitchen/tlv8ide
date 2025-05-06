package tern.eclipse.ide.linter.eslint.internal.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * ESLint core plugin.
 *
 */
public class TernESLintCorePlugin extends Plugin {

	public static final String PLUGIN_ID = "tern.eclipse.ide.linter.eslint.core"; //$NON-NLS-1$

	// The shared instance.
	private static TernESLintCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public TernESLintCorePlugin() {
		super();
		plugin = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static TernESLintCorePlugin getDefault() {
		return plugin;
	}

}
