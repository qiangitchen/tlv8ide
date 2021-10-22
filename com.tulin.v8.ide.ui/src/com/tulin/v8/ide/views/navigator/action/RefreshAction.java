package com.tulin.v8.ide.views.navigator.action;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.core.Configuration;
import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.internal.FlowDraw;
import com.tulin.v8.ide.internal.FlowFolder;
import com.tulin.v8.ide.internal.Root;
import com.tulin.v8.ide.utils.ActionUtils;
import com.tulin.v8.ide.utils.SelectionUtil;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.jobs.OracleSequeceSearchJob;
import zigen.plugin.db.ui.jobs.OracleSourceSearchJob;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;
import zigen.plugin.db.ui.jobs.RefreshTableJob;
import zigen.plugin.db.ui.jobs.TableTypeSearchJob;

public class RefreshAction extends Action implements Runnable {
	TreeViewer viewer = null;
	private boolean showDialog = true;

	public RefreshAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.Refresh.1"));
		setToolTipText(Messages.getString("View.Action.Refresh.2"));
		setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("refresh.gif")));
	}

	public void run() {
		ISelection selection = this.viewer.getSelection();
		Object obj = SelectionUtil.getSingleElement(selection);
		try {
			Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
			if (element instanceof FlowDraw) {
				ActionUtils.reloadFlowDraw((FlowDraw) element);
			} else if (element instanceof FlowFolder) {
				ActionUtils.loadFlowFolder((FlowFolder) element);
			} else if (element instanceof DataBase) {
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
				return;
			} else if (element instanceof Schema) {
				Schema schema = (Schema) element;
				schema.setExpanded(true);
				TableTypeSearchJob job = new TableTypeSearchJob(viewer, schema);
				job.setUser(showDialog);
				job.setPriority(TableTypeSearchJob.SHORT);
				job.schedule();
				return;
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
				RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
				job.setPriority(RefreshFolderJob.SHORT);
				job.setUser(showDialog);
				job.schedule();
				return;
			} else if (element instanceof ITable) {
				ITable table = (ITable) element;
				table.setExpanded(true);
				RefreshTableJob job = new RefreshTableJob(viewer, table);
				job.setPriority(RefreshTableJob.SHORT);
				job.setUser(showDialog);
				job.schedule();
				return;
			} else if (element instanceof Root) {
				Root data = (Root) element;
				if (data.getName().equals("DATA")) {
					data.removeChildAll();
					try {
						Map<String, Map<String, String>> rm = Configuration.getConfig();
						Set<String> k = rm.keySet();
						Iterator<String> it = k.iterator();
						while (it.hasNext()) {
							try {
								Thread.sleep(5);
							} catch (InterruptedException e) {
							}
							String key = (String) it.next();
							Map<String, String> m = rm.get(key);
							IDBConfig dbconfig = DBConfigManager.getDBConfig(key);
							DataBase db = new DataBase(dbconfig);
							db.setTvtype("dbkey");
							db.setUsername(m.get("username"));
							data.addChild(db);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		if (obj != null) {
			TreeNode node = (TreeNode) obj;
			if ("process".equals(node.getTvtype())) {
				ActionUtils.refreshProcess(node);
			}
			if ("folder".equals(node.getTvtype())) {
				ActionUtils.refreshFolder(node, new File(node.getPath()), true);
			}
			if (node.getBiz() == null && "BIZ".equals(node.getName())) {
				ActionUtils.loadProcess(node);
				ActionUtils.refreshFolder(node, new File(StudioPlugin.getBIZPath()), false);
			}
			if (node.getBiz() == null && "UI".equals(node.getName())) {
				ActionUtils.refreshFolder(node, new File(StudioPlugin.getUIPath()), true);
			}
			if (node.getBiz() == null && "mobileUI".equals(node.getName())) {
				ActionUtils.refreshFolder(node, new File(StudioPlugin.getMobileUIPath()), true);
			}
			if (node.getPath() != null) {
				IFile iFile = StudioPlugin.getWorkspace().getRoot().getFileForLocation(new Path(node.getPath()));
				try {
					iFile.refreshLocal(0, null);
				} catch (Exception ee) {
				}
			}
			this.viewer.refresh(node);
		} else {
			Sys.printErrMsg(Messages.getString("View.Action.Refresh.3"));
		}
	}

	public boolean isShowDialog() {
		return showDialog;
	}

	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}

}
