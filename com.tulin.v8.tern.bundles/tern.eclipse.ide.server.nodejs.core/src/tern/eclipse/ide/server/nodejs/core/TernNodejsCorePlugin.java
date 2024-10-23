package tern.eclipse.ide.server.nodejs.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import tern.eclipse.ide.server.nodejs.internal.core.NodejsInstallManager;

public class TernNodejsCorePlugin extends Plugin {

	public static final String PLUGIN_ID = "tern.eclipse.ide.server.nodejs.core"; //$NON-NLS-1$

	// The shared instance.
	private static TernNodejsCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public TernNodejsCorePlugin() {
		super();
		plugin = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		NodejsInstallManager.getManager().initialize();
		super.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		NodejsInstallManager.getManager().destroy();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static TernNodejsCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the Nodejs install manager.
	 * 
	 * @return the Nodejs install manager.
	 */
	public static INodejsInstallManager getNodejsInstallManager() {
		return NodejsInstallManager.getManager();
	}
}
