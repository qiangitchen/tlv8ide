/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.contentassist.SQLTemplateFormatter;

public class SQLTemplatesPreferencePage extends TemplatePreferencePage implements IWorkbenchPreferencePage {

	public static final String DESC = Messages.getString("SQLTemplatesPreferencePage.0"); //$NON-NLS-1$

	public static final String TEMPLATES_USE_CODEFORMATTER = "org.eclipse.ui.texteditor.templates.preferences.format_templates";

	IPreferenceStore ps;

	public SQLTemplatesPreferencePage() {
		ps = DbPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(SQLTemplateEditorUI.getDefault().getPreferenceStore());
		setTemplateStore(SQLTemplateEditorUI.getDefault().getTemplateStore());
		setContextTypeRegistry(SQLTemplateEditorUI.getDefault().getContextTypeRegistry());
		super.setDescription(DESC);
		super.setTitle(Messages.getString("SQLTemplatesPreferencePage.1")); //$NON-NLS-1$
	}

	protected boolean isShowFormatterSetting() {
		// return false;
		return true;
	}

	public boolean performOk() {
		boolean ok = super.performOk();
		SQLTemplateEditorUI.getDefault().savePluginPreferences();
		return ok;
	}

	protected void updateViewerInput() {

		boolean useCodeFormatter = ps.getBoolean(SQLTemplatesPreferencePage.TEMPLATES_USE_CODEFORMATTER);


		IStructuredSelection selection = (IStructuredSelection) getTableViewer().getSelection();
		SourceViewer sv = getViewer();

		if (selection.size() == 1) {
			TemplatePersistenceData data = (TemplatePersistenceData) selection.getFirstElement();
			Template template = data.getTemplate();

			if (useCodeFormatter) {
				SQLTemplateFormatter formatter = new SQLTemplateFormatter(sv.getDocument(), 0);
				sv.getDocument().set(formatter.format(template.getPattern()));
			} else {
				sv.getDocument().set(template.getPattern());
			}
		} else {
			sv.getDocument().set(""); //$NON-NLS-1$
		}
	}

}
