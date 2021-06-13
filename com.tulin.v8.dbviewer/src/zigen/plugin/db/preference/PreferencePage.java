/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String P_MAX_VIEW_RECORD = "PreferencePage.MaxViewRecord"; //$NON-NLS-1$

	public static final String P_NULL_SYMBOL = "PreferencePage.NullSymbol"; //$NON-NLS-1$

	public static final String P_CHANGE_NULL_COLOR = "PreferencePage.ChangeNullColor"; //$NON-NLS-1$

	public static final String P_MAX_HISTORY = "PreferencePage.MaxHistory"; //$NON-NLS-1$

	public static final String P_COLOR_BACKGROUND = "PreferencePage.BackGround";//$NON-NLS-1$

	public static final String P_CONNECT_TIMEOUT = "PreferencePage.ConnectTimeout"; //$NON-NLS-1$

	public static final String P_QUERY_TIMEOUT_FOR_COUNT = "PreferencePage.QueryTimeoutForCount"; //$NON-NLS-1$

	public static final String P_NO_CONFIRM_CONNECT_DB = "PreferencePage.NoConfirmConnectDB"; //$NON-NLS-1$

	public static final String P_SQL_FILE_CHARSET = "PreferencePage.SqlFileCharset"; //$NON-NLS-1$

	public static final String P_LOCKE_COLUMN_WIDTH = "PreferencePage.LogColumnWidth";//$NON-NLS-1$

	public void init(IWorkbench workbench) {}

	Combo comb;

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());
		setDescription(DbPluginConstant.TITLE + Messages.getString("PreferencePage.7")); //$NON-NLS-1$
	}

	private void addOption1(Composite parent) {

		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("PreferencePage.8")); //$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);

		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));
		addField(new IntegerFieldEditor(P_QUERY_TIMEOUT_FOR_COUNT, Messages.getString("PreferencePage.18"), grp, 5)); //$NON-NLS-1$
		addField(new IntegerFieldEditor(P_CONNECT_TIMEOUT, Messages.getString("PreferencePage.10"), grp, 5)); //$NON-NLS-1$
		Label label3 = new Label(grp, SWT.NONE);
		label3.setText(Messages.getString("PreferencePage.11")); //$NON-NLS-1$

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label3.setLayoutData(gd);

	}

	private void addOption2(Composite parent) {

		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("PreferencePage.12")); //$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);

		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		addField(new ColorFieldEditor(P_COLOR_BACKGROUND, Messages.getString("PreferencePage.1"), grp)); //$NON-NLS-1$
		addField(new StringFieldEditor(P_NULL_SYMBOL, Messages.getString("PreferencePage.0"), 10, grp)); //$NON-NLS-1$
		addField(new BooleanFieldEditor(P_CHANGE_NULL_COLOR, Messages.getString("PreferencePage.13"), grp)); //$NON-NLS-1$

		addField(new IntegerFieldEditor(P_MAX_VIEW_RECORD, Messages.getString("PreferencePage.14"), grp, 5)); //$NON-NLS-1$
		Label label1 = new Label(grp, SWT.NONE);
		label1.setText(Messages.getString("PreferencePage.15")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label1.setLayoutData(gd);

		addField(new BooleanFieldEditor(P_LOCKE_COLUMN_WIDTH, Messages.getString("PreferencePage.25"), grp)); //$NON-NLS-1$


	}

	private void addOption3(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("PreferencePage.16")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));
		addField(new IntegerFieldEditor(P_MAX_HISTORY, Messages.getString("PreferencePage.17"), grp, 5)); //$NON-NLS-1$

	}

	private void addOption4(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		// FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		// layout.marginHeight = 4;
		// layout.marginWidth = 4;
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		// layout.marginWidth = 0;
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		group.setLayout(layout);
		group.setText(Messages.getString("PreferencePage.19")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);

		GridLayout ly = new GridLayout(2, false);
		ly.marginHeight = 4;
		ly.marginWidth = 0;
		ly.horizontalSpacing = 2;
		ly.verticalSpacing = 2;

		Composite grp2 = new Composite(group, SWT.NONE);
		grp2.setLayout(ly);
		Label label = new Label(grp2, SWT.NONE);
		label.setText(Messages.getString("PreferencePage.21")); //$NON-NLS-1$
		comb = new Combo(grp2, SWT.NONE);
		String defaultString = getPreferenceStore().getString(P_SQL_FILE_CHARSET);
		for (int i = 0; i < CSVPreferencePage.encordes.length; i++) {
			comb.add(CSVPreferencePage.encordes[i]);
			if (CSVPreferencePage.encordes[i].equals(defaultString)) {
				comb.select(i);
			}
		}
		if (comb.getSelectionIndex() == -1) {
			comb.add(defaultString, 0);
			comb.select(0);
		}

		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(ly);
		addField(new BooleanFieldEditor(P_NO_CONFIRM_CONNECT_DB, Messages.getString("PreferencePage.20"), grp)); //$NON-NLS-1$

	}

	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		addOption1(parent);
		addOption2(parent);
		addOption3(parent);
		addOption4(parent);
	}

	protected void performDefaults() {
		String defaultString = getPreferenceStore().getDefaultString(P_SQL_FILE_CHARSET);
		for (int i = 0; i < CSVPreferencePage.encordes.length; i++) {
			if (CSVPreferencePage.encordes[i].equals(defaultString)) {
				comb.select(i);
			}
		}

		super.performDefaults();
	}

	public boolean performOk() {
		getPreferenceStore().setValue(P_SQL_FILE_CHARSET, comb.getText());
		return super.performOk();
	}

}
