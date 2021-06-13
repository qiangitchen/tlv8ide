/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IObjectActionDelegate;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.jobs.ScriptExecJob;
import zigen.plugin.db.ui.util.FileUtil;
import zigen.plugin.db.ui.util.ResourceUtil;

public class ExecScriptFileAction extends ExecSQLFileAction implements IObjectActionDelegate {


	public ExecScriptFileAction() {
		super();
	}

	public void run(IAction action) {
		try {
			if (file != null) {
				IDBConfig config = ResourceUtil.getDBConfig(file);

				if (config != null) {

					String sql = FileUtil.getContents(file);
					if (sql != null && sql.trim().length() > 0) {

						Transaction trans = Transaction.getInstance(config);

						ScriptExecJob job = new ScriptExecJob(trans, sql, null);
						// job.setPriority(Job.SHORT);
						job.setUser(false);
						job.schedule();

					} else {
						DbPlugin.getDefault().showInformationMessage(Messages.getString("ExecSQLFileAction.0")); //$NON-NLS-1$
					}
				} else {
					DbPlugin.getDefault().showInformationMessage(Messages.getString("ExecSQLFileAction.1")); //$NON-NLS-1$
				}
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}
}
