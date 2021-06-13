/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.core.DBType;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.jobs.OracleSequeceSearchJob;
import zigen.plugin.db.ui.jobs.OracleSourceSearchJob;
import zigen.plugin.db.ui.jobs.RefreshColumnJob;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;
import zigen.plugin.db.ui.jobs.TableTypeSearchJob;

public class TreeViewListener implements ITreeViewerListener {

//	private boolean showDialog = true;
	private boolean showDialog = false;

	public void treeCollapsed(TreeExpansionEvent event) {
	}

	public void treeExpanded(TreeExpansionEvent event) {
		Object element = event.getElement();
		TreeViewer viewer = (TreeViewer) event.getTreeViewer();

		if (element instanceof DataBase) {
			DataBase db = (DataBase) element;
			if (!db.isExpanded()) {
				db.setExpanded(true);
				ConnectDBJob job = new ConnectDBJob(viewer, db);
				job.setPriority(ConnectDBJob.SHORT);
				job.setUser(false);
				job.setSystem(false);
				job.schedule();

			}

		} else
		// ADD 2007/06/20 ZIGEN -->
		if (element instanceof Schema) {
			Schema schema = (Schema) element;
			if (!schema.isExpanded()) {
				schema.setExpanded(true);
				TableTypeSearchJob job = new TableTypeSearchJob(viewer, schema);
				job.setPriority(TableTypeSearchJob.SHORT);
				job.setUser(showDialog);
				job.schedule();

			}

		} else if (element instanceof ITable) {
			ITable table = (ITable) element;
			if (!table.isExpanded()) {
				RefreshColumnJob job = new RefreshColumnJob(viewer, table);
				job.setPriority(RefreshColumnJob.SHORT);
				job.setUser(false);
				job.schedule();
			}

		} else if (element instanceof Folder) {
			Folder folder = (Folder) element;

			if (!folder.isExpanded()) {
				folder.setExpanded(true);
				Schema schema = folder.getSchema();

				if (schema != null) {
					switch (DBType.getType(schema.getDbConfig())) {
					case DBType.DB_TYPE_ORACLE:
						if (schema != null) {
							if("TABLE".equals(folder.getName())){
								return;

							}else if ("SEQUENCE".equals(folder.getName())) { //$NON-NLS-1$
								OracleSequeceSearchJob job = new OracleSequeceSearchJob(viewer, folder);
								job.setPriority(OracleSequeceSearchJob.SHORT);
								job.setUser(showDialog);
								job.schedule();

								return;

							} else if ("VIEW".equals(folder.getName())) { //$NON-NLS-1$
								System.out.println("treeExpanded!!");
								RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
								job.setPriority(OracleSequeceSearchJob.SHORT);
								job.setUser(showDialog);
								job.schedule();

								return;
							} else {
								String[] sTypes = schema.getSourceType();
								if (sTypes != null) {
									for (int i = 0; i < sTypes.length; i++) {
										String stype = sTypes[i];
										if (stype.equals(folder.getName())) {

											OracleSourceSearchJob job = new OracleSourceSearchJob(viewer, folder);
											job.setPriority(OracleSourceSearchJob.SHORT);
											job.setUser(showDialog);
											job.schedule();

											return;
										}
									}
								}
							}
						}
					default:
						RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
						job.setPriority(RefreshFolderJob.SHORT);
						job.setUser(showDialog);
						job.schedule();

						return;
					}
				}

			}
		}

	}
}
