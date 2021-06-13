/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class PKWizard extends DefaultWizard {


	private PKWizardPage page1;

	public PKWizard(ISQLCreatorFactory factory, ITable tableNode) {
		super(factory, tableNode);
		super.setWindowTitle(Messages.getString("PKWizard.0")); //$NON-NLS-1$
	}

	public void addPages() {
		page1 = new PKWizardPage(factory, tableNode);
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
		Column[] columns = (Column[]) page1.selectedList.toArray(new Column[0]);
		String sql = factory.createCreateConstraintPKDDL(constraintName, columns);
		return new String[] {sql};
	}

}
