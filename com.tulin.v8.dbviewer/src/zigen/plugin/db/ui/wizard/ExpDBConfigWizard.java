/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.XMLManager;
import zigen.plugin.db.ui.internal.Root;

public class ExpDBConfigWizard extends Wizard {

	private ExpWizardPage1 page1;

	private Root root;

	public ExpDBConfigWizard(Root root) {
		super();
		this.root = root;
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page1 = new ExpWizardPage1(root);
		addPage(page1);
	}

	public boolean performFinish() {
		try {
			List selectList = new ArrayList();
			TableItem[] tableItems = this.page1.tableItems;
			for (int i = 0; i < tableItems.length; i++) {
				TableItem item = tableItems[i];
				if (item.isChecked()) {
					selectList.add(item.getConfig());
				}
			}
			return save((IDBConfig[]) selectList.toArray(new IDBConfig[0]));

		} catch (IOException e) {
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

	private boolean save(IDBConfig[] configs) throws IOException {
		Shell shell = DbPlugin.getDefault().getShell();
		File file = saveXml(shell, null);

		if (file != null) {
			// log.debug("path:" + file.getAbsolutePath()); //$NON-NLS-1$
			if (file.exists()) {
				if (!confirmOverwrite(shell, file.getName())) {
					// log.debug(Messages.getString("ExpDBConfigWizard.1")); //$NON-NLS-1$
					return false;
				}
			}
			XMLManager.save(file, configs);
		}
		return true;
	}

	private File saveXml(Shell shell, String defaultFileName) {
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFileName(defaultFileName);
		dialog.setFilterExtensions(new String[] {"*.xml", "*.*" //$NON-NLS-1$ //$NON-NLS-2$
		});
		dialog.setFilterNames(new String[] {Messages.getString("ExpDBConfigWizard.4"), Messages.getString("ExpDBConfigWizard.5") //$NON-NLS-1$ //$NON-NLS-2$
				});
		String fileName = dialog.open();
		if (fileName != null) {
			return new File(fileName);
		} else {
			return null;
		}
	}

	private boolean confirmOverwrite(Shell shell, String fileName) {
		MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		msg.setMessage(Messages.getString("ExpDBConfigWizard.6") + fileName + Messages.getString("ExpDBConfigWizard.7")); //$NON-NLS-1$ //$NON-NLS-2$
		msg.setText(Messages.getString("ExpDBConfigWizard.8")); //$NON-NLS-1$
		int res = msg.open();
		if (res == SWT.YES) {
			return true;
		} else {
			return false;
		}
	}

}
