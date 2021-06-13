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
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.views.TreeContentProvider;

public class EditDBAction extends Action implements Runnable {

	TreeViewer viewer = null;

	public EditDBAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("EditDBAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("EditDBAction.1")); //$NON-NLS-1$
		this.setEnabled(true);
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_DB_EDIT));
	}

	public void run() {
		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof DataBase) {

			DataBase db = (DataBase) element;

			DataBase oldDB = (DataBase) db.clone();


			Shell shell = DbPlugin.getDefault().getShell();
			DBConfigWizard wizard = new DBConfigWizard(viewer.getSelection(), db.getDbConfig());
			WizardDialog dialog2 = new WizardDialog(shell, wizard);
			int ret = dialog2.open();

			if (ret == IDialogConstants.OK_ID) {
				IDBConfig newConfig = wizard.getNewConfig();

				db.setDbConfig(newConfig);
				viewer.refresh(db);

				IContentProvider cp = viewer.getContentProvider();
				if (cp instanceof TreeContentProvider) {
					TreeContentProvider tcp = (TreeContentProvider) cp;
					BookmarkRoot bmroot = tcp.getBookmarkRoot();

					updateBookmark(bmroot, oldDB, db);

				}
				viewer.getControl().notifyListeners(SWT.Selection, null);

				DbPlugin.fireStatusChangeListener(newConfig, IStatusChangeListener.EVT_AddSchemaFilter);

				DbPlugin.fireStatusChangeListener(viewer, IStatusChangeListener.EVT_UpdateDataBaseList);


			}

		}
	}

	private void updateBookmark(BookmarkFolder folder, DataBase targetDataBase, DataBase newDataBase) {
		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof Bookmark) {
				Bookmark bm = (Bookmark) leaf;
				if (bm.getDataBase().equals(targetDataBase)) {
					bm.setDataBase(newDataBase);
				}
			} else if (leaf instanceof BookmarkFolder) {
				updateBookmark((BookmarkFolder) leaf, targetDataBase, newDataBase);
			}
		}
	}

}
