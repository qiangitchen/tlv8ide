/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import zigen.plugin.db.DbPlugin;

public class CodeAssistPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String MODE_NONE = "0"; //$NON-NLS-1$

	public static final String MODE_KEYWORD = "1"; //$NON-NLS-1$

	public static final String MODE_PARSE = "2"; //$NON-NLS-1$

	public static final String DESC = Messages.getString("CodeAssistPreferencePage.3"); //$NON-NLS-1$

	public static final String P_SQL_CODE_ASSIST_MODE = "CodeAssistPreferencePage.SqlCodeAssitModde"; //$NON-NLS-1$

	public static final String P_SQL_CODE_ASSIST_CACHE_TIME = "CodeAssistPreferencePage.SqlCodeAssitCacheTime"; //$NON-NLS-1$

	public static final String P_SQL_CODE_ASSIST_AUTO_ACTIVATE_DELAY_TIME = "CodeAssistPreferencePage.SqlCodeAssistAutoActivateDelayTime"; //$NON-NLS-1$

	public void init(IWorkbench workbench) {}

	public CodeAssistPreferencePage() {
		super(GRID);
		super.setPreferenceStore(DbPlugin.getDefault().getPreferenceStore());
		// super.setDescription(DESC);
	}

	public void createFieldEditors() {
		String[][] labelAndValue = new String[][] {new String[] {Messages.getString("CodeAssistPreferencePage.7"), "0" //$NON-NLS-1$ //$NON-NLS-2$
		}, new String[] {Messages.getString("CodeAssistPreferencePage.9"), "1" //$NON-NLS-1$ //$NON-NLS-2$
		}, new String[] {Messages.getString("CodeAssistPreferencePage.11"), "2" //$NON-NLS-1$ //$NON-NLS-2$
		},};

		addField(new RadioGroupFieldEditor(P_SQL_CODE_ASSIST_MODE, Messages.getString("CodeAssistPreferencePage.2"), labelAndValue.length, labelAndValue, //$NON-NLS-1$
				getFieldEditorParent()));

		addField(new IntegerFieldEditor(P_SQL_CODE_ASSIST_CACHE_TIME, Messages.getString("CodeAssistPreferencePage.1"), getFieldEditorParent(), 5)); //$NON-NLS-1$

		addField(new IntegerFieldEditor(P_SQL_CODE_ASSIST_AUTO_ACTIVATE_DELAY_TIME, Messages.getString("CodeAssistPreferencePage.0"), getFieldEditorParent(), 5)); //$NON-NLS-1$

	}

}
