/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.progress.IProgressConstants;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;
import zigen.plugin.db.core.rule.DefaultConstraintSearcherFactory;
import zigen.plugin.db.core.rule.IColumnSearcherFactory;
import zigen.plugin.db.core.rule.IConstraintSearcherFactory;
import zigen.plugin.db.ext.oracle.internal.OracleIndexSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSynonymInfoSearcher;
import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.editors.exceptions.NotFoundSynonymInfoException;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleColumn;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.jobs.AbstractJob;
import zigen.plugin.db.ui.views.TableSearchThread;

public class DDLDiffForFolderJob extends AbstractJob {

	public static final String TargetFolderPattern = "^TABLE|^VIEW|^SYNONYM|^ALIAS"; //$NON-NLS-1$

	public static final String TargetFolderPattern2 = "^FUNCTION|^PROCEDURE|^PACKAGE"; //$NON-NLS-1$

	private TreeViewer viewer;

	private List allList = new ArrayList();

	private List ddlList = new ArrayList();

	private Map map1 = new HashMap();

	private Map map2 = new HashMap();

	private Folder f1;

	private Folder f2;

	public DDLDiffForFolderJob(TreeViewer viewer, Folder f1, Folder f2) {
		super(Messages.getString("DDLDiffJob.0")); //$NON-NLS-1$
		this.f1 = f1;
		this.f2 = f2;
		this.viewer = viewer;
	}

	protected IStatus run(IProgressMonitor monitor) {
		IStatus result = Status.OK_STATUS;

		try {
			setProperty(IProgressConstants.ICON_PROPERTY, null);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			Folder folder1 = (Folder) f1.clone();
			folder1.setParent(f1.getParent());

			Folder folder2 = (Folder) f2.clone();
			folder2.setParent(f2.getParent());

			if (loadAll(monitor, folder1, map1) == Status.OK_STATUS) {
				result = loadAll(monitor, folder2, map2);
			} else {
				return Status.CANCEL_STATUS;
			}

			Collections.sort(allList);

			for (Iterator iter = allList.iterator(); iter.hasNext();) {

				if (monitor.isCanceled()) {
					break;
				}

				String keyword = (String) iter.next();

				Object obj1 = map1.get(keyword);
				Object obj2 = map2.get(keyword);

				if (obj1 != null && obj2 != null) {
					if (obj1 instanceof DDL && obj2 instanceof DDL) {
						DDL ddl1 = (DDL) obj1;
						DDL ddl2 = (DDL) obj2;
						IDDLDiff diff = new DDLDiff(ddl1, ddl2);
						ddlList.add(diff);

					} else if (obj1 instanceof SourceDDL && obj2 instanceof SourceDDL) {
						SourceDDL ddl1 = (SourceDDL) obj1;
						SourceDDL ddl2 = (SourceDDL) obj2;
						IDDLDiff diff = new SourceDDLDiff(ddl1, ddl2);
						ddlList.add(diff);
					}

				} else if (obj1 == null) {
					if (obj2 instanceof DDL) {
						DDL ddl2 = (DDL) obj2;
						IDDLDiff diff = new DDLDiff(null, ddl2);
						ddlList.add(diff);

					} else if (obj2 instanceof SourceDDL) {
						SourceDDL ddl2 = (SourceDDL) obj2;
						IDDLDiff diff = new SourceDDLDiff(null, ddl2);
						ddlList.add(diff);
					}

				} else if (obj2 == null) {
					if (obj1 instanceof DDL) {
						DDL ddl1 = (DDL) obj1;
						IDDLDiff diff = new DDLDiff(ddl1, null);
						ddlList.add(diff);

					} else if (obj1 instanceof SourceDDL) {
						SourceDDL ddl1 = (SourceDDL) obj1;
						IDDLDiff diff = new SourceDDLDiff(ddl1, null);
						ddlList.add(diff);
					}
				}

			}
			monitor.done();

			showResults(new ShowDiffView());

		} catch (Exception e) {
			DbPlugin.log(e);

		}

		return result;
	}

