/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Schema;

public class RefreshSchemaJob extends AbstractJob {

	private TreeViewer viewer;

	private Schema schema;

	public RefreshSchemaJob(TreeViewer viewer, Schema schema) {
		super(Messages.getString("RefreshSchemaJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.schema = schema;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {

			List folders = schema.getChildren();

			monitor.beginTask(Messages.getString("RefreshSchemaJob.1"), folders.size()); //$NON-NLS-1$

			for (Iterator iter = folders.iterator(); iter.hasNext();) {
				Folder folder = (Folder) iter.next();
				monitor.subTask(folder.getName() + Messages.getString("RefreshSchemaJob.2")); //$NON-NLS-1$

				folder.setExpanded(true);
				if (schema != null) {
					switch (DBType.getType(schema.getDbConfig())) {
					case DBType.DB_TYPE_ORACLE:
						if (schema != null) {
							if ("SEQUENCE".equals(folder.getName())) { //$NON-NLS-1$
								OracleSequeceSearchJob job = new OracleSequeceSearchJob(viewer, folder);
								job.setPriority(OracleSequeceSearchJob.SHORT);
								job.schedule();
								break;
							} else {
								String[] sTypes = schema.getSourceType();
								if (sTypes != null) {
									for (int i = 0; i < sTypes.length; i++) {
										String stype = sTypes[i];
										if (stype.equals(folder.getName())) {
											OracleSourceSearchJob job = new OracleSourceSearchJob(viewer, folder);
											job.setPriority(OracleSourceSearchJob.SHORT);
											job.schedule();
											break;
										}
									}
								}
							}
						}
					default:
					}
				}
				RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
				job.setPriority(RefreshFolderJob.SHORT);
				job.schedule();

				try {
					job.join();
				} catch (InterruptedException e) {
					DbPlugin.log(e);
				}

				monitor.worked(1);

			}
			schema.setExpanded(true);
			showResults(new RefreshTreeNodeAction(viewer, schema, RefreshTreeNodeAction.MODE_NOTHING));

		} catch (Exception e) {
			schema.setExpanded(false);

			showErrorMessage(Messages.getString("RefreshSchemaJob.4"), e); //$NON-NLS-1$
		} finally {
		}

		return Status.OK_STATUS;
	}

}
