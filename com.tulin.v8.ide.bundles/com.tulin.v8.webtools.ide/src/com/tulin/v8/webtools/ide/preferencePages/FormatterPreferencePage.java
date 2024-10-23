package com.tulin.v8.webtools.ide.preferencePages;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class FormatterPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Spinner indent;
	private Spinner lineChars;
	private Button useTab;

	public FormatterPreferencePage() {
		super(WebToolsPlugin.getResourceString("FormatterPreferencePage.Title"));
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
		setDescription(WebToolsPlugin.getResourceString("FormatterPreferencePage.Description"));
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));

		useTab = new Button(composite, SWT.CHECK);
		useTab.setText(WebToolsPlugin.getResourceString("FormatterPreferencePage.UseTab"));
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		useTab.setLayoutData(gd);
		useTab.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateStatus();
			}
		});

		new Label(composite, SWT.NULL).setText(WebToolsPlugin.getResourceString("FormatterPreferencePage.Indent"));
		indent = new Spinner(composite, SWT.BORDER);

		new Label(composite, SWT.NULL).setText(WebToolsPlugin.getResourceString("FormatterPreferencePage.LineChars"));
		lineChars = new Spinner(composite, SWT.BORDER);
		lineChars.setMaximum(1000);

		IPreferenceStore store = getPreferenceStore();
		useTab.setSelection(store.getBoolean(WebToolsPlugin.PREF_FORMATTER_TAB));
		indent.setSelection(store.getInt(WebToolsPlugin.PREF_FORMATTER_INDENT));
		lineChars.setSelection(store.getInt(WebToolsPlugin.PREF_FORMATTER_LINE));

		updateStatus();

		return composite;
	}

	private void updateStatus() {
		indent.setEnabled(!useTab.getSelection());
	}

	@Override
	protected void performDefaults() {
		IPreferenceStore store = getPreferenceStore();
		useTab.setSelection(store.getDefaultBoolean(WebToolsPlugin.PREF_FORMATTER_TAB));
		indent.setSelection(store.getDefaultInt(WebToolsPlugin.PREF_FORMATTER_INDENT));
		lineChars.setSelection(store.getDefaultInt(WebToolsPlugin.PREF_FORMATTER_LINE));
	}

	@Override
	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();
		store.setValue(WebToolsPlugin.PREF_FORMATTER_TAB, useTab.getSelection());
		store.setValue(WebToolsPlugin.PREF_FORMATTER_INDENT, indent.getSelection());
		store.setValue(WebToolsPlugin.PREF_FORMATTER_LINE, lineChars.getSelection());
		return true;
	}

}
