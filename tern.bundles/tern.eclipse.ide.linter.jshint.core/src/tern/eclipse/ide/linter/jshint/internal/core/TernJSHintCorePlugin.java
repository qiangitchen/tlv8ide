package tern.eclipse.ide.linter.jshint.internal.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class TernJSHintCorePlugin extends Plugin {

	public static final String PLUGIN_ID = "tern.eclipse.ide.linter.jshint.core"; //$NON-NLS-1$

	// The shared instance.
	private static TernJSHintCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public TernJSHintCorePlugin() {
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
	public static TernJSHintCorePlugin getDefault() {
		return plugin;
	}

}
