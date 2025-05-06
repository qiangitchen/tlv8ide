package tern.eclipse.ide.jsdt.internal.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import tern.internal.resources.InternalTernResourcesManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class JSDTTernCorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "tern.eclipse.ide.jsdt.core"; //$NON-NLS-1$

	// The shared instance
	private static JSDTTernCorePlugin plugin;

	/**
	 * The constructor
	 */
	public JSDTTernCorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// Initialize DOMProvider with DOM-SSE
		InternalTernResourcesManager.getInstance().setDOMProvider(
				DOMSSEProvider.INSTANCE);
		JSDTClassPathManager.getManager().startup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		JSDTClassPathManager.getManager().shutdown();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static JSDTTernCorePlugin getDefault() {
		return plugin;
	}

}
