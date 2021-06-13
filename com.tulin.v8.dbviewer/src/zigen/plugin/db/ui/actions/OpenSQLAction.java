/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.InputStreamUtil;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class OpenSQLAction extends SQLSourceViewerAction {

	public OpenSQLAction(SQLSourceViewer viewer) {
		super(viewer);
		this.setText(Messages.getString("OpenSQLAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("OpenSQLAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));

	}

	public void run() {
		try {
			Shell shell = DbPlugin.getDefault().getShell();
			// Shell shell = viewer.getControl().getShell();
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
			if (fSQLSourceViewer.getSqlFileName() != null) {
				dialog.setFileName(fSQLSourceViewer.getSqlFileName());
			}
			dialog.setFilterExtensions(new String[] {"*.sql", "*.*" //$NON-NLS-1$ //$NON-NLS-2$
			});
			dialog.setFilterNames(new String[] {Messages.getString("OpenSQLAction.4"), Messages.getString("OpenSQLAction.5") //$NON-NLS-1$ //$NON-NLS-2$
					});
			String fileName = dialog.open();

			if (fileName == null)
				return;

			File file = new File(fileName);

			String charset = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_SQL_FILE_CHARSET);
			if (file.exists() && file.canRead()) {
				String sqlData = InputStreamUtil.toString(new FileInputStream(file), charset);
				fSQLSourceViewer.getDocument().set(sqlData);

				fSQLSourceViewer.setSqlFile(file);
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}


}
