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

import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.TablePKColumn;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.rule.DefaultColumnSearcherFactory;
import zigen.plugin.db.core.rule.DefaultConstraintSearcherFactory;
import zigen.plugin.db.core.rule.IColumnSearcherFactory;
import zigen.plugin.db.core.rule.IConstraintSearcherFactory;
import zigen.plugin.db.ext.oracle.internal.OracleIndexSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleSynonymInfoSearcher;
import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleColumn;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.TreeLeaf;

abstract public class AbstractLoadColumnJob extends AbstractJob {

	public AbstractLoadColumnJob(String msg) {
		super(msg);
	}

	protected boolean loadColumnInfo(IProgressMonitor monitor, Connection con, ITable table) throws Exception {
		TimeWatcher ts = new TimeWatcher();
		ts.start();

		TableColumn[] columns = null;
		TablePKColumn[] pks = null;
		TableFKColumn[] fks = null;
		TableConstraintColumn[] cons = null;
		TableIDXColumn[] uidxs = null;
		TableIDXColumn[] nonuidxs = null;

		IDBConfig config = table.getDbConfig();
		boolean convertUnicode = config.isConvertUnicode();

		String schemaName = table.getSchemaName();
		String tableName = table.getName();

		if (monitor.isCanceled()) {
			return false;
		}

		switch (DBType.getType(con.getMetaData())) {
		case DBType.DB_TYPE_ORACLE:
			if (table instanceof Synonym) {
				Synonym synonym = (Synonym) table;
				schemaName = synonym.getTable_owner();
				tableName = synonym.getTable_name();

			} else if (table instanceof Bookmark) {
				Bookmark bm = (Bookmark) table;
				if (bm.isSynonym()) {
					SynonymInfo info = OracleSynonymInfoSearcher.execute(con, bm.getSchemaName(), bm.getName());
					schemaName = info.getTable_owner();
					tableName = info.getTable_name();
				}
			}
			break;
		}
		if (monitor.isCanceled()) {
			return false;
		}

		if (SchemaSearcher.isSupport(con)) {
			monitor.subTask(Messages.getString("RefreshColumnJob.6")); //$NON-NLS-1$
			// columns = ColumnSearcher.execute(con, schemaName, tableName, convertUnicode);
			IColumnSearcherFactory factory = DefaultColumnSearcherFactory.getFactory(con.getMetaData(), convertUnicode);
			columns = factory.execute(con, schemaName, tableName);

			ts.stop();
			ts.start();

			monitor.worked(1);

			monitor.subTask("Search for PrimaryKey..."); //$NON-NLS-1$
			// pks = ConstraintSearcher.getPKColumns(con, schemaName, tableName);
			IConstraintSearcherFactory constraintFactory = DefaultConstraintSearcherFactory.getFactory(config);
			pks = constraintFactory.getPKColumns(con, schemaName, tableName);
			ts.stop();
			ts.start();

			monitor.worked(1);

			monitor.subTask(Messages.getString("RefreshColumnJob.8")); //$NON-NLS-1$
			// fks = ConstraintSearcher.getFKColumns(con, schemaName, tableName);
			fks = constraintFactory.getFKColumns(con, schemaName, tableName);
			ts.stop();

			monitor.worked(1);

			switch (DBType.getType(con.getMetaData())) {
			case DBType.DB_TYPE_ORACLE:
				ts.start();
				monitor.subTask(Messages.getString("RefreshColumnJob.9")); //$NON-NLS-1$
				// cons = OracleConstraintSearcher.getConstraintColumns(con, schemaName, tableName);
				cons = constraintFactory.getConstraintColumns(con, schemaName, tableName);

				ts.stop();
				ts.start();

				monitor.worked(1);

				monitor.subTask(Messages.getString("RefreshColumnJob.10"));//$NON-NLS-1$
				TableIDXColumn[][] indexies = OracleIndexSearcher.getIDXColumns(con, schemaName, tableName);
				uidxs = indexies[0];
				nonuidxs = indexies[1];
				monitor.worked(2);

				ts.stop();

				// -->

				break;
			default:

				ts.start();
				monitor.subTask(Messages.getString("RefreshColumnJob.12")); //$NON-NLS-1$
				monitor.worked(1);

				monitor.subTask(Messages.getString("RefreshColumnJob.13")); //$NON-NLS-1$
				// uidxs = ConstraintSearcher.getUniqueIDXColumns(con, schemaName, tableName, true);
				uidxs = constraintFactory.getUniqueIDXColumns(con, schemaName, tableName, true);
				ts.stop();
				ts.start();

				monitor.worked(1);


				monitor.subTask(Messages.getString("RefreshColumnJob.14")); //$NON-NLS-1$
				// nonuidxs = ConstraintSearcher.getUniqueIDXColumns(con, schemaName, tableName, false);
				nonuidxs = constraintFactory.getUniqueIDXColumns(con, schemaName, tableName, false);

				ts.stop();

				monitor.worked(1);

				break;
			}

		} else {
			monitor.subTask(Messages.getString("RefreshColumnJob.15")); //$NON-NLS-1$
			// columns = ColumnSearcher.execute(con, null, tableName, convertUnicode);
			IColumnSearcherFactory factory = DefaultColumnSearcherFactory.getFactory(con.getMetaData(), convertUnicode);
			columns = factory.execute(con, null, tableName);

			monitor.worked(1);

			monitor.subTask(Messages.getString("RefreshColumnJob.16")); //$NON-NLS-1$
			// pks = ConstraintSearcher.getPKColumns(con, null, tableName);
			IConstraintSearcherFactory constraintFactory = DefaultConstraintSearcherFactory.getFactory(config);
			pks = constraintFactory.getPKColumns(con, null, tableName);


			monitor.worked(1);

			monitor.subTask(Messages.getString("RefreshColumnJob.17")); //$NON-NLS-1$
			// fks = ConstraintSearcher.getFKColumns(con, null, tableName);
			fks = constraintFactory.getFKColumns(con, null, tableName);

			monitor.worked(1);

			monitor.subTask(Messages.getString("RefreshColumnJob.18")); //$NON-NLS-1$
			monitor.worked(1);

			monitor.subTask(Messages.getString("RefreshColumnJob.19")); //$NON-NLS-1$
			// uidxs = ConstraintSearcher.getUniqueIDXColumns(con, null, tableName, true);
			uidxs = constraintFactory.getUniqueIDXColumns(con, null, tableName, true);
			monitor.worked(1);

			monitor.subTask(Messages.getString("RefreshColumnJob.20")); //$NON-NLS-1$
			// nonuidxs = ConstraintSearcher.getUniqueIDXColumns(con, null, tableName, false);
			nonuidxs = constraintFactory.getUniqueIDXColumns(con, null, tableName, false);
			monitor.worked(1);
		}
		if (monitor.isCanceled()) {
			return false;
		}
		table.setTablePKColumns(pks);
		table.setTableFKColumns(fks);
		table.setTableConstraintColumns(cons);
		table.setTableUIDXColumns(uidxs);
		table.setTableNonUIDXColumns(nonuidxs);
		table.removeChild(table.getChild(DbPluginConstant.TREE_LEAF_LOADING));

		List newColumnList = new ArrayList();

		for (int i = 0; i < columns.length; i++) {
			TableColumn w_column = columns[i];
			TablePKColumn w_pk = getPKColumn(pks, w_column);
			TableFKColumn[] w_fks = getFKColumns(fks, w_column);
			newColumnList.add(w_column.getColumnName());
			TreeLeaf leaf = table.getChild(w_column.getColumnName());
			if (leaf == null) {
				addColumn(table, w_column, w_pk, w_fks);
			} else {
				updateColumn(table, (Column) leaf, w_column, w_pk, w_fks);
			}
			if (monitor.isCanceled()) {
				return false;
			}
		}


		removeDeleteColumn(table, newColumnList);
		table.setExpanded(true);

		return true;
	}


	private void removeDeleteColumn(ITable table, List newColumnList) {
		TreeLeaf[] leafs = table.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (!newColumnList.contains(leaf.getName())) {
				table.removeChild(leaf);
			}
		}
	}


	private void addColumn(ITable table, TableColumn w_column, TablePKColumn w_pk, TableFKColumn[] w_fks) {
		switch (DBType.getType(table.getDbConfig())) {
		case DBType.DB_TYPE_ORACLE:
			table.addChild(new OracleColumn(w_column, w_pk, w_fks));
			break;

		default:
			table.addChild(new Column(w_column, w_pk, w_fks));
			break;
		}

	}

	private void updateColumn(ITable table, Column oldColumn, TableColumn w_column, TablePKColumn w_pk, TableFKColumn[] w_fks) {
		Column newColumn = null;
		switch (DBType.getType(table.getDbConfig())) {
		case DBType.DB_TYPE_ORACLE:
			newColumn = new OracleColumn(w_column, w_pk, w_fks);
			break;
		default:
			newColumn = new Column(w_column, w_pk, w_fks);
			break;
		}
		oldColumn.update(newColumn);

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

}
