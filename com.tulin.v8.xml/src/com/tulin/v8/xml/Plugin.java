package com.tulin.v8.xml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Plugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.tulin.v8.xml";
	public static final String ICON_TAG = "_icon_tag";
	public static final String ICON_ATTR = "_icon_attribute";
	public static final String ICON_VALUE = "_icon_value";
	public static final String PREF_CUSTOM_ELEMENTS = "__pref_custom_elements";
	public static final String PREF_CUSTOM_ATTRS = "__pref_custom_attributes";

	// The shared instance
	private static Plugin plugin;

	/**
	 * The constructor
	 */
	public Plugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Plugin getDefault() {
		return plugin;
	}

	public static IEditorPart getActiveEditor() {
		IEditorPart editor = getDefault().getPage().getActiveEditor();
		return editor;
	}

	public IWorkbenchPage getPage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	/**
	 * @param path
	 *            the path ["/icon/test.gif"]
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static void logException(Throwable ex) {
		ILog log = getDefault().getLog();
		IStatus status = null;
		if (ex instanceof CoreException) {
			status = ((CoreException) ex).getStatus();
		} else {
			status = new Status(IStatus.ERROR, getDefault().getPluginId(), 0,
					ex.toString(), ex);
		}
		log.log(status);

		// TODO debug
		ex.printStackTrace();
	}

	public String getPluginId() {
		return getBundle().getSymbolicName();
	}

}
