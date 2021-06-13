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

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.TableSearcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleSynonymInfoSearcher;
import zigen.plugin.db.ext.oracle.internal.SynonymInfo;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.View;

public class RefreshTableJob extends AbstractJob {

	private TreeViewer viewer;

	private ITable tableNode;

	public RefreshTableJob(TreeViewer viewer, ITable tableNode) {
		super(Messages.getString("RefreshTableJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.tableNode = tableNode;
	}

	protected IStatus run(IProgressMonitor monitor) {
		TableInfo tableInfo = null;
		try {
			Connection con = Transaction.getInstance(tableNode.getDbConfig()).getConnection();

			if (SchemaSearcher.isSupport(con)) {
				tableInfo = TableSearcher.execute(con, tableNode.getSchemaName(), tableNode.getName(), tableNode.getFolderName());
			} else {
				tableInfo = TableSearcher.execute(con, null, tableNode.getName(), tableNode.getFolderName());
			}

			String lable = tableNode.getFolderName();

			if ("SYNONYM".equals(lable)) { //$NON-NLS-1$
				switch (DBType.getType(con.getMetaData())) {
				case DBType.DB_TYPE_ORACLE:
					String owner = tableNode.getSchemaName();
					String synonymName = tableInfo.getName();
					SynonymInfo info = OracleSynonymInfoSearcher.execute(con, owner, synonymName);

					Synonym _synonym = new Synonym(tableInfo.getName(), tableInfo.getComment());
					_synonym.setSynonymInfo(info);

					if (tableNode instanceof Bookmark) {
						((Bookmark) tableNode).update(_synonym);
					} else {
						((Synonym) tableNode).update(_synonym);
					}

					break;
				default:
					break;
				}
			} else if ("VIEW".equals(lable)) { //$NON-NLS-1$

				View nTable = new View(tableInfo.getName(), tableInfo.getComment());
				if (tableNode instanceof Bookmark) {
					((Bookmark) tableNode).update(nTable);
				} else {
					((View) tableNode).update(nTable);
				}

			} else {
				// Update
				Table nTable = new Table(tableInfo.getName(), tableInfo.getComment());
				if (tableNode instanceof Bookmark) {
					((Bookmark) tableNode).update(nTable);
				} else {
					((Table) tableNode).update(nTable);
				}
			}

			RefreshColumnJob job2 = new RefreshColumnJob(viewer, tableNode);
			job2.setPriority(RefreshColumnJob.SHORT);
			job2.schedule();
			try {
				job2.join();
			} catch (InterruptedException e) {
				DbPlugin.log(e);
			}

		} catch (Exception e) {
			showErrorMessage(Messages.getString("RefreshTableJob.3"), e); //$NON-NLS-1$
		} finally {
		}
		return Status.OK_STATUS;
	}

}
