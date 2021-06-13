/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.StringUtil;

public class SQLFormatPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, ModifyListener {

	public static final String P_USE_FORMATTER_TYPE = "SQLFormatPreferencePage.UseFormatterType"; //$NON-NLS-1$

	public static final String P_FORMAT_PATCH = "SQLEditorPreferencePage.FormatPatch"; //$NON-NLS-1$

	public static final String P_FORMAT_OPTION_TABSIZE = "SQLEditorPreferencePage.FormatTabSize"; //$NON-NLS-1$

	public static final String P_FORMAT_OPTION_DECODE = "SQLEditorPreferencePage.FormatOptionDecode"; //$NON-NLS-1$

	public static final String P_FORMAT_OPTION_IN = "SQLEditorPreferencePage.FormatOptionIn"; //$NON-NLS-1$

	public static final String P_MAX_SQL_COUNT = "SQLEditorPreferencePage.MaxSqlCount"; //$NON-NLS-1$

	public static final int TYPE_DBVIEWER = 0;

	public static final int TYPE_BLANCO = 1;

	private Button formatterTypeBlanco;

	private Button formatterTypeKry;

	private Button formatterPatchForBlanco;

	private Label label;

	private Text maxText = null;

	public void init(IWorkbench workbench) {}

	public SQLFormatPreferencePage() {
		super();
		super.setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());

	}

	public void createControl(Composite parent) {
		super.createControl(parent);
	}

	protected Control createContents(Composite parent) {
		Composite base = new Composite(parent, SWT.NONE);
		base.setLayout(new GridLayout(1, false));
		base.setLayoutData(new GridData(GridData.FILL_BOTH));
		addCommon(base);

		addTypeOption(base);
		addOption3(base);
		addOption4(base);

		updateCurrentSetting();

		return parent;
	}

	private void addCommon(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText("Common Option"); //$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));
		Label label = new Label(grp, SWT.NONE);
		label.setText("Format Max SQL:");
		maxText = new Text(grp, SWT.BORDER);
		maxText.setTextLimit(5);
		maxText.addModifyListener(this);
		gd = new GridData();
		gd.widthHint = 100;
		maxText.setLayoutData(gd);

	}

	private void addTypeOption(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("SQLFormatPreferencePage.2")); //$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		formatterTypeKry = new Button(grp, SWT.RADIO);
		formatterTypeKry.setText(Messages.getString("SQLFormatPreferencePage.0")); //$NON-NLS-1$
		formatterTypeKry.setSelection(true);
		formatterTypeKry.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setEnableForDBV(true);
				setEnableForBlanco(false);
			}

		});

		formatterTypeBlanco = new Button(grp, SWT.RADIO);
		formatterTypeBlanco.setText(Messages.getString("SQLFormatPreferencePage.1")); //$NON-NLS-1$
		formatterTypeBlanco.setSelection(false);
		formatterTypeBlanco.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setEnableForDBV(false);
				setEnableForBlanco(true);
			}

		});

	}

	private void addOption3(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText("BlancoFormatter Option"); //$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(1, false));

		StringBuffer sb = new StringBuffer();
		sb.append(Messages.getString("SQLEditorPreferencePage.36")); //$NON-NLS-1$
		formatterPatchForBlanco = new Button(grp, SWT.CHECK);
		formatterPatchForBlanco.setText(sb.toString());
		formatterPatchForBlanco.setEnabled(false);
		formatterPatchForBlanco.setLayoutData(new GridData(GridData.END));
		label = new Label(grp, SWT.NONE);
		label.setText(Messages.getString("SQLEditorPreferencePage.35")); //$NON-NLS-1$
		label.setEnabled(false);
		label.setLayoutData(new GridData(GridData.END));

	}

	private Button tabSize2;

	private Button tabSize4;

	private Button specialFormatDecode;

	private Button specialFormatIn;

	private void addOption4(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText("DBViewer SQL Formatter Option"); //$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		tabSize2 = new Button(grp, SWT.RADIO);
		tabSize2.setText(Messages.getString("SQLFormatPreferencePage.3")); //$NON-NLS-1$
		tabSize2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tabSize4 = new Button(grp, SWT.RADIO);
		tabSize4.setText(Messages.getString("SQLFormatPreferencePage.4")); //$NON-NLS-1$
		tabSize4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		specialFormatDecode = new Button(grp, SWT.CHECK);
		specialFormatDecode.setText(Messages.getString("SQLFormatPreferencePage.5"));
		// //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		specialFormatDecode.setLayoutData(gd);

		specialFormatIn = new Button(grp, SWT.CHECK);
		specialFormatIn.setText(Messages.getString("SQLFormatPreferencePage.6")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		specialFormatIn.setLayoutData(gd);
	}

	private void setEnableForDBV(boolean b) {
		tabSize2.setEnabled(b);
		tabSize4.setEnabled(b);
		specialFormatDecode.setEnabled(b);
		specialFormatIn.setEnabled(b);
	}

	private void setEnableForBlanco(boolean b) {
		formatterPatchForBlanco.setEnabled(b);
		label.setEnabled(b);
	}

	private void updateCurrentSetting() {

		String maxCount = getPreferenceStore().getString(P_MAX_SQL_COUNT);
		int type = getPreferenceStore().getInt(P_USE_FORMATTER_TYPE);
		boolean onPatch = getPreferenceStore().getBoolean(P_FORMAT_PATCH);
		int tabSize = getPreferenceStore().getInt(P_FORMAT_OPTION_TABSIZE);
		boolean optionDecode = getPreferenceStore().getBoolean(P_FORMAT_OPTION_DECODE);
		boolean optionIn = getPreferenceStore().getBoolean(P_FORMAT_OPTION_IN);


		maxText.setText(maxCount);

		switch (type) {
		case TYPE_DBVIEWER:
			formatterTypeKry.setSelection(true);
			setEnableForDBV(true);
			formatterTypeBlanco.setSelection(false);
			setEnableForBlanco(false);

			break;
		case TYPE_BLANCO:
			formatterTypeKry.setSelection(false);
			setEnableForDBV(false);
			formatterTypeBlanco.setSelection(true);
			setEnableForBlanco(true);
			break;
		default:
			break;
		}
		formatterPatchForBlanco.setSelection(onPatch);

		if (tabSize == 2) {
			tabSize2.setSelection(true);
			tabSize4.setSelection(false);
		} else {
			tabSize2.setSelection(false);
			tabSize4.setSelection(true);
		}
		specialFormatDecode.setSelection(optionDecode);
		specialFormatIn.setSelection(optionIn);
	}

	private void updateDefaultSetting() {

		String maxCount = getPreferenceStore().getDefaultString(P_MAX_SQL_COUNT);
		int type = getPreferenceStore().getDefaultInt(P_USE_FORMATTER_TYPE);
		boolean onPatch = getPreferenceStore().getDefaultBoolean(P_FORMAT_PATCH);
		int tabSize = getPreferenceStore().getDefaultInt(P_FORMAT_OPTION_TABSIZE);
		boolean optionDecode = getPreferenceStore().getDefaultBoolean(P_FORMAT_OPTION_DECODE);
		boolean optionIn = getPreferenceStore().getDefaultBoolean(P_FORMAT_OPTION_IN);

		maxText.setText(maxCount);

		switch (type) {
		case TYPE_DBVIEWER:
			formatterTypeKry.setSelection(true);
			setEnableForDBV(true);
			formatterTypeBlanco.setSelection(false);
			setEnableForBlanco(false);

			break;
		case TYPE_BLANCO:
			formatterTypeKry.setSelection(false);
			setEnableForDBV(false);
			formatterTypeBlanco.setSelection(true);
			setEnableForBlanco(true);
			break;
		default:
			break;
		}
		formatterPatchForBlanco.setSelection(onPatch);
		if (tabSize == 2) {
			tabSize2.setSelection(true);
			tabSize4.setSelection(false);
		} else {
			tabSize2.setSelection(false);
			tabSize4.setSelection(true);
		}
		specialFormatDecode.setSelection(optionDecode);
		specialFormatIn.setSelection(optionIn);
	}

	protected void performDefaults() {
		updateDefaultSetting();
		super.performDefaults();
	}

	public boolean performOk() {
		getPreferenceStore().setValue(P_MAX_SQL_COUNT, maxText.getText());
		getPreferenceStore().setValue(P_FORMAT_PATCH, formatterPatchForBlanco.getSelection());

		if (tabSize2.getSelection()) {
			getPreferenceStore().setValue(P_FORMAT_OPTION_TABSIZE, 2);
		} else {
			getPreferenceStore().setValue(P_FORMAT_OPTION_TABSIZE, 4);
		}

		getPreferenceStore().setValue(P_FORMAT_OPTION_DECODE, specialFormatDecode.getSelection());
		getPreferenceStore().setValue(P_FORMAT_OPTION_IN, specialFormatIn.getSelection());

		if (formatterTypeKry.getSelection()) {
			getPreferenceStore().setValue(P_USE_FORMATTER_TYPE, TYPE_DBVIEWER);
		} else {
			getPreferenceStore().setValue(P_USE_FORMATTER_TYPE, TYPE_BLANCO);
		}
		return super.performOk();
	}

	public void modifyText(ModifyEvent e) {
		if (!StringUtil.isNumeric(maxText.getText())) {
			updateStatus(JFaceResources.getString("IntegerFieldEditor.errorMessage"));
			return;
		}

		if (maxText.getText().length() == 0) {
			updateStatus(JFaceResources.getString("IntegerFieldEditor.errorMessage"));
			return;
		}

		updateStatus(null);

	}

	protected void updateStatus(String message) {
		setErrorMessage(message);
		setValid(message == null);
	}

}
