package com.tulin.v8.webtools.ide.preferencePages;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * The preference page to configure appearance of the HTML/JSP/XML editor.
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private ColorFieldEditor colorForeground;
	private SystemColorFieldEditor colorBackground;
	private ColorFieldEditor colorTag;
	private ColorFieldEditor colorAttr;
	private ColorFieldEditor colorValue;
	private ColorFieldEditor colorComment;
	private ColorFieldEditor colorDoctype;
	private ColorFieldEditor colorString;
	private ColorFieldEditor colorScriptlet;
//	private ColorFieldEditor colorCssProperty;
	private UseSoftTabFieldEditor useSoftTab;
	private SoftTabWidthFieldEditor softTabWidth;
	private RadioGroupFieldEditor editorType;
	private BooleanFieldEditor highlightPair;
	private BooleanFieldEditor showXMLErrors;
	private BooleanFieldEditor autoEdit;

	public PreferencePage() {
		super(GRID); // $NON-NLS-1$
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	protected void createFieldEditors() {
		setTitle(WebToolsPlugin.getResourceString("PreferencePage.Appearance"));

		Composite parent = getFieldEditorParent();

		colorForeground = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_FG,
				WebToolsPlugin.getResourceString("PreferencePage.ForegroundColor"), parent); // $NON-NLS-1$
		addField(colorForeground);

		colorBackground = new SystemColorFieldEditor(WebToolsPlugin.PREF_COLOR_BG, WebToolsPlugin.PREF_COLOR_BG_DEF,
				WebToolsPlugin.getResourceString("PreferencePage.BackgroundColor"), parent); // $NON-NLS-1$
		addField(colorBackground);

		colorTag = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_TAG,
				WebToolsPlugin.getResourceString("PreferencePage.TagColor"), parent); // $NON-NLS-1$
		addField(colorTag);

		colorAttr = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_ATTR,
				WebToolsPlugin.getResourceString("PreferencePage.AttrColor"), parent); // $NON-NLS-1$
		addField(colorAttr);

		colorValue = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_VALUE,
				WebToolsPlugin.getResourceString("PreferencePage.ValueColor"), parent); // $NON-NLS-1$
		addField(colorValue);

		colorComment = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_COMMENT,
				WebToolsPlugin.getResourceString("PreferencePage.CommentColor"), parent); // $NON-NLS-1$
		addField(colorComment);

		colorDoctype = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_DOCTYPE,
				WebToolsPlugin.getResourceString("PreferencePage.DocTypeColor"), parent); // $NON-NLS-1$
		addField(colorDoctype);

		colorString = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_STRING,
				WebToolsPlugin.getResourceString("PreferencePage.StringColor"), parent); // $NON-NLS-1$
		addField(colorString);

		colorScriptlet = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_SCRIPT,
				WebToolsPlugin.getResourceString("PreferencePage.ScriptColor"), parent); // $NON-NLS-1$
		addField(colorScriptlet);

