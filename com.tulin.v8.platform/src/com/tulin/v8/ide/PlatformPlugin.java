package com.tulin.v8.ide;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class PlatformPlugin extends AbstractUIPlugin{
	private static PlatformPlugin plugin;
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static PlatformPlugin getDefault() {
		return plugin;
	}
	
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}
}
