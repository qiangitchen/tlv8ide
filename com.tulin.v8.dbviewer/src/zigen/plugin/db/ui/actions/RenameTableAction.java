/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

public class RenameTableAction extends Action implements Runnable {

	private StructuredViewer viewer = null;

	private IDBConfig config;

	private ISQLCreatorFactory factory;

	public RenameTableAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("RenameTableAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RenameTableAction.1")); //$NON-NLS-1$

	}

	public void run() {

		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		try {
			if (element instanceof ITable) {
				ITable table = (ITable) element;

				Shell shell = DbPlugin.getDefault().getShell();
				InputDialog dialog = new InputDialog(shell, Messages.getString("RenameTableAction.2"), Messages.getString("RenameTableAction.3"), table.getName(), null); //$NON-NLS-1$ //$NON-NLS-2$
				int rc = dialog.open();
				if (rc == InputDialog.CANCEL) {
					return;
				}

				String newTableName = dialog.getValue().trim();
				if (!table.getName().equals(newTableName)) {
					String newRemarks = table.getRemarks();

					config = table.getDbConfig();
					factory = AbstractSQLCreatorFactory.getFactory(config, table);

					Transaction trans = Transaction.getInstance(config);

					String[] sqls = createSQL(table, newTableName, newRemarks);
					for (int i = 0; i < sqls.length; i++) {
						SQLInvoker.execute(trans.getConnection(), sqls[i]);
					}
					if (!config.isAutoCommit() && factory.supportsRollbackDDL()) {
						if (DbPlugin.getDefault().confirmDialog(Messages.getString("RenameTableAction.4"))) { //$NON-NLS-1$
							trans.commit();
						}
					}
					DbPlugin.fireStatusChangeListener(table, IStatusChangeListener.EVT_ModifyTableDefine);

					table.setName(newTableName);
					viewer.refresh(table);
				}
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	public String[] createSQL(ITable table, String newTableName, String newRemarks) {
		List list = new ArrayList();


		if (!table.getRemarks().equals(newRemarks)) {
			list.add(factory.createCommentOnTableDDL(newRemarks));
		}
		if (!table.getName().equals(newTableName)) {
			list.add(factory.createRenameTableDDL(newTableName));
		}
		return (String[]) list.toArray(new String[0]);

	}

}
