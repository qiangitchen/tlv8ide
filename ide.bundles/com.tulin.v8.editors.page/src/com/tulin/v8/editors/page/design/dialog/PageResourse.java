package com.tulin.v8.editors.page.design.dialog;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.utils.CommonUtil;

public class PageResourse {

	private static String PHANTOM_PROJECT_NAME;
	private static String PROJECT_WEB_FOLDER;

	public static String getPagePath() {
		IProject project = TuLinPlugin.getCurrentProject();
		String currentPath = project.getLocation().toOSString();
		PHANTOM_PROJECT_NAME = project.getName();
		PROJECT_WEB_FOLDER = project.getFolder("WebContent").getName();
		if (PROJECT_WEB_FOLDER == null) {
			PROJECT_WEB_FOLDER = project.getFolder("WebRoot").getName();
		}
		String dps = CommonUtil.getPathdep();
		return currentPath + dps + PROJECT_WEB_FOLDER;
	}

	public static void loadFold(TreeItem treeitem, String dirPath) {
		String dps = CommonUtil.getPathdep();
		if (!dirPath.endsWith(".svn") && !dirPath.endsWith("WEB-INF") && !dirPath.endsWith("META-INF")) {
			File file = new File(dirPath);
			File[] subFiles = file.listFiles();
			for (File subFile : subFiles) {
				if (subFile.isFile()) {
					if (subFile.getName().toLowerCase().endsWith(".js")
							|| subFile.getName().toLowerCase().endsWith(".css")) {
						String f = dirPath + dps + subFile.getName();
						f = CommonUtil.rebuildFilePath(f);
						f = f.substring(f.indexOf("/" + PHANTOM_PROJECT_NAME + "/"));
						f = f.replace("/" + PROJECT_WEB_FOLDER + "/", "/");
						TreeItem nitem = new TreeItem(treeitem, SWT.NONE);
						nitem.setText(f);
						if (f.endsWith(".js")) {
							nitem.setImage(TuLinPlugin.getIcon("js.gif"));
						} else {
							nitem.setImage(TuLinPlugin.getIcon("css.gif"));
						}
					}
				} else {
					String f = subFile.getAbsolutePath();
					f = CommonUtil.rebuildFilePath(f);
					f = f.substring(f.indexOf("/" + PHANTOM_PROJECT_NAME + "/"));
					f = f.replace("/" + PROJECT_WEB_FOLDER + "/", "/");
					if (!f.endsWith(".svn") && !f.endsWith("WEB-INF") && !f.endsWith("META-INF")
							&& isPageFolder(subFile)) {
						TreeItem nitem = new TreeItem(treeitem, SWT.NONE);
						nitem.setText(f);
						nitem.setImage(TuLinPlugin.getIcon("folder.gif"));
						loadFold(nitem, subFile.getAbsolutePath());
					}
				}
			}
		}
	}

	public static boolean isPageFolder(File fl) {
		boolean rt = false;
		File[] subFiles = fl.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isFile()) {
				if (subFile.getName().toLowerCase().endsWith(".js") || subFile.getName().toLowerCase().endsWith(".css"))
					return true;
			} else {
				rt = isPageFolder(subFile);
			}
		}
		return rt;
	}

	public static void loadFileTree(Tree adtree) {
		TreeItem nitem = new TreeItem(adtree, SWT.NONE);
		nitem.setText("root");
		nitem.setImage(TuLinPlugin.getIcon("folder.gif"));
		loadFold(nitem, getPagePath());
	}

	public static String transeFile(String editFielPath, String fielName) {
		if (editFielPath.indexOf("/" + PROJECT_WEB_FOLDER + "/") > -1) {
			String containerPath = editFielPath
					.substring(editFielPath.indexOf("/" + PROJECT_WEB_FOLDER + "/") + PROJECT_WEB_FOLDER.length() + 2);
			String rootpath = "";
			if (containerPath.indexOf("/") > -1) {
				containerPath = containerPath.substring(0, containerPath.lastIndexOf("/"));
				String[] dotns = containerPath.split("/");
				for (int i = 0; i < dotns.length; i++) {
					rootpath += "../";
				}
			}
			fielName = fielName.replace("/" + PHANTOM_PROJECT_NAME + "/", rootpath);
		}
		return fielName;
	}
}
