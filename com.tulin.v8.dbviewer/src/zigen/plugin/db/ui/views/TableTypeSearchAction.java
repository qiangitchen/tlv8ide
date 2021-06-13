/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import java.sql.Connection;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.Schema;

public class TableTypeSearchAction implements Runnable {

	TreeViewer viewer;

	Schema schema;

	public TableTypeSearchAction(TreeViewer viewer, Schema schema) {
		this.viewer = viewer;
		this.schema = schema;
	}

	public void run() {
		try {

			Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
			if (element instanceof Schema) {
				Schema schema = (Schema) element;

				// schema.removeChild(schema.getChild(DbPluginConstant.TREE_LEAF_LOADING));
				// schema.removeChildAll();

				Connection con = Transaction.getInstance(schema.getDbConfig()).getConnection();

				String[] tableTypes = schema.getDataBase().getTableType();

				for (int i = 0; i < tableTypes.length; i++) {
					String[] types = new String[] {tableTypes[i]};

					if ("SEQUENCE".equals(tableTypes[i])) { //$NON-NLS-1$
						switch (DBType.getType(schema.getDbConfig())) {
						case DBType.DB_TYPE_ORACLE:
							Folder folder = new Folder();
							folder.setName(tableTypes[i]);
							OracleSequence seq = new OracleSequence();
							seq.setName(DbPluginConstant.TREE_LEAF_LOADING);
							folder.addChild(seq);
							schema.addChild(folder);
						default:
							// Folder folder = new Folder();
							// folder.setName(tableTypes[i]);
							// OracleSequence seq = new OracleSequence();
							// seq.setName(DbPluginConstant.TREE_LEAF_LOADING);
							// folder.addChild(seq);
							// schema.addChild(folder);
						}

					} else {
						TableInfo[] tables = TableSearcher.execute(con, schema.getName(), types);
						TableSearchThread.addFolderAndTables(con, schema, tableTypes[i], tables);

					}

				}
				schema.setExpanded(true);

				viewer.refresh(schema);

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}

}
