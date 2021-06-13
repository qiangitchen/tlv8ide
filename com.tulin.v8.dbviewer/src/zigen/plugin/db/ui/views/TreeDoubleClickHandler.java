/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.jobs.OpenEditorJob;
import zigen.plugin.db.ui.jobs.OpenSourceEditorJob;
import zigen.plugin.db.ui.jobs.OracleSequeceSearchJob;
import zigen.plugin.db.ui.jobs.OracleSourceSearchJob;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;
import zigen.plugin.db.ui.jobs.TableTypeSearchJob;

public class TreeDoubleClickHandler implements IDoubleClickListener {

	private boolean showDialog = false;

	public TreeDoubleClickHandler() {}

	public void doubleClick(DoubleClickEvent event) {

		try {

			Viewer view = event.getViewer();
			ISelection selection = event.getSelection();

			if (view instanceof TreeViewer && selection instanceof StructuredSelection) {
				TreeViewer viewer = (TreeViewer) view;
				Object element = ((StructuredSelection) selection).getFirstElement();

				if (element instanceof DataBase) {
					DataBase db = (DataBase) element;
					if (!db.isExpanded()) {
						db.setConnected(true);
						db.setExpanded(true);
						ConnectDBJob job = new ConnectDBJob(viewer, db);
						job.setPriority(ConnectDBJob.SHORT);
						job.setUser(false);
						job.setSystem(false);
						job.schedule();

					} else {
						changeExpandedState(viewer, (TreeNode) element);
					}

				} else if (element instanceof ITable) {
					OpenEditorJob job = new OpenEditorJob(viewer, (ITable) element);
					job.setPriority(OpenEditorJob.SHORT);
					job.setUser(showDialog);
					job.schedule();

				} else if (element instanceof OracleSource || element instanceof OracleSequence) {
					OpenSourceEditorJob job = new OpenSourceEditorJob(viewer);
					job.setPriority(OpenSourceEditorJob.SHORT);
					job.setUser(showDialog);
					job.schedule();

				} else if (element instanceof TreeNode) {
					changeExpandedState(viewer, (TreeNode) element);
				}

			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}

	}

	private void changeExpandedState(TreeViewer viewer, TreeNode element) {

		if (!viewer.getExpandedState(element)) {

			viewer.expandToLevel(element, 1);

			if (element instanceof Schema) {

				Schema schema = (Schema) element;

				if (!schema.isExpanded()) {
					schema.setExpanded(true);
					TableTypeSearchJob job = new TableTypeSearchJob(viewer, schema);
					job.setPriority(TableTypeSearchJob.SHORT);
					job.setUser(showDialog);
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

									System.out.println("changeExpandedState!!");
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
							break;
						}
					}
				}

			}

		} else {
			viewer.collapseToLevel(element, 1);
		}

	}
}