	private IStatus loadAll(IProgressMonitor monitor, Folder targetFolder, Map map) {

		Connection con = null;
		try {

			IDBConfig config = targetFolder.getDbConfig();
			con = ConnectionManager.getConnection(config);

			// schema.removeChildAll();
			targetFolder.setExpanded(true);

			Schema schema = targetFolder.getSchema();

			// String[] tableTypes = schema.getDataBase().getTableType();
			String[] tableTypes = new String[] {targetFolder.getName()};

			for (int i = 0; i < tableTypes.length; i++) {
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}

				int totalWork = 0;
				String currentType = tableTypes[i];

				String[] types = new String[] {currentType};

				if (currentType.toUpperCase().matches(TargetFolderPattern)) {
					TableInfo[] tables = new TableInfo[0];
					if (targetFolder.getDataBase().isSchemaSupport()) {

						tables = TableSearcher.execute(con, schema.getName(), types);
						TableSearchThread.AddTables(con, schema, targetFolder, tables);

					} else {
						tables = TableSearcher.execute(con, null, types);
						TableSearchThread.AddTables(con, null, targetFolder, tables);

					}
					totalWork += tables.length;

				} else if (currentType.toUpperCase().matches(TargetFolderPattern2)) {
					if (DBType.getType(config) == DBType.DB_TYPE_ORACLE) {
						try {
							OracleSourceInfo[] infos = OracleSourceSearcher.execute(con, schema.getName(), currentType);
							totalWork += infos.length;

//							Folder folder = (Folder) schema.getChild(currentType);
//							if (folder == null) {
//								folder = new Folder(currentType);
//								schema.addChild(folder);
//							}

							for (int j = 0; j < infos.length; j++) {
								OracleSource source = new OracleSource();
								source.setOracleSourceInfo(infos[j]);
//								folder.addChild(source);
								targetFolder.addChild(source);
							}
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
					}

				} else {
					continue;
				}

				monitor.beginTask(config.getDbName() + "/" + schema.getName() + Messages.getString("DDLDiffJob.1") + tableTypes[i] + Messages.getString("DDLDiffJob.2"), totalWork); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				targetFolder.setExpanded(true);
				List list = targetFolder.getChildren();
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					try {

						Object obj = iter.next();
						if (obj instanceof ITable) {
							ITable table = (ITable) obj;
							monitor.subTask("Target:" + table.getName());
							if (!table.isExpanded()) {
								table.removeChild(table.getChild(DbPluginConstant.TREE_LEAF_LOADING));
								table.setExpanded(true);
								try {
									if (DBType.getType(config) == DBType.DB_TYPE_ORACLE) {
										if (table.getName().indexOf("BIN$") >= 0) {
											continue;
										} else {
											loadColumn(monitor, con, table, config.isConvertUnicode());
										}
									} else {
										loadColumn(monitor, con, table, config.isConvertUnicode());
									}

								} catch (NotFoundSynonymInfoException e) {
									DbPlugin.log(e);
								}
							}
							IDDL ddl = new DDL(table);
							String key = currentType + "@" + table.getName();
							if (!allList.contains(key)) {
								allList.add(key);
							}
							map.put(key, ddl);

						} else if (obj instanceof OracleSource) {
							OracleSource os = (OracleSource) obj;
							monitor.subTask("Target:" + os.getName());
							IDDL ddl = new SourceDDL(os);
							String key = currentType + "@" + os.getName();
							if (!allList.contains(key)) {
								allList.add(key);
							}
							map.put(key, ddl);

						}
					} catch (RuntimeException e1) {
						e1.printStackTrace();
					}
					monitor.worked(1);

				}

			}

			// schema.removeChildAll();
			// schema = null;

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} finally {
			ConnectionManager.closeConnection(con);
		}

