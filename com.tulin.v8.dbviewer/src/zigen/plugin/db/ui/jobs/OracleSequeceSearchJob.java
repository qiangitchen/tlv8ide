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

import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSequenceInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSequenceSearcher;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.Schema;

public class OracleSequeceSearchJob extends AbstractJob {

	private TreeViewer viewer;

	private Folder folder;

	public OracleSequeceSearchJob(TreeViewer viewer, Folder folder) {
		super(Messages.getString("OracleSequeceSearchJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.folder = folder;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Search Oracle Seuence...", 10);
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			folder.removeChildAll();
			showResults(new RefreshTreeNodeAction(viewer, folder));

			Schema schema = (Schema) folder.getParent();
			String owner = schema.getName();
			OracleSequenceInfo[] infos = OracleSequenceSearcher.execute(con, owner);
			addSequences(con, folder, infos);

			folder.setExpanded(true);
			showResults(new RefreshTreeNodeAction(viewer, folder, RefreshTreeNodeAction.MODE_NOTHING));

			monitor.done();

		} catch (Exception e) {
			folder.setExpanded(false);
			showErrorMessage(Messages.getString("OracleSequeceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

	private void addSequences(Connection con, Folder folder, OracleSequenceInfo[] infos) throws Exception {
		for (int i = 0; i < infos.length; i++) {
			OracleSequence seq = new OracleSequence();
			seq.setOracleSequenceInfo(infos[i]);
			folder.addChild(seq);
		}
	}
}
