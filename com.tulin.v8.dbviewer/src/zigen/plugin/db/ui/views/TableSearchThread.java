/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import java.sql.Connection;

import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSynonymInfoSearcher;
import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;

public class TableSearchThread implements Runnable {

	StructuredViewer viewer;

	Folder folder;

	public TableSearchThread(StructuredViewer viewer, Folder folder) {
		this.viewer = viewer;
		this.folder = folder;
	}

	public void run() {
		TableInfo[] tables = null;
		try {
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();
			if (SchemaSearcher.isSupport(con)) {
				tables = TableSearcher.execute(con, folder.getSchema().getName(), new String[] {folder.getName()});
			} else {
				tables = TableSearcher.execute(con, null, new String[] {folder.getName()});
			}
			AddTables(con, folder.getSchema(), folder, tables);

			viewer.refresh(folder);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}
	}

	public static void addFolderAndTables(Connection con, TreeNode parent, String label, TableInfo[] tables) throws Exception {
		Folder folder = new Folder(label);
		folder.setTvtype("dbtype");
		folder.setDbkey(parent.getParent().getName());
		if (parent instanceof Schema) {
			AddTables(con, (Schema) parent, folder, tables);
		} else {
			AddTables(con, null, folder, tables);
		}
		parent.addChild(folder);

	}

	public static void AddTables(Connection con, Schema schema, Folder folder, TableInfo[] tables) throws Exception {
		String label = folder.getName();

		for (int i = 0; i < tables.length; i++) {
			TableInfo tableinfo = tables[i];
			TreeNode node;
			if ("SYNONYM".equals(label)) { //$NON-NLS-1$
				node = new Synonym(tableinfo.getName(), tableinfo.getComment());
				switch (DBType.getType(con.getMetaData())) {
				case DBType.DB_TYPE_ORACLE:
					if (schema != null) {
						String owner = schema.getName();
						String synonymName = tableinfo.getName();
						SynonymInfo info = OracleSynonymInfoSearcher.execute(con, owner, synonymName);
						((Synonym) node).setSynonymInfo(info);
					} else {
						throw new Exception(Messages.getString("TableSearchThread.1")); //$NON-NLS-1$
					}
					break;
				default:
					break;
				}

			} else if ("VIEW".equals(label)) { //$NON-NLS-1$
				node = new View(tableinfo.getName(), tableinfo.getComment());
				node.setTvtype("view");
				node.setDbkey(schema.getParent().getName());
			} else {
				node = new Table(tableinfo.getName(), tableinfo.getComment());
				node.setTvtype("table");
				node.setDbkey(schema.getParent().getName());
			}

			folder.addChild(node);

			TableColumn tColumn = new TableColumn();
			tColumn.setColumnName(DbPluginConstant.TREE_LEAF_LOADING);
			node.addChild(new Column(tColumn));
		}
	}

}
