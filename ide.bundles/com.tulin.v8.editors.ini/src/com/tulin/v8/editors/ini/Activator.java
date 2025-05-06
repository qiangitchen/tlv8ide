package com.tulin.v8.editors.ini;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.tulin.v8.editors.ini.editors.eclipse.PreferencesAdapter;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "com.tulin.v8.editors.ini";
	private static Activator plugin;
	private JavaTextTools fJavaTextTools;
	private IPreferenceStore fCombinedPreferenceStore;
	private Map<String, IPreferenceStore> preferenceStoreMap;
	private static ThreadLocal<Boolean> readingPropertiesDocument = new ThreadLocal();

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

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public synchronized JavaTextTools getJavaTextTools() {
		if (this.fJavaTextTools == null)
			this.fJavaTextTools = new JavaTextTools(getPreferenceStore(), JavaCore.getPlugin().getPluginPreferences());
		return this.fJavaTextTools;
	}

	public IPreferenceStore getCombinedPreferenceStore() {
		if (this.fCombinedPreferenceStore == null) {
			IPreferenceStore generalTextStore = EditorsUI.getPreferenceStore();
			IPreferenceStore javaUIPrefStore = getPreferenceStoreForPlugin(JavaCore.PLUGIN_ID);
			this.fCombinedPreferenceStore = new ChainedPreferenceStore(new IPreferenceStore[] { getPreferenceStore(),
					new PreferencesAdapter(JavaCore.getPlugin().getPluginPreferences()), generalTextStore,
					javaUIPrefStore });
		}

		return this.fCombinedPreferenceStore;
	}

	public synchronized IPreferenceStore getPreferenceStoreForPlugin(String pluginId) {
		if (this.preferenceStoreMap == null) {
			this.preferenceStoreMap = new HashMap();
		}

		IPreferenceStore result = (IPreferenceStore) this.preferenceStoreMap.get(pluginId);
		if (result != null) {
			return result;
		}

		result = new ScopedPreferenceStore(InstanceScope.INSTANCE, pluginId);
		this.preferenceStoreMap.put(pluginId, result);

		return result;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}

	public boolean isReadingPropertiesDocument() {
		Boolean result = (Boolean) readingPropertiesDocument.get();
		return (result != null) && (result.booleanValue());
	}

	public void setReadingPropertiesDocument(boolean value) {
		readingPropertiesDocument.set(Boolean.valueOf(value));
	}
}