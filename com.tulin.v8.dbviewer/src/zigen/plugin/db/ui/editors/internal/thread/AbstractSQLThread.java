/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.thread;

import java.sql.Connection;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;

abstract public class AbstractSQLThread implements Runnable {

	protected ITable table;

	protected IDBConfig config;

	protected ISQLCreatorFactory factory;

	public AbstractSQLThread(ITable table) {
		this.table = table;
		this.config = table.getDbConfig();
		this.factory = AbstractSQLCreatorFactory.getFactory(config, table);
	}

	public void run() {
		executeUpdate(createSQL(factory, table));
	}

	abstract String[] createSQL(ISQLCreatorFactory factory, ITable talbe);

	public void executeUpdate(String sqls[]) {
		String _sql = ""; //$NON-NLS-1$
		try {
			if (sqls == null || sqls.length == 0) {
				DbPlugin.getDefault().showWarningMessage(Messages.getString("AbstractSQLThread.2")); //$NON-NLS-1$
				return;
			}

			Connection con = Transaction.getInstance(table.getDbConfig()).getConnection();
			for (int i = 0; i < sqls.length; i++) {
				_sql = sqls[i];
				SQLInvoker.execute(con, _sql);
			}

			doAfterExecuteUpdate(table);

			Transaction trans = Transaction.getInstance(config);
			if (!config.isAutoCommit() && factory.supportsRollbackDDL()) {
				if (DbPlugin.getDefault().confirmDialog(Messages.getString("AbstractSQLThread.3"))) { //$NON-NLS-1$
					trans.commit();
				}
			}
			DbPlugin.fireStatusChangeListener(table, IStatusChangeListener.EVT_ModifyTableDefine);

			return;

		} catch (Exception e) {
			DbPlugin.log(Messages.getString("AbstractSQLThread.4") + _sql); //$NON-NLS-1$
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	protected void doAfterExecuteUpdate(ITable table) {
		;
	}

}
