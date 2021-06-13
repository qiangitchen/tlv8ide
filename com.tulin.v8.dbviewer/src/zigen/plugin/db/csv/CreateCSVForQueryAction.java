/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.csv;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.CSVPreferencePage;
import zigen.plugin.db.ui.editors.IQueryViewEditor;


public class CreateCSVForQueryAction extends Action {

	protected IQueryViewEditor editor;

	private IPreferenceStore store;

	public CreateCSVForQueryAction() {
		this.setText(Messages.getString("CreateCSVForQueryAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CreateCSVForQueryAction.1")); //$NON-NLS-1$
		this.store = DbPlugin.getDefault().getPreferenceStore();
	}

	public void setActiveEditor(IQueryViewEditor queryEditor) {
		editor = queryEditor;
	}


	public void run() {
		invoke();

	}

	private void invoke() {

		try {
			Shell shell = DbPlugin.getDefault().getShell();
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFileName("query"); //$NON-NLS-1$
			dialog.setFilterExtensions(new String[] {"*.csv", "*.*" //$NON-NLS-1$ //$NON-NLS-2$
			});
			dialog.setFilterNames(new String[] {Messages.getString("CreateCSVForQueryAction.5"), Messages.getString("CreateCSVForQueryAction.6") //$NON-NLS-1$ //$NON-NLS-2$
					});
			String fileName = dialog.open();

			if (fileName == null)
				return;

			File csvFile = new File(fileName);
			if (csvFile.exists()) {
				// Shell shell = DbPlugin.getDefault().getShell();
				MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				msg.setMessage(fileName + Messages.getString("CreateCSVForQueryAction.7")); //$NON-NLS-1$
				msg.setText(Messages.getString("CreateCSVForQueryAction.8")); //$NON-NLS-1$
				int res2 = msg.open();
				if (res2 == SWT.NO)
					return;
			}

			CSVConfig config = new CSVConfig();

			String encoding = store.getString(CSVPreferencePage.P_ENCODING);
			String separator = store.getString(CSVPreferencePage.P_DEMILITER);
			boolean nonHeader = store.getBoolean(CSVPreferencePage.P_NON_HEADER);
			boolean nonDoubleQuate = store.getBoolean(CSVPreferencePage.P_NON_DOUBLE_QUATE);

			config.setQuery(editor.getQuery());
			config.setCsvEncoding(encoding);
			config.setSeparator(separator);
			config.setNonHeader(nonHeader);
			config.setNonDoubleQuate(nonDoubleQuate);

			config.setCsvFile(fileName);
			CSVWriter writer = new CSVWriter(editor.getDBConfig(), config);
			writer.execute();

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
