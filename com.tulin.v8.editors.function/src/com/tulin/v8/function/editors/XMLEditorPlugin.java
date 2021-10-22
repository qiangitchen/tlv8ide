package com.tulin.v8.function.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class XMLEditorPlugin extends AbstractUIPlugin {
	private static XMLEditorPlugin plugin;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static XMLEditorPlugin getDefault() {
		return plugin;
	}

	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
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
		return imageDescriptorFromPlugin(getPluginId(), path);
	}

	public static Image getIcon(String imgname) {
		return getDefault().getImage("/icons/" + imgname);
	}

}
