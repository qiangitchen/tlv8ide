/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.sql.Connection;

import org.eclipse.jface.viewers.StructuredViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;

public class OracleSourceSearchAction implements Runnable {

	private StructuredViewer viewer;

	private Folder folder;

	public OracleSourceSearchAction(StructuredViewer viewer, Folder folder) {
		this.viewer = viewer;
		this.folder = folder;
	}

	public void run() {
		try {
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();

			Schema schema = (Schema) folder.getParent();

			String owner = schema.getName();
			String type = folder.getName();

			OracleSourceInfo[] infos = OracleSourceSearcher.execute(con, owner, type);

			AddSources(con, folder, infos);

			if (viewer != null) {
				viewer.refresh(folder);
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private static void AddSources(Connection con, Folder folder, OracleSourceInfo[] infos) throws Exception {
		for (int i = 0; i < infos.length; i++) {
			OracleSource source = new OracleSource();
			source.setOracleSourceInfo(infos[i]);
			folder.addChild(source);
		}
	}

}
