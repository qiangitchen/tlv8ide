/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class IndexWizard extends DefaultWizard implements IConfirmDDLWizard {


	private IndexWizardPage page1;

	public IndexWizard(ISQLCreatorFactory factory, ITable tableNode) {
		super(factory, tableNode);
		super.setWindowTitle(Messages.getString("IndexWizard.0")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public void addPages() {
		page1 = new IndexWizardPage(factory, tableNode);
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
		String indexName = page1.txtIndexName.getText();
		Column[] columns = (Column[]) page1.selectedList.toArray(new Column[0]);
		int indexType = page1.getIndexType();
		String sql = factory.createCreateIndexDDL(indexName, columns, indexType);
		return new String[] {sql};
	}
}
