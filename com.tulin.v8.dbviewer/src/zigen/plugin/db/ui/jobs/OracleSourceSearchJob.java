/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceSearcher;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleFunction;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeLeaf;

public class OracleSourceSearchJob extends AbstractJob {

	private TreeViewer viewer;

	private Folder folder;

	public OracleSourceSearchJob(TreeViewer viewer, Folder folder) {
		super(Messages.getString("OracleSourceSearchJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.folder = folder;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Search Oracle Source...", 10);
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			Schema schema = (Schema) folder.getParent();
			String owner = schema.getName();
			String type = folder.getName();

			OracleSourceInfo[] infos = OracleSourceSearcher.execute(con, owner, type);
			OracleSourceErrorInfo[] errors  = OracleSourceErrorSearcher.execute(con, owner, type);

			Map errorMap = new HashMap();
			for (int i = 0; i < errors.length; i++) {
				OracleSourceErrorInfo err = errors[i];
				if(!errorMap.containsKey(err.getName())){
					errorMap.put(err.getName(), err);
				}
			}

			addSources(con, folder, infos, errorMap);


			folder.setExpanded(true);
			showResults(new RefreshTreeNodeAction(viewer, folder, RefreshTreeNodeAction.MODE_NOTHING));

			monitor.done();
		} catch (Exception e) {
			folder.setExpanded(false);
			showErrorMessage(Messages.getString("OracleSourceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

	private void addSources(Connection con, Folder folder, OracleSourceInfo[] infos, Map errorMap) throws Exception {

		List newList = new ArrayList();

		for (int i = 0; i < infos.length; i++) {
			newList.add(infos[i].getName());

			OracleSource source;
			if ("FUNCTION".equals(folder.getName())) { //$NON-NLS-1$
				source = new OracleFunction();
			} else {
				source = new OracleSource();
			}
			source.setOracleSourceInfo(infos[i]);
			source.setHasError(errorMap.containsKey(source.getName()));

			TreeLeaf leaf = folder.getChild(source.getName());
			if (leaf == null) {
				folder.addChild(source);

			} else {

				OracleSource os = (OracleSource) leaf;
				os.update(source);
				RefreshOracleSourceJob job = new RefreshOracleSourceJob(viewer, os);
				job.setPriority(RefreshOracleSourceJob.SHORT);
				job.setUser(true);
				job.schedule();

			}

		}

		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (!newList.contains(leaf.getName())) {
				folder.removeChild(leaf);
			}
		}

	}
}
