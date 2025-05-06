package com.tulin.v8.webtools.ide.preferencePages;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class JavaScriptEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private ColorFieldEditor colorComment;
	private ColorFieldEditor colorJsdoc;
	private ColorFieldEditor colorString;
	private ColorFieldEditor colorKeyword;

	public JavaScriptEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	protected void createFieldEditors() {
		setTitle(WebToolsPlugin.getResourceString("PreferencePage.JavaScript"));

		Composite parent = getFieldEditorParent();

		colorComment = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_JSCOMMENT,
				WebToolsPlugin.getResourceString("PreferencePage.JavaScriptCommentColor"), parent);
		addField(colorComment);

		colorJsdoc = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_JSDOC, "JSDoc", parent);
		addField(colorJsdoc);

		colorString = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_JSSTRING,
				WebToolsPlugin.getResourceString("PreferencePage.JavaScriptStringColor"), parent);
		addField(colorString);

		colorKeyword = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_JSKEYWORD,
				WebToolsPlugin.getResourceString("PreferencePage.JavaScriptKeywordColor"), parent);
		addField(colorKeyword);
	}

}
