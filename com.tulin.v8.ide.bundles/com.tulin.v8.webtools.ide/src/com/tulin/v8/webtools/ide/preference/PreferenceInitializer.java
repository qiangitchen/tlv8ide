package com.tulin.v8.webtools.ide.preference;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.IColorConstants;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		store.setDefault(WebToolsPlugin.PREF_COLOR_TAG, StringConverter.asString(IColorConstants.TAG));
		store.setDefault(WebToolsPlugin.PREF_COLOR_ATTR, StringConverter.asString(IColorConstants.ATTR));
		store.setDefault(WebToolsPlugin.PREF_COLOR_VALUE, StringConverter.asString(IColorConstants.VALUE));
		store.setDefault(WebToolsPlugin.PREF_COLOR_COMMENT, StringConverter.asString(IColorConstants.HTML_COMMENT));
		store.setDefault(WebToolsPlugin.PREF_COLOR_DOCTYPE, StringConverter.asString(IColorConstants.PROC_INSTR));
		store.setDefault(WebToolsPlugin.PREF_COLOR_STRING, StringConverter.asString(IColorConstants.STRING));
		store.setDefault(WebToolsPlugin.PREF_COLOR_SCRIPT, StringConverter.asString(IColorConstants.SCRIPT));
		store.setDefault(WebToolsPlugin.PREF_COLOR_CSSPROP, StringConverter.asString(IColorConstants.CSS_PROP));
		store.setDefault(WebToolsPlugin.PREF_COLOR_CSSCOMMENT,
				StringConverter.asString(IColorConstants.CSS_COMMENT));
		store.setDefault(WebToolsPlugin.PREF_COLOR_CSSVALUE, StringConverter.asString(IColorConstants.CSS_VALUE));
		store.setDefault(WebToolsPlugin.PREF_EDITOR_TYPE, "tab");
		store.setDefault(WebToolsPlugin.PREF_DTD_URI, "");
		store.setDefault(WebToolsPlugin.PREF_DTD_PATH, "");
		store.setDefault(WebToolsPlugin.PREF_DTD_CACHE, true);
		store.setDefault(WebToolsPlugin.PREF_ASSIST_AUTO, true);
		store.setDefault(WebToolsPlugin.PREF_ASSIST_CHARS, "</\"");
		store.setDefault(WebToolsPlugin.PREF_ASSIST_CLOSE, true);
		store.setDefault(WebToolsPlugin.PREF_ASSIST_TIMES, 0);
		store.setDefault(WebToolsPlugin.PREF_USE_SOFTTAB, false);
		store.setDefault(WebToolsPlugin.PREF_SOFTTAB_WIDTH, 2);
		store.setDefault(WebToolsPlugin.PREF_COLOR_FG, StringConverter.asString(IColorConstants.FOREGROUND));
		store.setDefault(WebToolsPlugin.PREF_COLOR_BG, StringConverter.asString(IColorConstants.BACKGROUND));
		store.setDefault(WebToolsPlugin.PREF_COLOR_BG_DEF, true);
		store.setDefault(WebToolsPlugin.PREF_JSP_COMMENT, StringConverter.asString(IColorConstants.JAVA_COMMENT));
		store.setDefault(WebToolsPlugin.PREF_JSP_STRING, StringConverter.asString(IColorConstants.JAVA_STRING));
		store.setDefault(WebToolsPlugin.PREF_JSP_KEYWORD, StringConverter.asString(IColorConstants.JAVA_KEYWORD));
		store.setDefault(WebToolsPlugin.PREF_JSP_FIX_PATH, false);
		store.setDefault(WebToolsPlugin.PREF_PAIR_CHAR, true);
		store.setDefault(WebToolsPlugin.PREF_SHOW_XML_ERRORS, false);
		store.setDefault(WebToolsPlugin.PREF_COLOR_JSCOMMENT,
				StringConverter.asString(IColorConstants.JAVA_COMMENT));
		store.setDefault(WebToolsPlugin.PREF_COLOR_JSSTRING, StringConverter.asString(IColorConstants.JAVA_STRING));
		store.setDefault(WebToolsPlugin.PREF_COLOR_JSKEYWORD,
				StringConverter.asString(IColorConstants.JAVA_KEYWORD));
		store.setDefault(WebToolsPlugin.PREF_COLOR_JSDOC, StringConverter.asString(IColorConstants.JSDOC));
		store.setDefault(WebToolsPlugin.PREF_CUSTOM_ATTRS, "");
		store.setDefault(WebToolsPlugin.PREF_CUSTOM_ELEMENTS, "");
		store.setDefault(WebToolsPlugin.PREF_TASK_TAGS, "FIXME\t2\nTODO\t1\nXXXX\t1\n");
		store.setDefault(WebToolsPlugin.PREF_ENABLE_CLASSNAME, false);
		store.setDefault(WebToolsPlugin.PREF_CLASSNAME_ATTRS, "type class classname className bean component");
		store.setDefault(WebToolsPlugin.PREF_SCHEMA_MAPPINGS, "");
		store.setDefault(WebToolsPlugin.PREF_AUTO_EDIT, true);
		store.setDefault(WebToolsPlugin.PREF_COLOR_TAGLIB, StringConverter.asString(IColorConstants.TAGLIB));
		store.setDefault(WebToolsPlugin.PREF_COLOR_TAGLIB_ATTR,
				StringConverter.asString(IColorConstants.TAGLIB_ATTR));
		store.setDefault(WebToolsPlugin.PREF_FORMATTER_TAB, false);
		store.setDefault(WebToolsPlugin.PREF_FORMATTER_INDENT, 2);
		store.setDefault(WebToolsPlugin.PREF_FORMATTER_LINE, 120);

		getContributions(store);
	}

	private void getContributions(IPreferenceStore store) {
		try {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint point = registry.getExtensionPoint(WebToolsPlugin.getPluginId() + ".preferenceContributer");
			IExtension[] extensions = point.getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				IConfigurationElement[] elements = extensions[i].getConfigurationElements();
				for (int j = 0; j < elements.length; j++) {
					if ("contributer".equals(elements[j].getName())) {
						IPreferenceContributer contributer = (IPreferenceContributer) elements[j]
								.createExecutableExtension("class");
						contributer.initializeDefaultPreferences(store);
					}
				}
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

}
