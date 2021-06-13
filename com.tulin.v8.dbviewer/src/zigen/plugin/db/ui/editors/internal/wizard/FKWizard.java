/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Table;

public class FKWizard extends DefaultWizard implements IConfirmDDLWizard {

	private FKWizardPage1 page1;

	private FKWizardPage2 page2;

	public FKWizard(ISQLCreatorFactory factory, ITable tableNode) {
		super(factory, tableNode);
		super.setWindowTitle(Messages.getString("FKWizard.0")); //$NON-NLS-1$
		this.factory = factory;
		this.tableNode = tableNode;

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);

	}

	public void addPages() {

		page1 = new FKWizardPage1(factory, tableNode);
		page2 = new FKWizardPage2(factory, tableNode);
		addPage(page1);
		addPage(page2);
		addPage(confirmPage);

	}

	public boolean canFinish() {
		if (page1.isPageComplete() && page2.isPageComplete() && confirmPage.isPageComplete()) {
			return true;
		} else {
			return false;
		}
	}

	public String[] createSQL() throws Exception {
		String constraintName = page1.txtConstraintName.getText();
		Column[] columns = (Column[]) page1.selectedList.toArray(new Column[0]);
		TableInfo tableinfo = page2.getRerenceTableInfo();

		DataBase database = (DataBase) tableNode.getDataBase().clone();
		Schema schema = tableNode.getSchema();
		Folder folder = tableNode.getFolder();
		Table refTable = new Table(tableinfo.getName(), tableinfo.getComment());
		if (tableNode.isSchemaSupport()) {
			database.addChild(schema);
			schema.addChild(folder);
			folder.addChild(refTable);

		} else {
			database.addChild(folder);
			folder.addChild(refTable);
		}

		Column[] refColumns = (Column[]) page2.selectedList.toArray(new Column[0]);
		boolean onCascadeDelete = page2.chkDeleteCascade.getSelection();
		String sql = factory.createCreateConstraintFKDDL(constraintName, columns, refTable, refColumns, onCascadeDelete);
		return new String[] {sql};
	}

}
