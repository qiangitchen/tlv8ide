package com.tulin.v8.webtools.ide.template;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

@SuppressWarnings("deprecation")
public class HTMLTemplateManager {

	private static final String CUSTOM_TEMPLATES_KEY = WebToolsPlugin.getPluginId() + ".customtemplates";

	private static HTMLTemplateManager instance;
	private TemplateStore fStore;
	private ContributionContextTypeRegistry fRegistry;

	private HTMLTemplateManager() {
	}

	public static HTMLTemplateManager getInstance() {
		if (instance == null) {
			instance = new HTMLTemplateManager();
		}
		return instance;
	}

	public TemplateStore getTemplateStore() {
		if (fStore == null) {
			fStore = new ContributionTemplateStore(getContextTypeRegistry(),
					WebToolsPlugin.getDefault().getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
			try {
				fStore.load();
			} catch (IOException e) {
				WebToolsPlugin.logException(e);
			}
		}
		return fStore;
	}

	public ContextTypeRegistry getContextTypeRegistry() {
		if (fRegistry == null) {
			fRegistry = new ContributionContextTypeRegistry();
			fRegistry.addContextType(HTMLContextType.CONTEXT_TYPE);
			fRegistry.addContextType(JavaScriptContextType.CONTEXT_TYPE);
		}
		return fRegistry;
	}

	public IPreferenceStore getPreferenceStore() {
		return WebToolsPlugin.getDefault().getPreferenceStore();
	}

//	public void savePluginPreferences(){
//		WebToolsPlugin.getDefault().savePluginPreferences();
//	}

}
