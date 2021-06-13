/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceTypeSearcher;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;

public class SourceTypeSearchJob extends AbstractJob {

	private TreeViewer viewer;

	private Schema schema;

	public SourceTypeSearchJob(TreeViewer viewer, Schema schema) {
		super(Messages.getString("SourceTypeSearchJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.schema = schema;
	}

	protected IStatus run(IProgressMonitor monitor) {

		try {
			Connection con = Transaction.getInstance(schema.getDbConfig()).getConnection();

			schema.removeChildAll();
			showResults(new RefreshTreeNodeAction(viewer, schema));

			String owner = schema.getName();
			String[] sourceTypes = OracleSourceTypeSearcher.execute(con, owner);
			schema.setSourceType(sourceTypes);

			for (int i = 0; i < sourceTypes.length; i++) {
				String stype = sourceTypes[i];
				Folder folder = new Folder(stype);

				OracleSource source = new OracleSource();
				source.setName(DbPluginConstant.TREE_LEAF_LOADING);
				folder.addChild(source);
				schema.addChild(folder);

				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
			}

			showResults(new RefreshTreeNodeAction(viewer, schema, RefreshTreeNodeAction.MODE_EXPAND));

		} catch (Exception e) {
			showErrorMessage(Messages.getString("SourceTypeSearchJob.1"), e); //$NON-NLS-1$

		} finally {
		}
		return Status.OK_STATUS;

	}

}
