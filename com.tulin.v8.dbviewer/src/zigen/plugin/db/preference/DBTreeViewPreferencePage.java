/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;

public class DBTreeViewPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String DESC = Messages.getString("DBTreeViewPreferencePage.0"); //$NON-NLS-1$

	public static final String P_DISPLAY_TBL_COMMENT = "DBTreeViewPreferencePage.DisplayTableComment"; //$NON-NLS-1$

	public static final String P_DISPLAY_COL_COMMENT = "DBTreeViewPreferencePage.DisplayColumnComment"; //$NON-NLS-1$

	public void init(IWorkbench workbench) {}

	public DBTreeViewPreferencePage() {
		super(GRID);
		super.setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());
		super.setDescription(DESC);
	}

	public void createFieldEditors() {
		addField(new BooleanFieldEditor(P_DISPLAY_TBL_COMMENT, Messages.getString("DBTreeViewPreferencePage.3"), getFieldEditorParent())); //$NON-NLS-1$
		addField(new BooleanFieldEditor(P_DISPLAY_COL_COMMENT, Messages.getString("DBTreeViewPreferencePage.4"), getFieldEditorParent())); //$NON-NLS-1$
	}

}
