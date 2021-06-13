/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.io.File;
import java.util.NoSuchElementException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.XMLManager;
import zigen.plugin.db.ui.wizard.ImpDBConfigWizard;

public class ImportDBConfigAction extends Action implements Runnable {

	TreeViewer viewer;

	public ImportDBConfigAction(TreeViewer viewer) {
		this.viewer = viewer;

		this.setText(Messages.getString("ImportDBConfigAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("ImportDBConfigAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_IMPORT));

	}

	public void run() {

		try {
			Shell shell = DbPlugin.getDefault().getShell();
			File file = save(shell, null);
			if (file != null) {
				Object xml = loadXml(file);
				if (xml instanceof IDBConfig[]) {
					IDBConfig[] configs = (IDBConfig[]) xml;
					ImpDBConfigWizard wizard = new ImpDBConfigWizard(viewer, configs);
					WizardDialog dialog = new WizardDialog(shell, wizard);
					dialog.open();

					DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);

				} else {
					DbPlugin.getDefault().showWarningMessage(Messages.getString("ImportDBConfigAction.3")); // throw
					// //$NON-NLS-1$
				}

			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private Object loadXml(File file) throws Exception {
		try {
			return XMLManager.load(file);
		} catch (NoSuchElementException e) {

			throw new Exception(Messages.getString("ImportDBConfigAction.4")); //$NON-NLS-1$
		}
	}

	private File save(Shell shell, String defaultFileName) {
		FileDialog dialog = new FileDialog(shell, SWT.ON_TOP);
		dialog.setFileName(defaultFileName);
		dialog.setFilterExtensions(new String[] {"*.xml", "*.*" //$NON-NLS-1$ //$NON-NLS-2$
		});
		dialog.setFilterNames(new String[] {Messages.getString("ImportDBConfigAction.7"), Messages.getString("ImportDBConfigAction.8") //$NON-NLS-1$ //$NON-NLS-2$
				});
		String fileName = dialog.open();
		if (fileName != null) {
			return new File(fileName);
		} else {
			return null;
		}
	}
}
