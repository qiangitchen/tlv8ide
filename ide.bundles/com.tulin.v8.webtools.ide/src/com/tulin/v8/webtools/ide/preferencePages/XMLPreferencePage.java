package com.tulin.v8.webtools.ide.preferencePages;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * The preference page for the <code>XMLEditor</code>.
 */
public class XMLPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private IWorkbench workbench;
	private Button enableClassName;
	private List classNameAttrs;
	private Button addClassName;
	private Button removeClassName;

	public XMLPreferencePage() {
		super(WebToolsPlugin.getResourceString("PreferencePage.XML"));
		setPreferenceStore(WebToolsPlugin.getDefault().getPreferenceStore());
		// setDescription(WebToolsPlugin.getResourceString("PreferencePage.XML"));
	}

	/**
	 * Creates contents of the preference page.
	 * 
	 * @param parent the parent <code>Composite</code>
	 * @retrun the created <code>Control</code> which contains contents.
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(2, false));

		// checkbox to toggle the classname support
		enableClassName = new Button(composite, SWT.CHECK);
		enableClassName.setText(WebToolsPlugin.getResourceString("PreferencePage.EnableClassName"));
		enableClassName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateControls();
			}
		});
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		enableClassName.setLayoutData(gd);

		// listbox
		classNameAttrs = new List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		classNameAttrs.setLayoutData(new GridData(GridData.FILL_BOTH));
		classNameAttrs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateControls();
			}
		});

		Composite buttons = new Composite(composite, SWT.NULL);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		addClassName = new Button(buttons, SWT.PUSH);
		addClassName.setText(WebToolsPlugin.getResourceString("PreferencePage.AddAttribute"));
		addClassName.setLayoutData(createButtonGridData());
		addClassName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				InputDialog dialog = new InputDialog(workbench.getActiveWorkbenchWindow().getShell(),
						WebToolsPlugin.getResourceString("PreferencePage.Dialog.Title"),
						WebToolsPlugin.getResourceString("PreferencePage.Dialog.Message"), "",
						new IInputValidator() {
							public String isValid(String newText) {
								return newText.length() == 0
										? WebToolsPlugin.getResourceString("PreferencePage.Dialog.Error")
										: null;
							}
						});
				if (dialog.open() == InputDialog.OK) {
					classNameAttrs.add(dialog.getValue());
				}
			}
		});

		removeClassName = new Button(buttons, SWT.PUSH);
		removeClassName.setText(WebToolsPlugin.getResourceString("PreferencePage.RemoveAttribute"));
		removeClassName.setLayoutData(createButtonGridData());
		removeClassName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				classNameAttrs.remove(classNameAttrs.getSelectionIndices());
			}
		});

		// fill initial values
		IPreferenceStore store = getPreferenceStore();
		enableClassName.setSelection(store.getBoolean(WebToolsPlugin.PREF_ENABLE_CLASSNAME));
		String[] values = StringConverter.asArray(store.getString(WebToolsPlugin.PREF_CLASSNAME_ATTRS));
		for (int i = 0; i < values.length; i++) {
			classNameAttrs.add(values[i]);
		}

		updateControls();
		return composite;
	}

	/**
	 * Updates controls status.
	 */
	private void updateControls() {
		boolean enableClassName = this.enableClassName.getSelection();
		classNameAttrs.setEnabled(enableClassName);
		addClassName.setEnabled(enableClassName);
		removeClassName.setEnabled(enableClassName);
		if (enableClassName) {
			removeClassName.setEnabled(classNameAttrs.getSelectionCount() > 0);
		}
	}

	/**
	 * Creates the <code>GridData</code> for buttons.
	 * 
	 * @return the <code>GridData</code> which is configured for buttons
	 */
	private static GridData createButtonGridData() {
		GridData gd = new GridData();
		gd.widthHint = 120;
		return gd;
	}

	/**
	 * Initializes the preference page.
	 * 
	 * @param workbench the <code>IWorkbench</code> instance
	 */
	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		IPreferenceStore store = getPreferenceStore();
		enableClassName.setSelection(store.getDefaultBoolean(WebToolsPlugin.PREF_ENABLE_CLASSNAME));
		String[] values = StringConverter.asArray(store.getDefaultString(WebToolsPlugin.PREF_CLASSNAME_ATTRS));
		classNameAttrs.removeAll();
		for (int i = 0; i < values.length; i++) {
			classNameAttrs.add(values[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();
		store.setValue(WebToolsPlugin.PREF_ENABLE_CLASSNAME, enableClassName.getSelection());

		String[] items = classNameAttrs.getItems();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			if (i != 0) {
				sb.append(" ");
			}
			sb.append(items[i]);
		}
		store.setValue(WebToolsPlugin.PREF_CLASSNAME_ATTRS, sb.toString());

		return true;
	}

}
