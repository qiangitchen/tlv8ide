/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.views.internal.SQLContextType;

public class SQLTemplateEditorUI {

	private static final String CUSTOM_TEMPLATES_KEY = "zigen.plugin.db.preference.SQLTemplateEditorUI"; //$NON-NLS-1$

	private static SQLTemplateEditorUI fInstance;

	private TemplateStore fStore;

	private ContributionContextTypeRegistry fRegistry;

	private SQLTemplateEditorUI() {}

	public static SQLTemplateEditorUI getDefault() {
		if (fInstance == null)
			fInstance = new SQLTemplateEditorUI();
		return fInstance;
	}

	public TemplateStore getTemplateStore() {
		if (fStore == null) {
			fStore = new ContributionTemplateStore(getContextTypeRegistry(), DbPlugin.getDefault().getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
			try {
				fStore.load();
			} catch (IOException e) {
				DbPlugin.getDefault().showErrorDialog(e);
			}
		}
		return fStore;
	}

	public ContextTypeRegistry getContextTypeRegistry() {
		if (fRegistry == null) {
			fRegistry = new ContributionContextTypeRegistry();
			fRegistry.addContextType(SQLContextType.CONTEXT_TYPE_SQL);
			fRegistry.addContextType(SQLContextType.CONTEXT_TYPE_FUNCTION);
		}
		return fRegistry;
	}

	public ImageRegistry getImageRegistry() {
		return DbPlugin.getDefault().getImageRegistry();
	}

	public static ImageDescriptor imageDescriptorFromPlugin(String string, String default_image) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(string, default_image);
	}

	public IPreferenceStore getPreferenceStore() {
		return DbPlugin.getDefault().getPreferenceStore();
	}

	public void savePluginPreferences() {
		DbPlugin.getDefault().savePluginPreferences();
	}


}
