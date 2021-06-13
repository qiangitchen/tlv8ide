/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;

public class SQLEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String DESC = Messages.getString("SQLEditorPreferencePage.0"); //$NON-NLS-1$

	public static final String P_COLOR_KEYWORD = "SQLEditorPreferencePage.ColorKeyword"; //$NON-NLS-1$

	public static final String P_COLOR_FUNCTION = "SQLEditorPreferencePage.ColorFunction"; //$NON-NLS-1$

	public static final String P_COLOR_COMMENT = "SQLEditorPreferencePage.ColorComment"; //$NON-NLS-1$

	public static final String P_COLOR_STRING = "SQLEditorPreferencePage.ColorString"; //$NON-NLS-1$

	public static final String P_COLOR_DEFAULT = "SQLEditorPreferencePage.ColorDefailt"; //$NON-NLS-1$

	public static final String P_COLOR_BACK = "SQLEditorPreferencePage.ColorBackGround"; //$NON-NLS-1$

	public static final String P_COLOR_SELECT_FORE = "SQLEditorPreferencePage.ColorSelectFore"; //$NON-NLS-1$

	public static final String P_COLOR_SELECT_BACK = "SQLEditorPreferencePage.ColorSelectBack"; //$NON-NLS-1$

	public static final String P_COLOR_MATCHING = "SQLEditorPreferencePage.ColorMatching"; //$NON-NLS-1$

	public static final String P_COLOR_FIND_SCOPE = "SQLEditorPreferencePage.ColorFindScope"; //$NON-NLS-1$

	public static final String P_COLOR_CURSOR_LINE = "SQLEditorPreferencePage.CursorLine"; //$NON-NLS-1$

	// public static final String P_LINE_DEMILITER =
	// "SQLEditorPreferencePage.LineDemiliter";

	public static final String P_SQL_DEMILITER = "SQLEditorPreferencePage.SqlDemiliter"; //$NON-NLS-1$

	// public static final String P_FORMAT_PATCH = "SQLEditorPreferencePage.FormatPatch"; //$NON-NLS-1$
	

	public static final String P_STYLE_KEYWORD = "SQLEditorPreferencePage.StyleKeyword"; //$NON-NLS-1$
	
	public static final String P_STYLE_FUNCTION = "SQLEditorPreferencePage.StyleFunction"; //$NON-NLS-1$
	

	public void init(IWorkbench workbench) {}

	public SQLEditorPreferencePage() {
		super(GRID);
		super.setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());
		super.setDescription(DESC);
	}

	private String[][] radioLine = new String[][] {new String[] {"CR+LF (Windows)", "\r\n" //$NON-NLS-1$ //$NON-NLS-2$
	}, new String[] {"LF (Unix)", "\n" //$NON-NLS-1$ //$NON-NLS-2$
	}, new String[] {"CR (Mac)", "\r" //$NON-NLS-1$ //$NON-NLS-2$
	},};

	private String[][] radioSql = new String[][] {new String[] {Messages.getString("SQLEditorPreferencePage.18"), "/" //$NON-NLS-1$ //$NON-NLS-2$
	}, new String[] {Messages.getString("SQLEditorPreferencePage.20"), ";" //$NON-NLS-1$ //$NON-NLS-2$
	}};

	private String[][] radioStyle = new String[][] {
			new String[] { Messages.getString("SQLEditorPreferencePage.1"), String.valueOf(SWT.NORMAL) }, //$NON-NLS-1$
			new String[] { Messages.getString("SQLEditorPreferencePage.2"), String.valueOf(SWT.ITALIC) }, //$NON-NLS-1$
			new String[] { Messages.getString("SQLEditorPreferencePage.3"), String.valueOf(SWT.BOLD) }, //$NON-NLS-1$
			new String[] { Messages.getString("SQLEditorPreferencePage.4"), String.valueOf(SWT.ITALIC|SWT.BOLD) },}; //$NON-NLS-1$

	
	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		addOption1(parent);
		addOption2(parent);
		addOption3(parent);
	}

	private void addOption1(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("SQLEditorPreferencePage.22")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		addField(new ColorFieldEditor(P_COLOR_KEYWORD, Messages.getString("SQLEditorPreferencePage.23"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_FUNCTION, Messages.getString("SQLEditorPreferencePage.38"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_STRING, Messages.getString("SQLEditorPreferencePage.24"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_COMMENT, Messages.getString("SQLEditorPreferencePage.25"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_DEFAULT, Messages.getString("SQLEditorPreferencePage.26"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_BACK, Messages.getString("SQLEditorPreferencePage.27"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_SELECT_FORE, Messages.getString("SQLEditorPreferencePage.28"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_SELECT_BACK, Messages.getString("SQLEditorPreferencePage.29"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_MATCHING, Messages.getString("SQLEditorPreferencePage.30"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_FIND_SCOPE, Messages.getString("SQLEditorPreferencePage.34"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(P_COLOR_CURSOR_LINE, Messages.getString("SQLEditorPreferencePage.37"), grp)); //$NON-NLS-1$

	}

	private void addOption2(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("SQLEditorPreferencePage.33")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		// addField(new RadioGroupFieldEditor(P_LINE_DEMILITER,
		addField(new RadioGroupFieldEditor(P_SQL_DEMILITER, Messages.getString("SQLEditorPreferencePage.32"), radioSql.length, radioSql, grp)); //$NON-NLS-1$

	}
	
	private void addOption3(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("SQLEditorPreferencePage.5")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		addField(new RadioGroupFieldEditor(P_STYLE_KEYWORD, Messages.getString("SQLEditorPreferencePage.6"), radioStyle.length, radioStyle, grp)); //$NON-NLS-1$
		addField(new RadioGroupFieldEditor(P_STYLE_FUNCTION, Messages.getString("SQLEditorPreferencePage.7"), radioStyle.length, radioStyle, grp)); //$NON-NLS-1$

	}
}
