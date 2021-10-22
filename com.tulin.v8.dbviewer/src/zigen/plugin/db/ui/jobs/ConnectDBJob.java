/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.TableTypeSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;

public class ConnectDBJob extends AbstractJob {

	public static final String VisibleFolderPattern = "^TABLE|^VIEW|^SYNONYM|^ALIAS"; //$NON-NLS-1$

	private TreeViewer viewer;

	private DataBase db;

	public ConnectDBJob(TreeViewer viewer, DataBase db) {
		super(Messages.getString("ConnectDBJob.1")); //$NON-NLS-1$
		this.viewer = viewer;
		this.db = db;

	}

	protected IStatus run(IProgressMonitor monitor) {

		try {
			synchronized (viewer) {
				// TimeWatcher tw = new TimeWatcher(); tw.start();
				monitor.beginTask("Connect DataBase...", 3);
				// monitor.worked(1);

				IDBConfig config = db.getDbConfig();

				int timeout = store.getInt(PreferencePage.P_CONNECT_TIMEOUT);
				TestConnectThread t = new TestConnectThread(config, timeout, true);
				Thread th = new Thread(t);
				th.setPriority(Thread.MIN_PRIORITY);
				th.start();

				if (timeout > 0) {
					th.join(timeout * 1000);
				} else {
					th.join();
				}
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}

				monitor.worked(1);

				if (!t.isSuccess) {
					db.setConnected(false);
					db.setExpanded(false);

					if (t.getThrowable() == null) {
						SQLException se = new SQLException("Connection timeout occurred.");
						t.setThrowable(se);
					}

					showErrorMessage(t.getMessage(), t.getThrowable());
					return Status.CANCEL_STATUS;
				}

				Connection con = Transaction.getInstance(config).getConnection();

				if (DBType.DB_TYPE_SYMFOWARE != config.getDbType()) {
					Display.getDefault().syncExec(new ChangeTransactionIsolationLevelAction(config));
				}

				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}

				db.removeChildAll();
				showResults(new RefreshTreeNodeAction(viewer, db));

				db.setSchemaSupport(SchemaSearcher.isSupport(con));
				db.setDefaultSchema(config.getSchema());

				String[] tableTypes = TableTypeSearcher.execute(con);
				monitor.worked(1);

				db.setTableType(tableTypes);

				if (db.isSchemaSupport()) {
//					String[] schemas = SchemaSearcher.execute(con);
//					addSchemas(db, schemas);

					DatabaseMetaData objMet = con.getMetaData();
					String share = db.getDefaultSchema();
					if (DBType.getType(objMet) == DBType.DB_TYPE_ORACLE
							|| DBType.getType(objMet) == DBType.DB_TYPE_DM) {
						share = share.toUpperCase();
					}
					// 只取默认连接
					String[] schemas = new String[] { share };
					if (DBType.DB_TYPE_POSTGRESQL == db.getDbConfig().getDbType()) {
						// 取所有支持的连接
//						schemas = SchemaSearcher.execute(con);
						String username = db.getUsername();
						if (username == null) {
							username = db.getDbConfig().getUserId();
							db.setUsername(username);
						}
						if (!"postgres".equalsIgnoreCase(username)) {
							schemas = new String[] { share, username };
						}
					}
					addSchemas(db, schemas);
				} else {
					for (int i = 0; i < tableTypes.length; i++) {
						String[] type = new String[] { tableTypes[i] };

						if (tableTypes[i].toUpperCase().matches(VisibleFolderPattern)) {
							addTables(db, tableTypes[i], TableSearcher.execute(con, null, type));
						}

						if (monitor.isCanceled()) {
							return Status.CANCEL_STATUS;
						}
					}
				}

				db.setConnected(true);

				showResults(new RefreshTreeNodeAction(viewer, db, RefreshTreeNodeAction.MODE_EXPAND));

				monitor.worked(1);
			}

		} catch (Exception e) {
			db.setConnected(false);
			db.setExpanded(false);
			showErrorMessage(Messages.getString("ConnectDBJob.2"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;

	}

	private void addSchemas(DataBase db, String[] schemas) throws Exception {
		boolean onlyDefaultSchema = false;
		for (int i = 0; i < schemas.length; i++) {
			Schema schema = new Schema(schemas[i]);
			if (!onlyDefaultSchema) {
				db.addChild(schema);

			} else {
				if (db.getDefaultSchema().toUpperCase().equals(schemas[i].toUpperCase())) {
					db.addChild(schema);
				}
			}
			if (db.getDefaultSchema().toUpperCase().equals(schemas[i].toUpperCase())) {
				if (viewer instanceof TreeViewer) {
					TableTypeSearchJob job = new TableTypeSearchJob(viewer, schema);
					job.setPriority(TableTypeSearchJob.SHORT);
					job.schedule();
					try {
						job.join();
					} catch (InterruptedException e) {
						DbPlugin.log(e);
					}
					schema.setExpanded(true);
					showResults(new RefreshTreeNodeAction(viewer, schema, RefreshTreeNodeAction.MODE_EXPAND));
				}
			} else {
				schema.addChild(new Folder(DbPluginConstant.TREE_LEAF_LOADING));
			}
		}
	}

	private void addTables(TreeNode parent, String label, TableInfo[] tables) {
		Folder folder = new Folder(label);
		folder.setTvtype("dbtype");
		folder.setDbkey(parent.getName());
		for (int i = 0; i < tables.length; i++) {
			TableInfo tableinfo = tables[i];
			Table table;
			if ("VIEW".equals(label)) { //$NON-NLS-1$
				table = new View(tableinfo.getName(), tableinfo.getComment());
				table.setTvtype("table");
				table.setDbkey(parent.getName());
			} else {
				table = new Table(tableinfo.getName(), tableinfo.getComment());
				table.setTvtype("view");
				table.setDbkey(parent.getName());
			}
			folder.addChild(table);
			TableColumn tColumn = new TableColumn();
			tColumn.setColumnName(DbPluginConstant.TREE_LEAF_LOADING);
			table.addChild(new Column(tColumn));
		}
		parent.addChild(folder);
	}

}
