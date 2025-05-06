/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSynonymInfoSearcher;
import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;

@SuppressWarnings({"rawtypes","unchecked"})
public class RefreshFolderJob extends AbstractJob {

	private TreeViewer viewer;

	private Folder folder;

	public RefreshFolderJob(TreeViewer viewer, Folder folder) {
		super(Messages.getString("RefreshFolderJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.folder = folder;
	}

	protected IStatus run(IProgressMonitor monitor) {
		TableInfo[] tables = null;
		try {

			TimeWatcher ts = new TimeWatcher();
			ts.start();
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();
			if (SchemaSearcher.isSupport(con)) {
				tables = TableSearcher.execute(con, folder.getSchema().getName(), new String[] {folder.getName()});
			} else {
				tables = TableSearcher.execute(con, null, new String[] {folder.getName()});
			}
			ts.stop();
			if (updateTables(monitor, con, folder.getSchema(), folder, tables)) {

			} else {
				return Status.CANCEL_STATUS;
			}

		} catch (Exception e) {
			showErrorMessage(Messages.getString("RefreshFolderJob.1"), e); //$NON-NLS-1$
		}
		return Status.OK_STATUS;
	}

	private boolean updateTables(IProgressMonitor monitor, Connection con, Schema schema, Folder folder, TableInfo[] tables) throws Exception {
		String label = folder.getName();
		List newTableList = new ArrayList();

		monitor.beginTask(Messages.getString("RefreshFolderJob.2"), tables.length); //$NON-NLS-1$

		TimeWatcher ts = new TimeWatcher();
		ts.start();
		for (int i = 0; i < tables.length; i++) {

			TimeWatcher ts1 = new TimeWatcher();
			ts1.start();

			TableInfo tableinfo = tables[i];
			monitor.subTask(tableinfo.getName() + Messages.getString("RefreshFolderJob.3")); //$NON-NLS-1$

			ITable node;
			if ("SYNONYM".equals(label)) { //$NON-NLS-1$
				monitor.setTaskName(Messages.getString("RefreshFolderJob.5")); //$NON-NLS-1$
				node = new Synonym(tableinfo.getName(), tableinfo.getComment());
				switch (DBType.getType(con.getMetaData())) {
				case DBType.DB_TYPE_ORACLE:
					if (schema != null) {
						String owner = schema.getName();
						String synonymName = tableinfo.getName();
						SynonymInfo info = OracleSynonymInfoSearcher.execute(con, owner, synonymName);
						((Synonym) node).setSynonymInfo(info);
					} else {
						throw new Exception(Messages.getString("RefreshFolderJob.6")); //$NON-NLS-1$
					}
					break;
				default:
					throw new UnsupportedOperationException(Messages.getString("RefreshFolderJob.7")); //$NON-NLS-1$
					// break;
				}

			} else if ("VIEW".equals(label)) { //$NON-NLS-1$
				monitor.setTaskName(Messages.getString("RefreshFolderJob.9")); //$NON-NLS-1$
				node = new View(tableinfo.getName(), tableinfo.getComment());
				View view = (View)node;
				view.setTvtype("view");
				view.setDbkey(folder.getDbkey());
			} else {
				monitor.setTaskName(Messages.getString("RefreshFolderJob.10")); //$NON-NLS-1$
				node = new Table(tableinfo.getName(), tableinfo.getComment());
				Table table = (Table)node;
				table.setTvtype("table");
				table.setDbkey(folder.getDbkey());
			}

			newTableList.add(node.getName());

			TreeLeaf leaf = folder.getChild(node.getName());
			if (leaf == null) {
				addTable(folder, node);
				showResults(new RefreshTreeNodeAction(viewer, node, RefreshTreeNodeAction.MODE_NOTHING));
			} else {
				updateTable(leaf, node);
				showResults(new RefreshTreeNodeAction(viewer, leaf, RefreshTreeNodeAction.MODE_NOTHING));
			}

			if (monitor.isCanceled()) {
				return false;
			}
			ts1.stop();

			monitor.worked(1);
		}
		ts.stop();

		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (!newTableList.contains(leaf.getName())) {
				folder.removeChild(leaf);
			}
		}

		ts.start();

		try {
			showResults(new RefreshTreeNodeAction(viewer, folder, RefreshTreeNodeAction.MODE_NOTHING));
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		ts.stop();

		return true;
	}

	private void addTable(Folder folder, ITable newNode) {
		folder.addChild((TreeNode) newNode);
		TableColumn tColumn = new TableColumn();
		tColumn.setColumnName(DbPluginConstant.TREE_LEAF_LOADING);
		newNode.addChild(new Column(tColumn));
	}

	private void updateTable(TreeLeaf oldNode, ITable newNode) {
		RefreshColumnJob job = null;
		if (oldNode instanceof Synonym) {
			Synonym synonym = (Synonym) oldNode;
			synonym.update((Synonym) newNode);
			synonym.setEnabled(true);
			if (synonym.isExpanded()) {
				job = new RefreshColumnJob(viewer, synonym);
			}
		} else if (oldNode instanceof View) {
			View view = (View) oldNode;
			view.update((View) newNode);
			view.setEnabled(true);
			if (view.isExpanded()) {
				job = new RefreshColumnJob(viewer, view);
			}
		} else if (oldNode instanceof Table) {
			Table table = (Table) oldNode;
			table.update((Table) newNode);
			table.setEnabled(true);
			if (table.isExpanded()) {
				job = new RefreshColumnJob(viewer, table);
			}
		}
		if (job != null) {
			TimeWatcher ts = new TimeWatcher();
			ts.start();

			job.setPriority(RefreshColumnJob.SHORT);
			job.schedule();
			try {
				job.join();
			} catch (InterruptedException e) {
				DbPlugin.log(e);
			}

			ts.stop();
		}

	}

}
