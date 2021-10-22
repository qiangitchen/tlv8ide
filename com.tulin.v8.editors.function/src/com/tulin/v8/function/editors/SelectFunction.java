package com.tulin.v8.function.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.utils.CommonUtil;

public class SelectFunction {
	private static String PHANTOM_PROJECT_NAME;
	private static String PROJECT_WEB_FOLDER;

	public static List<String> funcpage = new ArrayList<String>();

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

	public static List<String> loadFunctionPage() {
		loadFile(getPagePath());
		return funcpage;
	}

	public static void loadFile(String dirPath) {
		String pds = CommonUtil.getPathdep();
		if (!dirPath.endsWith(".svn") && !dirPath.endsWith("WEB-INF") && !dirPath.endsWith("META-INF")) {
			File file = new File(dirPath);
			File[] subFiles = file.listFiles();
			for (File subFile : subFiles) {
				if (subFile.isFile()) {
					String filename = subFile.getName().toLowerCase();
					if (isIgnorePage(filename)) {
						continue;
					}
					if (filename.contains(".jsp") || filename.contains(".htm")) {
						String f = dirPath + pds + subFile.getName();
						f = f.substring(f.indexOf(pds + PHANTOM_PROJECT_NAME + pds));
						f = CommonUtil.rebuildFilePath(f);
						f = f.replace("/" + PROJECT_WEB_FOLDER + "/", "/");
						funcpage.add(f);
					}
				} else {
					loadFile(subFile.getAbsolutePath());
				}
			}
		}
	}

	public static void loadFold(TreeItem treeitem, String dirPath) {
		String pds = CommonUtil.getPathdep();
		if (!dirPath.endsWith(".svn") && !dirPath.endsWith("WEB-INF") && !dirPath.endsWith("META-INF")) {
			File file = new File(dirPath);
			File[] subFiles = file.listFiles();
			for (File subFile : subFiles) {
				if (subFile.isFile()) {
					String filename = subFile.getName().toLowerCase();
					if (isIgnorePage(filename)) {
						continue;
					}
					if (filename.contains(".jsp") || filename.contains(".htm")) {
						String f = dirPath + pds + subFile.getName();
						f = CommonUtil.rebuildFilePath(f);
						f = f.substring(
								f.indexOf("/" + PHANTOM_PROJECT_NAME + "/") + PHANTOM_PROJECT_NAME.length() + 1);
						f = f.replace("/" + PROJECT_WEB_FOLDER + "/", "/");
						TreeItem nitem = new TreeItem(treeitem, SWT.NONE);
						nitem.setText(f);
						if (f.endsWith(".jsp")) {
							nitem.setImage(TuLinPlugin.getIcon("jsp.gif"));
						} else {
							nitem.setImage(TuLinPlugin.getIcon("html.gif"));
						}
					}
				} else {
					String f = subFile.getAbsolutePath();
					f = CommonUtil.rebuildFilePath(f);
					f = f.substring(f.indexOf("/" + PHANTOM_PROJECT_NAME + "/") + PHANTOM_PROJECT_NAME.length() + 1);
					f = f.replace("/" + PROJECT_WEB_FOLDER + "/", "/");
					if (!f.endsWith(".svn") && !f.endsWith("WEB-INF") && !f.endsWith("META-INF")
							&& isPageFolder(subFile)) {
						TreeItem nitem = new TreeItem(treeitem, SWT.NONE);
						nitem.setText(f);
						nitem.setImage(TuLinPlugin.getIcon("folder-open.gif"));
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
				if (subFile.getName().toLowerCase().contains(".jsp")
						|| subFile.getName().toLowerCase().contains(".htm"))
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
		nitem.setImage(TuLinPlugin.getIcon("folder-open.gif"));
		loadFold(nitem, getPagePath());
	}

	public static boolean isIgnorePage(String filename) {
		if (filename.equals("404.jsp") || filename.equals("error.jsp") || filename.equals("alertlogin.jsp")
				|| filename.equals("sessionauthor.jsp") || filename.equals("sessionerr.jsp")) {
			return true;
		}
		return false;
	}
}
