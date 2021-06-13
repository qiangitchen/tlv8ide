/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.csv;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableManager;
import zigen.plugin.db.preference.CSVPreferencePage;
import zigen.plugin.db.ui.internal.ITable;


public class CreateCSVAction extends Action {

	private StructuredViewer viewer = null;

	private IPreferenceStore store;

	public CreateCSVAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("CreateCSVAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CreateCSVAction.1")); //$NON-NLS-1$
		this.store = DbPlugin.getDefault().getPreferenceStore();
	}


	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Object element = selection.getFirstElement();
		invoke(element);

		// if (fileName != null) {
		// IStructuredSelection selection = (IStructuredSelection)
		// viewer.getSelection();
		// for (Iterator iter = selection.iterator(); iter.hasNext();) {
		// Object obj = iter.next();
		// invoke(dialog.getPackageFragment(), obj);
		// }
		//
		// }
	}

	private void invoke(Object element) {
		if (element instanceof ITable) {
			ITable table = (ITable) element;
			try {
				Shell shell = DbPlugin.getDefault().getShell();
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				dialog.setFileName(table.getName());
				dialog.setFilterExtensions(new String[] {"*.csv", "*.*" //$NON-NLS-1$ //$NON-NLS-2$
				});
				dialog.setFilterNames(new String[] {Messages.getString("CreateCSVAction.4"), Messages.getString("CreateCSVAction.5") //$NON-NLS-1$ //$NON-NLS-2$
						});
				String fileName = dialog.open();

				if (fileName == null)
					return;

				File csvFile = new File(fileName);
				if (csvFile.exists()) {
					// Shell shell = DbPlugin.getDefault().getShell();
					MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
					msg.setMessage(fileName + Messages.getString("CreateCSVAction.6")); //$NON-NLS-1$
					msg.setText(Messages.getString("CreateCSVAction.7")); //$NON-NLS-1$
					int res2 = msg.open();
					if (res2 == SWT.NO)
						return;
				}

				CSVConfig config = new CSVConfig();

				String encoding = store.getString(CSVPreferencePage.P_ENCODING);
				String separator = store.getString(CSVPreferencePage.P_DEMILITER);
				boolean nonHeader = store.getBoolean(CSVPreferencePage.P_NON_HEADER);
				boolean nonDoubleQuate = store.getBoolean(CSVPreferencePage.P_NON_DOUBLE_QUATE);

				config.setQuery(TableManager.getSQLForCSV(table));
				config.setCsvEncoding(encoding);
				config.setSeparator(separator);
				config.setNonHeader(nonHeader);
				config.setNonDoubleQuate(nonDoubleQuate);
				config.setCsvFile(fileName);

				CSVWriter writer = new CSVWriter(table.getDbConfig(), config);
				writer.execute();

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			}
		}
	}

}
