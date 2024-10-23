package com.tulin.v8.webtools.ide.preferencePages;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * The preference page for the CSS editor.
 */
public class CSSEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private ColorFieldEditor colorComment;
	private ColorFieldEditor colorProperty;
	private ColorFieldEditor colorValue;

	public CSSEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	protected void createFieldEditors() {
		setTitle(WebToolsPlugin.getResourceString("PreferencePage.CSS"));

		Composite parent = getFieldEditorParent();

		colorComment = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_CSSCOMMENT,
				WebToolsPlugin.getResourceString("PreferencePage.CSSCommentColor"), parent);
		addField(colorComment);

		colorProperty = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_CSSPROP,
				WebToolsPlugin.getResourceString("PreferencePage.CSSPropColor"), parent);
		addField(colorProperty);

		colorValue = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_CSSVALUE,
				WebToolsPlugin.getResourceString("PreferencePage.CSSValueColor"), parent);
		addField(colorValue);
	}

}
