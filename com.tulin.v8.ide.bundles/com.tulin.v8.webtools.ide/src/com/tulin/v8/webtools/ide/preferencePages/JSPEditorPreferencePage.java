package com.tulin.v8.webtools.ide.preferencePages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class JSPEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public JSPEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	protected void createFieldEditors() {
		setTitle(WebToolsPlugin.getResourceString("PreferencePage.JSP"));

		Composite parent = getFieldEditorParent();

		addField(new ColorFieldEditor(WebToolsPlugin.PREF_JSP_COMMENT,
				WebToolsPlugin.getResourceString("PreferencePage.JSPCommentColor"), parent));

		addField(new ColorFieldEditor(WebToolsPlugin.PREF_JSP_STRING,
				WebToolsPlugin.getResourceString("PreferencePage.JSPStringColor"), parent));

		addField(new ColorFieldEditor(WebToolsPlugin.PREF_JSP_KEYWORD,
				WebToolsPlugin.getResourceString("PreferencePage.JSPKeywordColor"), parent));

		addField(new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_TAGLIB,
				WebToolsPlugin.getResourceString("PreferencePage.JSPTaglibColor"), parent));

		addField(new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_TAGLIB_ATTR,
				WebToolsPlugin.getResourceString("PreferencePage.JSPTaglibAttrColor"), parent));

		addField(new BooleanFieldEditor(WebToolsPlugin.PREF_JSP_FIX_PATH,
				WebToolsPlugin.getResourceString("PreferencePage.JSPFixPath"), parent));
	}

}
