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
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.tulin.v8.editors.ini.editors.eclipse.PreferencesAdapter;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.bogus.propeditor";
	private static Activator plugin;
	private JavaTextTools fJavaTextTools;
	private IPreferenceStore fCombinedPreferenceStore;
	private Map<String, IPreferenceStore> preferenceStoreMap;
	private static ThreadLocal<Boolean> readingPropertiesDocument = new ThreadLocal();

	public void start(BundleContext context) throws Exception {
		/* 47 */ super.start(context);
		/* 48 */ plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		/* 58 */ plugin = null;
		/* 59 */ super.stop(context);
	}

	public static Activator getDefault() {
		/* 69 */ return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		/* 82 */ return imageDescriptorFromPlugin("org.bogus.propeditor", path);
	}

	public synchronized JavaTextTools getJavaTextTools() {
		/* 89 */ if (this.fJavaTextTools == null)
			/* 90 */ this.fJavaTextTools = /* 91 */ new JavaTextTools(getPreferenceStore(),
					/* 91 */ JavaCore.getPlugin().getPluginPreferences());
		/* 92 */ return this.fJavaTextTools;
	}

	public IPreferenceStore getCombinedPreferenceStore() {
		/* 111 */ if (this.fCombinedPreferenceStore == null) {
			/* 112 */ IPreferenceStore generalTextStore = EditorsUI.getPreferenceStore();
			/* 113 */ IPreferenceStore javaUIPrefStore = getPreferenceStoreForPlugin("org.eclipse.jdt.ui");
			/* 114 */ this.fCombinedPreferenceStore = /* 119 */ new ChainedPreferenceStore(
					new IPreferenceStore[] { /* 116 */ getPreferenceStore(),
							/* 117 */ new PreferencesAdapter(JavaCore.getPlugin().getPluginPreferences()),
							/* 118 */ generalTextStore, /* 119 */ javaUIPrefStore });
		}

		/* 122 */ return this.fCombinedPreferenceStore;
	}

	public synchronized IPreferenceStore getPreferenceStoreForPlugin(String pluginId) {
		/* 134 */ if (this.preferenceStoreMap == null) {
			/* 135 */ this.preferenceStoreMap = new HashMap();
		}

		/* 138 */ IPreferenceStore result = (IPreferenceStore) this.preferenceStoreMap.get(pluginId);
		/* 139 */ if (result != null) {
			/* 140 */ return result;
		}

		/* 143 */ result = new ScopedPreferenceStore(new InstanceScope(), pluginId);
		/* 144 */ this.preferenceStoreMap.put(pluginId, result);

		/* 146 */ return result;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		/* 152 */ return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	public static Shell getActiveWorkbenchShell() {
		/* 157 */ IWorkbenchWindow window = getActiveWorkbenchWindow();
		/* 158 */ if (window != null) {
			/* 159 */ return window.getShell();
		}
		/* 161 */ return null;
	}

	public boolean isReadingPropertiesDocument() {
		/* 169 */ Boolean result = (Boolean) readingPropertiesDocument.get();
		/* 170 */ return (result != null) && (result.booleanValue());
	}

	public void setReadingPropertiesDocument(boolean value) {
		/* 175 */ readingPropertiesDocument.set(Boolean.valueOf(value));
	}
}