package tern.eclipse.ide.linter.lint.internal.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Lint core plugin.
 *
 */
public class TernLintCorePlugin extends Plugin {

	public static final String PLUGIN_ID = "tern.eclipse.ide.linter.lint.core"; //$NON-NLS-1$

	// The shared instance.
	private static TernLintCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public TernLintCorePlugin() {
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
	public static TernLintCorePlugin getDefault() {
		return plugin;
	}

}
