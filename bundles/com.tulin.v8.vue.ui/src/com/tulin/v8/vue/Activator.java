package com.tulin.v8.vue;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "com.tulin.v8.vue";
	private static Activator plugin;

	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public Image getImage(String path) {
		Image image = getImageRegistry().get(path);
		if (image == null) {
			ImageDescriptor descriptor = getImageDescriptor(path);
			if (descriptor != null) {
				getImageRegistry().put(path, image = descriptor.createImage());
			}
		}
		return image;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
