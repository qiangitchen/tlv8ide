/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.action;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.actions.TableViewEditorAction;
import zigen.plugin.db.ui.editors.internal.wizard.ColumnWizard;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.TreeNode;

public class AddColumnAction extends TableViewEditorAction {

	private TableViewer tableViewer;

	public AddColumnAction() {
		this.setText(Messages.getString("AddColumnAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("AddColumnAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_COLUMN_ADD));
	}

	public void run() {

		IDBConfig config = editor.getDBConfig();
		ITable tableNode = editor.getTableNode();
		ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactory(config, tableNode);

		zigen.plugin.db.core.TableColumn tCol = new zigen.plugin.db.core.TableColumn();
		Column col = new Column();
		col.setColumn(tCol);
		col.setParent((TreeNode) tableNode);

		Shell shell = DbPlugin.getDefault().getShell();

		// int column = tableNode.getChildrens().length;
		ColumnWizard wizard = new ColumnWizard(factory, tableNode, col, true);
		WizardDialog dialog2 = new WizardDialog(shell, wizard);
		int ret = dialog2.open();
		if (ret == IDialogConstants.OK_ID) {
		}

	}

	public void setTableViewer(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

}
