package com.tulin.v8.ide.navigator.views.action;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.progress.UIJob;

import com.tulin.v8.core.Configuration;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.navigator.views.Root;
import com.tulin.v8.ide.ui.editors.EditorUtility;
import com.tulin.v8.ide.ui.editors.process.element.ProcessDrawElement;
import com.tulin.v8.ide.ui.internal.FlowDraw;
import com.tulin.v8.ide.ui.internal.FlowFolder;
import com.tulin.v8.ide.utils.FlowUtils;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;
import zigen.plugin.db.ui.jobs.ConnectDBJob;

@SuppressWarnings("rawtypes")
public class ActionUtils {
	private static TreeViewer viewer;

	public static void loadModel(TreeViewer view, final Root root) {
		viewer = view;
		Job job1 = new Job(Messages.getString("View.Action.model.0")) {
			public IStatus run(IProgressMonitor monitor) {
				viewSourceInit(root, monitor);
				return Status.OK_STATUS;
			}
		};
		UIJob job2 = new UIJob(Messages.getString("View.Action.model.0") + " UI") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				viewer.refresh();
				return Status.CANCEL_STATUS;
			}
		};
		ISchedulingRule Schedule_RULE = new ISchedulingRule() {
			public boolean contains(ISchedulingRule rule) {
				return this.equals(rule);
			}

			public boolean isConflicting(ISchedulingRule rule) {
				return this.equals(rule);
			}
		};
		job1.setRule(Schedule_RULE);
		job2.setRule(Schedule_RULE);
		job1.setUser(true);
		job1.schedule();
		job2.setUser(false);
		job2.schedule();
	}

	public static void viewSourceInit(Root root, IProgressMonitor monitor) {
		monitor.beginTask(Messages.getString("View.Action.model.1"), 100);
		root.removeChildAll();
		Root DATA = new Root("DATA");
//		Root BIZ = new Root("BIZ");
//		BIZ.setPath(new File(StudioPlugin.getBIZPath()).getAbsolutePath());
//		Root UI = new Root("UI");
//		UI.setPath(new File(StudioPlugin.getUIPath()).getAbsolutePath());
		root.addChild(DATA);
//		root.addChild(BIZ);
//		root.addChild(UI);
		try {
			Map<String, Map<String, String>> rm = Configuration.getConfig();
			Set<String> k = rm.keySet();
			Iterator<String> it = k.iterator();
			int ms = rm.size();
			while (it.hasNext()) {
				monitor.worked(1 / ms);
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
				DATA.addChild(db);
				// adddataview(db, key, m.get("username"), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		loadProcess(root);// 加载流程process
		monitor.worked(1);

//		traverseItem(BIZ, new File(StudioPlugin.getBIZPath()), "BIZ", monitor);
//		traverseItemUI(UI, new File(StudioPlugin.getUIPath()), "UI", monitor);
//		if (checkmobileUIModel()) {
//			Root mobileUI = new Root("mobileUI");
//			mobileUI.setPath(new File(StudioPlugin.getMobileUIPath()).getAbsolutePath());
//			root.addChild(mobileUI);
//			traverseItem(mobileUI, new File(StudioPlugin.getMobileUIPath()), "mobileUI", monitor);
//		}
		monitor.done();
	}

	public static void loadProcess(TreeNode BIZ) {
//		BIZ.removeChildAll();
		TreeNode process = new TreeNode("PROCESS");
		process.setTvtype("process");
		BIZ.addChild(process);
		refreshProcess(process);
	}

	public static void refreshProcess(final TreeNode node) {
		if (!"process".equals(node.getTvtype())) {
			return;
		}
		node.removeChildAll();
		List<Map> rootf = FlowUtils.getRootFolder();
		for (int i = 0; i < rootf.size(); i++) {
			Map m = rootf.get(i);
			FlowFolder folder = new FlowFolder(m);
			folder.setTvtype("flowfolder");
			node.addChild(folder);
			loadFlowFolder(folder);
		}
	}

	public static void loadFlowFolder(FlowFolder folder) {
		folder.removeChildAll();
		String pid = folder.getElement().getSid();
		List<Map> childf = FlowUtils.getChildFolder(pid);
		for (int i = 0; i < childf.size(); i++) {
			Map m = childf.get(i);
			FlowFolder chilfolder = new FlowFolder(m);
			chilfolder.setTvtype("flowfolder");
			folder.addChild(chilfolder);
			loadFlowFolder(chilfolder);
		}
		loadFlowDraw(folder);
	}

	private static void loadFlowDraw(FlowFolder folder) {
		String pid = folder.getElement().getSid();
		List<Map> dlist = FlowUtils.getDrawByFolderId(pid);
		for (int i = 0; i < dlist.size(); i++) {
			Map m = dlist.get(i);
			FlowDraw draw = new FlowDraw(m);
			draw.setTvtype("flowdraw");
			folder.addChild(draw);
		}
	}

	public static void reloadFlowDraw(FlowDraw node) {
		Map m = FlowUtils.getDrawById(node.getElement().getSid());
		ProcessDrawElement element = new ProcessDrawElement(m);
		node.setElement(element);
	}

	public static void refreshFolder(final TreeNode node, final File file, final boolean clear) {
		Job job1 = new Job("Refresh Folder") {
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.getString("View.Action.model.2"), 100);
				if (clear) {
					node.removeChildAll();
				}
				if (node.getBiz() == null) {
					if ("UI".equals(node.getName())) {
						traverseItemUI(node, file, "UI", monitor);
					} else {
						traverseItem(node, file, node.getName(), monitor);
					}
				} else if ("UI".equals(node.getBiz())) {
					traverseItemUI(node, file, "UI", monitor);
				} else {
					traverseItem(node, file, node.getBiz(), monitor);
				}
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		UIJob job2 = new UIJob("Refresh Folder UI") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				viewer.refresh();
				return Status.CANCEL_STATUS;
			}
		};
		ISchedulingRule Schedule_RULE = new ISchedulingRule() {
			public boolean contains(ISchedulingRule rule) {
				return this.equals(rule);
			}

			public boolean isConflicting(ISchedulingRule rule) {
				return this.equals(rule);
			}
		};
		job1.setRule(Schedule_RULE);
		job2.setRule(Schedule_RULE);
		job1.setUser(true);
		job1.schedule();
		job2.setUser(false);
		job2.schedule();
	}

	public static void traverseItem(TreeNode tree, File file, String biz, IProgressMonitor monitor) {
		File[] files = file.listFiles();
		int tol = files.length;
		for (File subfile : files) {
			if (subfile.getName().indexOf(".") == 0 || subfile.getName().equals("classes")
					|| subfile.getName().equals("lib"))
				continue;
			if (subfile.isDirectory()) {
				if (subfile.toString().indexOf("$") < 0) {
					TreeNode pt1 = new TreeNode(subfile.getName());
					pt1.setTvtype("folder");
					pt1.setBiz(biz);
					pt1.setPath(subfile.getAbsolutePath());
					tree.addChild(pt1);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
					}
					traverseItem(pt1, subfile, biz, monitor);
				}
			} else if (subfile.isFile()) {
				if (subfile.toString().endsWith("SqlMapConfig.xml")) {
					continue;
				}
				TreeNode to2 = new TreeNode(subfile.getName());
				to2.setTvtype("file");
				to2.setBiz(biz);
				to2.setPath(subfile.getAbsolutePath());
				tree.addChild(to2);
			}
			monitor.worked(1 / tol);
		}
	}

	public static void traverseItemUI(TreeNode tree, File file, String ui, IProgressMonitor monitor) {
		File[] files = file.listFiles();
		int tols = files.length;
		for (File subfile : files) {
			if (subfile.getName().indexOf(".") == 0 || subfile.getName().equals("classes")
					|| subfile.getName().equals("lib"))
				continue;
			if (subfile.isDirectory()) {
				if (subfile.getName().equals("mobileUI")) {
					continue;
				}
				if (subfile.getName().equals("META-INF")) {
					continue;
				}
				if (subfile.toString().indexOf("$") < 0) {
					TreeNode pt1 = new TreeNode(subfile.getName());
					pt1.setTvtype("folder");
					pt1.setBiz(ui);
					pt1.setPath(subfile.getAbsolutePath());
					tree.addChild(pt1);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
					}
					traverseItemUI(pt1, subfile, ui, monitor);
				}
			} else if (subfile.isFile()) {
				TreeNode to2 = new TreeNode(subfile.getName());
				to2.setTvtype("file");
				to2.setBiz(ui);
				to2.setPath(subfile.getAbsolutePath());
				tree.addChild(to2);
			}
			monitor.worked(1 / tols);
		}
	}

	public static void loadDataModel(final TreeNode DATA) {
		DATA.setTvtype("DB");
		DATA.removeChildAll();
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(Messages.getString("View.Action.model.3"), 30);
				try {
					Map<String, Map<String, String>> rm = Configuration.getConfig();
					Set<String> k = rm.keySet();
					Iterator<String> it = k.iterator();
					while (it.hasNext()) {
						monitor.worked((int) ((1.0 / k.size()) * 100));
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
						DATA.addChild(db);
						adddataview(db, key, m.get("username"), false);
					}
				} catch (Exception e) {
				}
				monitor.done();
			}
		};
		try {
			new ProgressMonitorDialog(StudioPlugin.getShell()).run(true, true, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void adddataview(TreeNode treeparent, String dbkey, String username, boolean isInit) {
		try {
			Schema user = new Schema(username);
			user.setTvtype("dbuser");
			user.setDbkey(dbkey);
			treeparent.addChild(user);
			if (isInit == false) {
				DataBase db = (DataBase) treeparent;
				ConnectDBJob job = new ConnectDBJob(viewer, db);
				job.setPriority(ConnectDBJob.SHORT);
				job.setUser(false);
				job.setSystem(false);
				job.schedule();
			}
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
		}
	}

	public static void adddataTypeview(Schema treeparent, String dbkey) {
		// Map<String, List<String>> TreeitemData;
		// try {
		// TreeitemData = DataSelectPage.getDataObject(dbkey);
		// Folder table = new Folder("TABLE");
		// table.setTvtype("dbtype");
		// table.setDbkey(dbkey);
		// List<String> litable = TreeitemData.get("TABLE");
		// for (int i = 0; i < litable.size(); i++) {
		// Table treeitem = new Table(litable.get(i));
		// treeitem.setTvtype("table");
		// treeitem.setDbkey(dbkey);
		// table.addChild(treeitem);
		// }
		// Folder view = new Folder("VIEW");
		// view.setTvtype("dbtype");
		// view.setDbkey(dbkey);
		// List<String> liview = TreeitemData.get("VIEW");
		// for (int i = 0; i < liview.size(); i++) {
		// View treeitem = new View(liview.get(i));
		// treeitem.setTvtype("view");
		// treeitem.setDbkey(dbkey);
		// view.addChild(treeitem);
		// }
		//
		// treeparent.addChild(table);
		// treeparent.addChild(view);
		// } catch (Exception e) {
		// Sys.packErrMsg(e.toString());
		// }
	}

	public static void adddataTableview(TreeNode treeparent, String dbkey) {
		Map<String, List<String>> TreeitemData;
		try {
			TreeitemData = CommonUtil.getDataObject(dbkey);
			List<String> litable = TreeitemData.get("TABLE");
			for (int i = 0; i < litable.size(); i++) {
				Table treeitem = new Table(litable.get(i));
				treeitem.setTvtype("table");
				treeitem.setDbkey(dbkey);
				treeparent.addChild(treeitem);
			}
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
		}
	}

	public static void adddataViewview(TreeNode treeparent, String dbkey) {
		Map<String, List<String>> TreeitemData;
		try {
			TreeitemData = CommonUtil.getDataObject(dbkey);
			List<String> liview = TreeitemData.get("VIEW");
			for (int i = 0; i < liview.size(); i++) {
				View treeitem = new View(liview.get(i));
				treeitem.setTvtype("view");
				treeitem.setDbkey(dbkey);
				treeparent.addChild(treeitem);
			}
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
		}
	}

//	private static boolean checkmobileUIModel() {
//		String currentPath = StudioConfig.getWorkspacesPath();
//		String uiPath = currentPath + "\\" + StudioConfig.PHANTOM_PROJECT_NAME + "\\" + StudioConfig.PROJECT_WEB_FOLDER
//				+ "\\mobileUI";
//		File uiroot = new File(uiPath);
//		return uiroot.exists();
//	}

	public static void clearDir(File folder) {
		File[] fls = folder.listFiles();
		if (fls.length < 1) {
			folder.delete();
		}
		for (int i = 0; i < fls.length; i++) {
			if (fls[i].isDirectory()) {
				clearDir(fls[i]);
			}
			if (fls[i].isFile()) {
				IWorkbenchPage page = StudioPlugin.getActivePage();
				IEditorPart part = EditorUtility.isOpenInEditor(fls[i]);
				if (part != null) {
					page.closeEditor(part, false);
				}
				fls[i].delete();
			}
		}
		folder.delete();
	}
}
