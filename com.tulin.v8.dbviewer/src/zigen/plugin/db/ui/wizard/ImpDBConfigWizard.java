/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.wizard;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SameDbNameException;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.views.TreeContentProvider;


public class ImpDBConfigWizard extends Wizard {

	private ImpWizardPage1 page1;

	private TreeViewer viewer;

	private IDBConfig[] configs;

	public ImpDBConfigWizard(TreeViewer viewer, IDBConfig[] configs) {
		super();
		this.viewer = viewer;
		this.configs = configs;
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page1 = new ImpWizardPage1(configs);
		addPage(page1);
	}

	public boolean performFinish() {
		try {
			TableItem[] tableItems = this.page1.tableItems;
			for (int i = 0; i < tableItems.length; i++) {
				TableItem item = tableItems[i];
				if (item.isChecked()) {
					saveDBConfig(item.getConfig());

				}
			}

			DbPlugin.getDefault().saveDBDialogSettings();

			return true;
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
			return false;
		}

	}

	public boolean canFinish() {
		if (page1.isPageComplete()) {
			return true;
		} else {
			return false;
		}
	}

	private void saveDBConfig(IDBConfig config) {
		try {
			DBConfigManager.save(config);

			IContentProvider obj = viewer.getContentProvider();
			if (obj instanceof TreeContentProvider) {
				TreeContentProvider provider = (TreeContentProvider) obj;
				DataBase registDb = provider.addDataBase(config);
				viewer.expandToLevel(provider.getRoot(), 1);
				viewer.refresh();

				viewer.setSelection(new StructuredSelection(registDb), true);
			}
		} catch (SameDbNameException e) {
			config.setDbName(config.getDbName() + Messages.getString("ImpDBConfigWizard.0")); //$NON-NLS-1$
			saveDBConfig(config);
		}
	}

}
