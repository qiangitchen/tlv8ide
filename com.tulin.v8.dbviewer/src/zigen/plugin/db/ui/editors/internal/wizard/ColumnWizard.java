/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class ColumnWizard extends DefaultWizard implements IConfirmDDLWizard {

	private ColumnWizardPage page1;

	private Column column;

	private Column newColumn;

	private boolean isAddColumn = false;

	public ColumnWizard(ISQLCreatorFactory factory, ITable tableNode, Column column, boolean isAddColumn) {
		super(factory, tableNode);
		if (isAddColumn) {
			super.setWindowTitle(Messages.getString("ColumnWizard.0")); //$NON-NLS-1$
		} else {
			super.setWindowTitle(Messages.getString("ColumnWizard.1")); //$NON-NLS-1$
		}
		this.column = column;
		this.isAddColumn = isAddColumn;

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);

	}

	public void addPages() {
		page1 = new ColumnWizardPage(factory, tableNode, column, isAddColumn);
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
		Column newColumn = new Column(new zigen.plugin.db.core.TableColumn());
		newColumn.setParent(column.getParent());
		newColumn.setName(page1.txtColumnName.getText());
		newColumn.setTypeName(page1.cmbColumnType.getText());

		if (factory.isVisibleColumnSize(page1.cmbColumnType.getText())) {
			newColumn.setSize(page1.txtColumnSize.getText());

			if ("".equals(page1.txtColumnSize.getText())) {
				newColumn.getColumn().setWithoutParam(true);
			}

		}
		newColumn.setDefaultValue(page1.txtDefualtt.getText());
		newColumn.setRemark(page1.txtColumnComment.getText());
		newColumn.setNotNull(page1.chkNotNull.getSelection());

		List list = new ArrayList();
		if (isAddColumn) {
			String[] sqls = factory.createAddColumnDDL(newColumn);
			for (int i = 0; i < sqls.length; i++) {
				list.add(sqls[i]);
			}
			list.add(factory.createCommentOnColumnDDL(newColumn));

		} else {
			if (!column.getName().equals(newColumn.getName())) {
				list.add(factory.createRenameColumnDDL(column, newColumn));

			}
			if (!column.getTypeName().equals(newColumn.getTypeName()) || !column.getSize().equals(newColumn.getSize())
					|| !column.getDefaultValue().equals(newColumn.getDefaultValue()) || column.isNotNull() != newColumn.isNotNull()) {

				String[] sqls = factory.createModifyColumnDDL(column, newColumn);
				for (int i = 0; i < sqls.length; i++) {
					list.add(sqls[i]);
				}
			}

			if (!column.getRemarks().equals(newColumn.getRemarks())) {
				list.add(factory.createCommentOnColumnDDL(newColumn));
			}

		}
		return (String[]) list.toArray(new String[0]);

	}


}
