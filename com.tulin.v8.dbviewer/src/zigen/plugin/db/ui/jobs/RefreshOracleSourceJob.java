/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSynonymInfoSearcher;
import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.View;

public class RefreshOracleSourceJob extends AbstractJob {

	private TreeViewer viewer;

	private OracleSource source;

	public RefreshOracleSourceJob(TreeViewer viewer, OracleSource source) {
		super("RefreshOracleSource...");
		this.viewer = viewer;
		this.source = source;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Refresh Oracle Source...", 10);
			Connection con = Transaction.getInstance(source.getDbConfig()).getConnection();

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}


			Schema schema = source.getSchema();
			String owner = schema.getName();
			String type = source.getType();

			OracleSourceErrorInfo[] errors  = OracleSourceErrorSearcher.execute(con, owner, source.getName(),type);
			source.setHasError(errors.length > 0);

			showResults(new RefreshTreeNodeAction(viewer, source.getParent(), RefreshTreeNodeAction.MODE_NOTHING));

			showResults(new RefreshTreeNodeAction(viewer, source, RefreshTreeNodeAction.MODE_NOTHING));

			monitor.done();
		} catch (Exception e) {
			showErrorMessage(Messages.getString("OracleSourceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

}
