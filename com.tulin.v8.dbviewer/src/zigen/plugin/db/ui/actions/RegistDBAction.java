/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.dialogs.DBConfigWizard;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.views.TreeContentProvider;

public class RegistDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public RegistDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("RegistDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RegistDBAction.1")); //$NON-NLS-1$

		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_ADD));

	}

	public void run() {

		Shell shell = DbPlugin.getDefault().getShell();
		DBConfigWizard wizard = new DBConfigWizard(viewer.getSelection());
		WizardDialog dialog2 = new WizardDialog(shell, wizard);
		int ret = dialog2.open();

		if (ret == IDialogConstants.OK_ID) {
			IDBConfig newConfig = wizard.getNewConfig();

			IContentProvider obj = viewer.getContentProvider();
			if (obj instanceof TreeContentProvider) {
				TreeContentProvider provider = (TreeContentProvider) obj;
				DataBase registDb = provider.addDataBase(newConfig);

				viewer.refresh();

				viewer.setSelection(new StructuredSelection(registDb), true);
			}

			viewer.getControl().notifyListeners(SWT.Selection, null);

			DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);
		}


	}

}
