/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;

public class CSVPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String P_ENCODING = "CSVPreferencePage.Encoding"; //$NON-NLS-1$

	public static final String P_DEMILITER = "CSVPreferencePage.Demiliter"; //$NON-NLS-1$

	public static final String P_NON_HEADER = "CSVPreferencePage.NonHeader"; //$NON-NLS-1$

	public static final String P_NON_DOUBLE_QUATE = "CSVPreferencePage.NonDoubleQuate"; //$NON-NLS-1$


	public static String[] encordes = {"MS932", //$NON-NLS-1$
			"ISO-8859-1", //$NON-NLS-1$
			"ISO2022JP", //$NON-NLS-1$
			"JIS", //$NON-NLS-1$
			"Shift-JIS", //$NON-NLS-1$
			"US-ASCII", //$NON-NLS-1$
			"UTF-16", //$NON-NLS-1$
			"UTF-16BE", //$NON-NLS-1$
			"UTF-16LE", //$NON-NLS-1$
			"UTF-8", //$NON-NLS-1$
			"EUC_JP" //$NON-NLS-1$
	};

	private Combo comb;

	private Text txtSeparator;

	private Button checkNonHeader;

	private Button checkNonDoubleQuate;

	public void init(IWorkbench workbench) {}

	public CSVPreferencePage() {
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

		Group group = new Group(base, SWT.NONE);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		group.setText(Messages.getString("CSVPreferencePage.12")); //$NON-NLS-1$

		Composite composite = new Composite(group, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.getString("CSVPreferencePage.13")); //$NON-NLS-1$

		comb = new Combo(composite, SWT.NONE);

		String defaultString = getPreferenceStore().getString(P_ENCODING);

		for (int i = 0; i < encordes.length; i++) {
			comb.add(encordes[i]);
			if (encordes[i].equals(defaultString)) {
				comb.select(i);
			}
		}

		if (comb.getSelectionIndex() == -1) {
			comb.add(defaultString, 0);
			comb.select(0);
		}

		Label label2 = new Label(composite, SWT.NONE);
		label2.setText(Messages.getString("CSVPreferencePage.7")); //$NON-NLS-1$
		label2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtSeparator = new Text(composite, SWT.BORDER);
		txtSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		defaultString = getPreferenceStore().getString(P_DEMILITER).trim();
		if (defaultString != null && !"".equals(defaultString)) { //$NON-NLS-1$
			txtSeparator.setText(defaultString);
		} else {
			txtSeparator.setText(","); //$NON-NLS-1$
		}
		checkNonHeader = new Button(composite, SWT.CHECK);
		checkNonHeader.setText(Messages.getString("CSVPreferencePage.9")); //$NON-NLS-1$
		GridData d = new GridData();
		d.horizontalSpan = 2;
		checkNonHeader.setLayoutData(d);

		boolean b = getPreferenceStore().getBoolean(P_NON_HEADER);
		checkNonHeader.setSelection(b);
		GridData d2 = new GridData();
		d2.horizontalSpan = 2;
		checkNonHeader.setLayoutData(d2);

		checkNonDoubleQuate = new Button(composite, SWT.CHECK);
		checkNonDoubleQuate.setText(Messages.getString("CSVPreferencePage.8")); //$NON-NLS-1$

		boolean b2 = getPreferenceStore().getBoolean(P_NON_DOUBLE_QUATE);
		checkNonDoubleQuate.setSelection(b2);

		return parent;
	}

	protected void performDefaults() {
		String defaultString = getPreferenceStore().getDefaultString(P_ENCODING);
		String demiliter = getPreferenceStore().getDefaultString(P_DEMILITER);
		boolean nonHeader = getPreferenceStore().getDefaultBoolean(P_NON_HEADER);
		boolean nonDoubleQuate = getPreferenceStore().getDefaultBoolean(P_NON_DOUBLE_QUATE);
		for (int i = 0; i < encordes.length; i++) {
			if (encordes[i].equals(defaultString)) {
				comb.select(i);
			}
		}
		txtSeparator.setText(demiliter);
		checkNonHeader.setSelection(nonHeader);
		checkNonDoubleQuate.setSelection(nonDoubleQuate);


		super.performDefaults();
	}

	public boolean performOk() {
		getPreferenceStore().setValue(P_ENCODING, comb.getText());
		getPreferenceStore().setValue(P_DEMILITER, txtSeparator.getText());
		getPreferenceStore().setValue(P_NON_HEADER, checkNonHeader.getSelection());
		getPreferenceStore().setValue(P_NON_DOUBLE_QUATE, checkNonDoubleQuate.getSelection());
		return super.performOk();
	}

}
