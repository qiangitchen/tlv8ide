/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class CheckWizard extends DefaultWizard implements IConfirmDDLWizard {


	private CheckWizardPage page1;

	public CheckWizard(ISQLCreatorFactory factory, ITable tableNode) {
		super(factory, tableNode);
		super.setWindowTitle(Messages.getString("CheckWizard.0")); //$NON-NLS-1$

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);

	}

	public void addPages() {
		page1 = new CheckWizardPage(factory, tableNode);
		addPage(page1);
		addPage(confirmPage);

	}

	public boolean canFinish() {
		if (page1.isPageComplete() && confirmPage.isPageComplete()) {
			return true;
		} else {
			return false;
		}
	}

	public String[] createSQL() throws Exception {
		String constraintName = page1.txtConstraintName.getText();
		String check = page1.sqlViewer.getDocument().get();
		String sql = factory.createCreateConstraintCheckDDL(constraintName, check);
		return new String[] {sql};

	}

}