		return Status.OK_STATUS;

	}

	private void loadColumn(IProgressMonitor monitor, Connection con, ITable table, boolean convertUnicode) throws Exception {

		TablePKColumn[] pks = null;
		TableFKColumn[] fks = null;
		TableConstraintColumn[] cons = null;
		TableIDXColumn[] uidxs = null;
		TableIDXColumn[] nonuidxs = null;

		String schemaName = table.getSchemaName();
		String tableName = table.getName();

		switch (DBType.getType(con.getMetaData())) {
		case DBType.DB_TYPE_ORACLE:

			if (table.getName().toUpperCase().indexOf("BIN$") >= 0) {
				return;
			}

			if (table instanceof Synonym) {
				Synonym synonym = (Synonym) table;
				schemaName = synonym.getTable_owner();
				tableName = synonym.getTable_name();
			} else if (table instanceof Bookmark) {
				Bookmark bm = (Bookmark) table;
				if (bm.isSynonym()) {
					// SynonymInfo info = OracleSynonymInfoSearcher.execute(con,
					// bm.getName());
					SynonymInfo info = OracleSynonymInfoSearcher.execute(con, bm.getSchemaName(), bm.getName());
					schemaName = info.getTable_owner();
					tableName = info.getTable_name();

				}
			}
			break;
		}


		IDBConfig config = table.getDbConfig();
		IColumnSearcherFactory factory = DefaultColumnSearcherFactory.getFactory(con.getMetaData(), config.isConvertUnicode());
		TableColumn[] columns = factory.execute(con, schemaName, tableName);

		// TableColumn[] columns = ColumnSearcher.execute(con, schemaName, tableName, convertUnicode);
		// pks = ConstraintSearcher.getPKColumns(con, schemaName, tableName);
		// fks = ConstraintSearcher.getFKColumns(con, schemaName, tableName);

		IConstraintSearcherFactory constraintFactory = DefaultConstraintSearcherFactory.getFactory(config);
		pks = constraintFactory.getPKColumns(con, schemaName, tableName);
		fks = constraintFactory.getFKColumns(con, schemaName, tableName);


		if (!table.getFolderName().equals("VIEW")) { //$NON-NLS-1$

			switch (DBType.getType(con.getMetaData())) {
			case DBType.DB_TYPE_ORACLE:
				// cons = OracleConstraintSearcher.getConstraintColumns(con, schemaName, tableName);
				cons = constraintFactory.getConstraintColumns(con, schemaName, tableName);
				uidxs = OracleIndexSearcher.getIDXColumns(con, schemaName, tableName, true);
				nonuidxs = OracleIndexSearcher.getIDXColumns(con, schemaName, tableName, false);
				break;
			default:
				// uidxs = ConstraintSearcher.getUniqueIDXColumns(con, schemaName, tableName, true);
				// nonuidxs = ConstraintSearcher.getUniqueIDXColumns(con, schemaName, tableName, false);
				uidxs = constraintFactory.getUniqueIDXColumns(con, schemaName, tableName, true);
				nonuidxs = constraintFactory.getUniqueIDXColumns(con, schemaName, tableName, false);
				break;
			}

		}

		table.setTablePKColumns(pks);
		table.setTableFKColumns(fks);
		table.setTableConstraintColumns(cons);
		table.setTableUIDXColumns(uidxs);
		table.setTableNonUIDXColumns(nonuidxs);

		for (int i = 0; i < columns.length; i++) {
			TableColumn w_column = columns[i];
			TablePKColumn w_pk = getPKColumn(pks, w_column);
			TableFKColumn[] w_fks = getFKColumns(fks, w_column);

			addChild(con, table, w_column, w_pk, w_fks);

		}
	}

	private void addChild(Connection con, ITable table, TableColumn w_column, TablePKColumn w_pk, TableFKColumn[] w_fks) throws Exception {

		switch (DBType.getType(con.getMetaData())) {
		case DBType.DB_TYPE_ORACLE:
			table.addChild(new OracleColumn(w_column, w_pk, w_fks));
			break;
		default:
			table.addChild(new Column(w_column, w_pk, w_fks));
			break;
		}
	}

	private TablePKColumn getPKColumn(TablePKColumn[] pks, TableColumn column) throws Exception {
		TablePKColumn pk = null;
		for (int i = 0; i < pks.length; i++) {
			if (pks[i].getColumnName().equals(column.getColumnName())) {
				pk = pks[i];
				break;
			}
		}
		return pk;

	}

	private TableFKColumn[] getFKColumns(TableFKColumn[] fks, TableColumn column) throws Exception {

		List list = new ArrayList();
		for (int i = 0; i < fks.length; i++) {
			if (fks[i].getColumnName().equals(column.getColumnName())) {
				list.add(fks[i]);
			}
		}
		return (TableFKColumn[]) list.toArray(new TableFKColumn[0]);

	}

	public class ShowDiffView implements Runnable {

		public ShowDiffView() {}

		public void run() {
			try {

				IDDLDiff[] diffs = (IDDLDiff[]) ddlList.toArray(new IDDLDiff[0]);

				DDLDiffEditorInput input = new DDLDiffEditorInput(diffs, false);
				IWorkbenchPage page = DbPlugin.getDefault().getPage();
				IEditorPart editor = IDE.openEditor(page, input, DDLDiffEditor.ID, true);

				// if (editor instanceof DDLDiffEditor) {
				// DDLDiffEditor dEditor = (DDLDiffEditor) editor;
				// }

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}
}
