/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.popup;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.jobs.SqlExecJob;
import zigen.plugin.db.ui.util.FileUtil;
import zigen.plugin.db.ui.util.ResourceUtil;

public class ExecSQLFileAction extends Action implements IObjectActionDelegate {

	protected IFile file;

	public ExecSQLFileAction() {
		super();
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	// action.setAccelerator(SWT.CTRL + SWT.CR);
	}

	public void run(IAction action) {
		try {
			if (file != null) {
				IDBConfig config = ResourceUtil.getDBConfig(file);

				if (config != null) {

					String sql = FileUtil.getContents(file);
					if (sql != null && sql.trim().length() > 0) {

						Transaction trans = Transaction.getInstance(config);

						SqlExecJob job = new SqlExecJob(trans, sql, null);
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

	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(false);

		if (selection instanceof StructuredSelection) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object obj = ss.getFirstElement();
			if (obj instanceof IFile) {
				IFile wkfile = (IFile) obj;
				file = wkfile;
				action.setEnabled(true);

			}
		}

	}

}
