/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

public class DriverContentProvider implements ITreeContentProvider {

	private Root invisibleRoot;

	Table[] tables = null;

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {

		if (newInput != null) {
			invisibleRoot = new Root("invisible", true); //$NON-NLS-1$

			IDBConfig[] dbConfigs = DBConfigManager.getDBConfigs();
			for (int i = 0; i < dbConfigs.length; i++) {
				IDBConfig config = dbConfigs[i];

				DataBase wk_db = new DataBase();
				wk_db.setName(config.getDriverName());

				DataBase node = (DataBase) invisibleRoot.getChild(wk_db.getName());
				if (node == null) {
					invisibleRoot.addChild(wk_db);
					node = wk_db;
				}

				String[] classPaths = config.getClassPaths();
				for (int j = 0; j < classPaths.length; j++) {
					String path = classPaths[j];
					File file = new File(path);
					TreeLeaf classPath = null;

					if (file.exists()) {
						if (file.isDirectory()) {
							classPath = new Folder(path);
						} else {
							classPath = new TreeLeaf(path);
						}
						TreeLeaf wk = node.getChild(classPath.getName());
						if (wk == null) {
							node.addChild(classPath);
						}
					}
				}

			}

		}

	}

	public void dispose() {}

	public Object[] getElements(Object inputElement) {
		return getChildren(invisibleRoot);
	}

	public Object getParent(Object element) {
		if (element instanceof TreeLeaf) {
			return ((TreeLeaf) element).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TreeNode) {
			return ((TreeNode) parentElement).getChildrens();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object element) {
		if (element instanceof TreeNode)
			return ((TreeNode) element).hasChildren();
		return false;
	}

	public Root getInvisibleRoot() {
		return invisibleRoot;
	}
}
