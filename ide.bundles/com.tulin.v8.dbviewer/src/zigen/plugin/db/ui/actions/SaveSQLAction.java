/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class SaveSQLAction extends SQLSourceViewerAction {

	public SaveSQLAction(SQLSourceViewer viewer) {
		super(viewer);
		this.setText(Messages.getString("SaveSQLAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("SaveSQLAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_SAVE));

	}

	public void run() {
		try {
			Shell shell = DbPlugin.getDefault().getShell();
			// Shell shell = viewer.getControl().getShell();
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);

			if (fSQLSourceViewer.getSqlFileName() != null) {
				dialog.setFileName(fSQLSourceViewer.getSqlFileName());
			}

			dialog.setFilterExtensions(new String[] {"*.sql", "*.*" //$NON-NLS-1$ //$NON-NLS-2$
			});
			dialog.setFilterNames(new String[] {Messages.getString("SaveSQLAction.3"), Messages.getString("SaveSQLAction.4") //$NON-NLS-1$ //$NON-NLS-2$
					});
			String fileName = dialog.open();

			if (fileName == null)
				return;

			File file = new File(fileName);
			if (file.exists()) {
				MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				msg.setMessage(fileName + Messages.getString("SaveSQLAction.5")); //$NON-NLS-1$
				msg.setText(Messages.getString("SaveSQLAction.6")); //$NON-NLS-1$
				int res2 = msg.open();
				if (res2 == SWT.NO)
					return;
			}

			write(fileName, fSQLSourceViewer.getDocument().get());

			fSQLSourceViewer.setSqlFile(file);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	public void write(String fileName, String saveData) throws Exception {
		PrintStream pout = null;
		try {

			String charset = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_SQL_FILE_CHARSET);
			pout = new PrintStream(new FileOutputStream(fileName), true, charset);
			pout.print(saveData);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			if (pout != null)
				pout.close();
		}

	}

}