//		colorCssProperty = new ColorFieldEditor(WebToolsPlugin.PREF_COLOR_CSSPROP,
//					WebToolsPlugin.getResourceString("PreferencePage.CSSPropColor"),
//					parent); //$NON-NLS-1$
//		addField(colorCssProperty);

		highlightPair = new BooleanFieldEditor(WebToolsPlugin.PREF_PAIR_CHAR,
				WebToolsPlugin.getResourceString("PreferencePage.PairCharacter"), parent);
		addField(highlightPair);

		showXMLErrors = new BooleanFieldEditor(WebToolsPlugin.PREF_SHOW_XML_ERRORS,
				WebToolsPlugin.getResourceString("PreferencePage.ShowXMLErrors"), parent);
		addField(showXMLErrors);

		autoEdit = new BooleanFieldEditor(WebToolsPlugin.PREF_AUTO_EDIT,
				WebToolsPlugin.getResourceString("PreferencePage.AutoInsert"), parent);
		addField(autoEdit);

		useSoftTab = new UseSoftTabFieldEditor(WebToolsPlugin.PREF_USE_SOFTTAB,
				WebToolsPlugin.getResourceString("PreferencePage.UseSoftTab"), parent);
		addField(useSoftTab);

		softTabWidth = new SoftTabWidthFieldEditor(WebToolsPlugin.PREF_SOFTTAB_WIDTH,
				WebToolsPlugin.getResourceString("PreferencePage.SoftTabWidth"), parent, 4);
		softTabWidth.setEnabled(getPreferenceStore().getBoolean(WebToolsPlugin.PREF_USE_SOFTTAB), parent);
		addField(softTabWidth);

		editorType = new RadioGroupFieldEditor(WebToolsPlugin.PREF_EDITOR_TYPE,
				WebToolsPlugin.getResourceString("PreferencePage.EditorType"), 1,
				new String[][] { { WebToolsPlugin.getResourceString("PreferencePage.EditorTab"), "tab" },
						{ WebToolsPlugin.getResourceString("PreferencePage.EditorSplitHor"), "horizontal" },
						{ WebToolsPlugin.getResourceString("PreferencePage.EditorSplitVer"), "vertical" },
						{ WebToolsPlugin.getResourceString("PreferencePage.EditorNoPreview"), "noPreview" } },
				parent, true);
		addField(editorType);
	}

	/** Background Color Field Editor */
	private class SystemColorFieldEditor extends ColorFieldEditor {

		private String booleanName = null;
		private Button colorButton;
		private Button checkbox;

		public SystemColorFieldEditor(String colorName, String booleanName, String labelText, Composite parent) {
			super(colorName, labelText, parent);
			this.booleanName = booleanName;
		}

		protected void doFillIntoGrid(Composite parent, int numColumns) {
			Control control = getLabelControl(parent);
			GridData gd = new GridData();
			gd.horizontalSpan = numColumns - 1;
			control.setLayoutData(gd);

			Composite composite = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(2, false);
			layout.horizontalSpacing = 5;
			layout.verticalSpacing = 0;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			composite.setLayout(layout);

			colorButton = getChangeControl(composite);
			gd = new GridData();
			int widthHint = convertHorizontalDLUsToPixels(colorButton, IDialogConstants.BUTTON_WIDTH);
			gd.widthHint = Math.max(widthHint, colorButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
			colorButton.setLayoutData(gd);

			checkbox = new Button(composite, SWT.CHECK);
			checkbox.setText(WebToolsPlugin.getResourceString("PreferencePage.SystemDefault"));
			checkbox.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					colorButton.setEnabled(!checkbox.getSelection());
				}
			});
		}

		protected void doLoad() {
			super.doLoad();
			checkbox.setSelection(getPreferenceStore().getBoolean(booleanName));
			colorButton.setEnabled(!checkbox.getSelection());
		}

		protected void doLoadDefault() {
			super.doLoadDefault();
			checkbox.setSelection(getPreferenceStore().getDefaultBoolean(booleanName));
			colorButton.setEnabled(!checkbox.getSelection());
		}

		protected void doStore() {
			super.doStore();
			getPreferenceStore().setValue(booleanName, checkbox.getSelection());
		}
	}

	/** Soft Tab Field Editor */
	private class UseSoftTabFieldEditor extends BooleanFieldEditor {

		private Composite parent;

		public UseSoftTabFieldEditor(String name, String label, Composite parent) {
			super(name, label, parent);
			this.parent = parent;
		}

		protected void valueChanged(boolean oldValue, boolean newValue) {
			super.valueChanged(oldValue, newValue);
			softTabWidth.setEnabled(newValue, parent);
		}
	}

	/** Soft Tab Width Field Listener */
	private class SoftTabWidthFieldEditor extends IntegerFieldEditor {
		public SoftTabWidthFieldEditor(String name, String labelText, Composite parent, int textLimit) {
			super(name, labelText, parent, textLimit);
		}

		protected void doFillIntoGrid(Composite parent, int numColumns) {
			super.doFillIntoGrid(parent, numColumns);
			GridData gd = (GridData) getTextControl().getLayoutData();
			gd.horizontalAlignment = 0;
			gd.widthHint = 40;
		}
	}
}
