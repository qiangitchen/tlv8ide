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
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSourceErrorSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSourceTypeSearcher;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.View;
import zigen.plugin.db.ui.views.TableSearchThread;

public class TableTypeSearchJob extends AbstractJob {

	// public static final String VisibleFolderPattern = "^TABLE|^VIEW|^SYNONYM|^ALIAS"; //$NON-NLS-1$
	public static final String VisibleFolderPattern = "^TABLE|^SYNONYM|^ALIAS"; //$NON-NLS-1$

	private TreeViewer viewer;

	private Schema schema;

	public TableTypeSearchJob(TreeViewer viewer, Schema schema) {
		super(Messages.getString("TableTypeSearchJob.1")); //$NON-NLS-1$
		this.viewer = viewer;
		this.schema = schema;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {

			Connection con = Transaction.getInstance(schema.getDbConfig()).getConnection();

			schema.removeChildAll();
			showResults(new RefreshTreeNodeAction(viewer, schema));

			String[] tableTypes = schema.getDataBase().getTableType();

			monitor.beginTask(Messages.getString("TableTypeSearchJob.2"), tableTypes.length); //$NON-NLS-1$
			for (int i = 0; i < tableTypes.length; i++) {
				TimeWatcher ts = new TimeWatcher();
				ts.start();

				monitor.subTask(tableTypes[i] + Messages.getString("TableTypeSearchJob.3")); //$NON-NLS-1$

				if (tableTypes[i].toUpperCase().matches(VisibleFolderPattern)) {
					TableInfo[] tables = TableSearcher.execute(con, schema.getName(), new String[] {tableTypes[i]});
					TableSearchThread.addFolderAndTables(con, schema, tableTypes[i], tables);


				} else if ("VIEW".equals(tableTypes[i].toUpperCase())) {
					Folder folder = new Folder();
					folder.setName(tableTypes[i]);
					folder.setTvtype("dbtype");
					folder.setDbkey(schema.getParent().getName());
					View view = new View();
					view.setName(DbPluginConstant.TREE_LEAF_LOADING);
					view.setTvtype("view");
					view.setDbkey(schema.getParent().getName());
					folder.addChild(view);
					schema.addChild(folder);
				} else if ("SEQUENCE".equals(tableTypes[i])) { //$NON-NLS-1$
					switch (DBType.getType(schema.getDbConfig())) {
					case DBType.DB_TYPE_ORACLE:
						Folder folder = new Folder();
						folder.setName(tableTypes[i]);
						folder.setTvtype("dbtype");
						folder.setDbkey(schema.getParent().getName());
						OracleSequence seq = new OracleSequence();
						seq.setName(DbPluginConstant.TREE_LEAF_LOADING);
						folder.addChild(seq);
						schema.addChild(folder);
					}
				}
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				ts.stop();

				monitor.worked(1);
			}



			IDBConfig conifg = schema.getDbConfig();
			switch (DBType.getType(conifg)) {
			case DBType.DB_TYPE_ORACLE:
				String owner = schema.getName();

				TimeWatcher ts1 = new TimeWatcher();
				ts1.start();
				monitor.subTask("SELECT DISTINCT TYPE FROM ALL_SOURCE...");
				String[] sourceTypes = OracleSourceTypeSearcher.execute(con, owner);
				ts1.stop();


				schema.setSourceType(sourceTypes);
				for (int i = 0; i < sourceTypes.length; i++) {
					TimeWatcher ts = new TimeWatcher();
					ts.start();
					monitor.subTask(sourceTypes[i] + Messages.getString("TableTypeSearchJob.3")); //$NON-NLS-1$

					String stype = sourceTypes[i];
					Folder folder = new Folder(stype);
					folder.setTvtype("dbtype");
					folder.setDbkey(schema.getParent().getName());
					OracleSource source = new OracleSource();
					source.setName(DbPluginConstant.TREE_LEAF_LOADING);
					folder.addChild(source);

					int errorCount = OracleSourceErrorSearcher.execute(con, owner, stype).length;
					schema.addChild(folder);
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					ts.stop();
				}
			default:
			}
			schema.setExpanded(true);
			showResults(new RefreshTreeNodeAction(viewer, schema, RefreshTreeNodeAction.MODE_EXPAND));

		} catch (Exception e) {
			schema.setExpanded(false);

			showErrorMessage(Messages.getString("TableTypeSearchJob.5"), e); //$NON-NLS-1$
		}

		return Status.OK_STATUS;
	}

}
