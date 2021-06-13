package com.tulin.v8.tomcat;

/*
 * (c) Copyright Sysdeo SA 2001, 2002.
 * All Rights Reserved.
 */

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tulin.v8.tomcat.editors.ProjectListEditor;

@SuppressWarnings("unused")
public class AdvancedPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, TomcatPluginResources {

	private BooleanFieldEditor securityEditor;

	static final private int FIELD_WIDTH = 50;
	private RadioGroupFieldEditor version;
	private DirectoryFieldEditor home;
	private DirectoryFieldEditor base;
	private FileFieldEditor configFile;
	private ProjectListEditor projectListEditor;
	// private ComboFieldEditor targetPerspectiveEditor;
	private ModifyListener fModifyListener;

	public AdvancedPreferencePage() {
		super();
		setPreferenceStore(TomcatLauncherPlugin.getDefault().getPreferenceStore());
	}

	/*
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, false));

		Group homeGroup = new Group(composite, SWT.NONE);

		base = new DirectoryFieldEditor(TomcatLauncherPlugin.TOMCAT_PREF_BASE_KEY,
				PREF_PAGE_BASE_LABEL, homeGroup);

		initLayoutAndData(homeGroup, 3);

		Group securityGroup = new Group(composite, SWT.NONE);
		securityEditor = new BooleanFieldEditor(
				TomcatLauncherPlugin.TOMCAT_PREF_SECURITYMANAGER,
				PREF_PAGE_SECURITYMANAGER_LABEL, securityGroup);
		this.initField(securityEditor);
		initLayoutAndData(securityGroup, 1);

		Group projectListGroup = new Group(composite, SWT.NONE);
		String[] excludedProjectsNature = { TomcatLauncherPlugin.NATURE_ID };
		projectListEditor = new ProjectListEditor(excludedProjectsNature);
		projectListEditor.setLabel(PREF_PAGE_PROJECTINCP_LABEL);
		Control projectList = projectListEditor.getControl(projectListGroup);
		GridData gd2 = new GridData();
		gd2.horizontalAlignment = GridData.FILL;
		projectList.setLayoutData(gd2);
		initLayoutAndData(projectListGroup, 1);

		this.initField(base);

		new Label(composite, SWT.NULL); // blank

		return composite;
	}

	/*
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	@SuppressWarnings("deprecation")
	public boolean performOk() {
		base.store();
		securityEditor.store();
		TomcatLauncherPlugin.getDefault().setProjectsInCP(
				projectListEditor.getCheckedElements());
		TomcatLauncherPlugin.getDefault().savePluginPreferences();
		return true;
	}

	@SuppressWarnings("deprecation")
	private void initField(FieldEditor field) {
		field.setPreferenceStore(getPreferenceStore());
		field.setPreferencePage(this);
		field.load();
	}

	private void initLayoutAndData(Group aGroup, int spanH, int spanV,
			int numColumns) {
		GridLayout gl = new GridLayout(numColumns, false);
		aGroup.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = spanH;
		gd.verticalSpan = spanV;
		aGroup.setLayoutData(gd);
	}

	private void initLayoutAndData(Group aGroup, int numColumns) {
		GridLayout gl = new GridLayout(numColumns, false);
		aGroup.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.widthHint = 400;
		aGroup.setLayoutData(gd);
	}

}
