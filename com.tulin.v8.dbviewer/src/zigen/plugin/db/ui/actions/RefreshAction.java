/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.jobs.OracleSequeceSearchJob;
import zigen.plugin.db.ui.jobs.OracleSourceSearchJob;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;
import zigen.plugin.db.ui.jobs.RefreshTableJob;
import zigen.plugin.db.ui.jobs.TableTypeSearchJob;

public class RefreshAction extends Action {

	private boolean showDialog = true;

	private TreeViewer viewer = null;

	public RefreshAction(TreeViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("RefreshAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("RefreshAction.1")); //$NON-NLS-1$
		this.setAccelerator(SWT.F5);
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_REFRESH));
		this.setDisabledImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_REFRESH));
	}

	public void run() {

		Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
		try {

			if (element instanceof DataBase) {
				DataBase db = (DataBase) element;
				// closeEditor(db);
				db.removeChildAll();
				db.setExpanded(false);
				db.setConnected(true);
				ConnectDBJob job = new ConnectDBJob(viewer, db);
				job.setPriority(ConnectDBJob.SHORT);
				job.setUser(false);
				job.setSystem(false);
				job.schedule();

			} else if (element instanceof Schema) {
				Schema schema = (Schema) element;
				schema.setExpanded(true);
				TableTypeSearchJob job = new TableTypeSearchJob(viewer, schema);
				job.setUser(showDialog);
				job.setPriority(TableTypeSearchJob.SHORT);
				job.schedule();

			} else if (element instanceof Folder) {
				Folder folder = (Folder) element;
				folder.setExpanded(true);
				Schema schema = folder.getSchema();
				if (schema != null) {
					switch (DBType.getType(schema.getDbConfig())) {
					case DBType.DB_TYPE_ORACLE:
						if (schema != null) {
							if ("SEQUENCE".equals(folder.getName())) { //$NON-NLS-1$
								OracleSequeceSearchJob job = new OracleSequeceSearchJob(viewer, folder);
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
					}
				}
				System.out.println("Refresh!!");
				RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
				job.setPriority(RefreshFolderJob.SHORT);
				job.setUser(showDialog);
				job.schedule();

			} else if (element instanceof ITable) {
				ITable table = (ITable) element;
				table.setExpanded(true);
				RefreshTableJob job = new RefreshTableJob(viewer, table);
				job.setPriority(RefreshTableJob.SHORT);
				job.setUser(showDialog);
				job.schedule();

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
