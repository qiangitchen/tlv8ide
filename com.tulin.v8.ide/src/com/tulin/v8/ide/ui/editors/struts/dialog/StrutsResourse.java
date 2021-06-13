package com.tulin.v8.ide.ui.editors.struts.dialog;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.StudioPlugin;

public class StrutsResourse {
	TextEditor editor;

	public StrutsResourse(TextEditor editor) {
		this.editor = editor;
	}

	public String getPagePath() {
		String dps = CommonUtil.getPathdep();
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			IProject project = file.getProject();
			return project.getLocation().toFile().getAbsoluteFile() + dps + "src";
		}
		return StudioPlugin.getBIZPath();
	}

	public void loadFold(TreeItem treeitem, String dirPath) {
		if (!dirPath.endsWith(".svn") && !dirPath.endsWith(".data")) {
			File file = new File(dirPath);
			File[] subFiles = file.listFiles();
			for (File subFile : subFiles) {
				if (subFile.isFile()) {
					if (isStrutsFile(subFile)) {
						String f = dirPath + "/" + subFile.getName();
						f = CommonUtil.rebuildFilePath(f);
						f = transeFile(f);
						TreeItem nitem = new TreeItem(treeitem, SWT.NONE);
						nitem.setText(f);
						nitem.setData(subFile.getAbsolutePath());
						nitem.setImage(StudioPlugin.getIcon("xml.gif"));
					}
				} else {
					String f = subFile.getAbsolutePath();
					f = CommonUtil.rebuildFilePath(f);
					f = transeFile(f);
					if (!f.endsWith(".svn") && isPageFolder(subFile)) {
						TreeItem nitem = new TreeItem(treeitem, SWT.NONE);
						nitem.setText(f);
						nitem.setData(subFile.getAbsolutePath());
						nitem.setImage(StudioPlugin.getIcon("folder.gif"));
						loadFold(nitem, subFile.getAbsolutePath());
					}
				}
			}
		}
	}

	public boolean isPageFolder(File fl) {
		boolean rt = false;
		File[] subFiles = fl.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isFile()) {
				if (isStrutsFile(subFile))
					return true;
			} else {
				rt = isPageFolder(subFile);
			}
		}
		return rt;
	}

	public void loadFileTree(Tree adtree) {
		String sourcepath = getPagePath();
		TreeItem nitem = new TreeItem(adtree, SWT.NONE);
		nitem.setText("src");
		nitem.setData(sourcepath);
		nitem.setImage(StudioPlugin.getIcon("folder.gif"));
		loadFold(nitem, sourcepath);
	}

	public boolean isStrutsFile(File file) {
		if (file.getName().toLowerCase().endsWith(".xml") && !file.getName().toLowerCase().endsWith("struts.xml")
				&& !file.getName().endsWith("applicationContext.xml") && !file.getName().endsWith("-SqlMapConfig.xml")
				&& !file.getName().endsWith("log4j.xml")) {
			return true;
		}
		return false;
	}

	public String transeFile(String fielName) {
		fielName = fielName.substring(fielName.indexOf("/src/") + 5);
		return fielName;
	}
}
