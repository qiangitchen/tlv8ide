package com.tulin.v8.ide.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.tulin.v8.ide.views.navigator.job.LoadJob;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TreeContentProvider implements ITreeContentProvider,
		IPropertyChangeListener {
	private TreeViewer fViewer;
	private Root invisibleRoot;
	private IDBConfig[] dbConfigs;

	public TreeContentProvider(TreeViewer Viewer) {
		fViewer = Viewer;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		if (invisibleRoot == null) {
			initialize();
		}
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

	public void initialize() {
		invisibleRoot = new Root("invisible", true);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		createDataBase();
		Display.getDefault().syncExec(new LoadJob(fViewer, invisibleRoot));
	}
	
	public void createDataBase() {
		dbConfigs = DBConfigManager.getDBConfigs();
	}
	
	public DataBase addDataBase(IDBConfig config) {
		DataBase db = new DataBase(config);
		//invisibleRoot.addChild(db);
		return db;
	}

	public Root getRoot() {
		return invisibleRoot;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if ((this.fViewer != null) && (!this.fViewer.getControl().isDisposed())) {
			this.fViewer.getControl().setRedraw(false);
			Object[] expandedObjects = this.fViewer.getExpandedElements();
			this.fViewer.refresh();
			this.fViewer.setExpandedElements(expandedObjects);
			this.fViewer.getControl().setRedraw(true);
		}
	}

	public void runPendingUpdates() {
		// TODO Auto-generated method stub

	}
	
	public DataBase[] getDataBases() {
		TreeLeaf[] leafs = invisibleRoot.getChildrens();
		List list = new ArrayList();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof DataBase) {
				list.add(leaf);
			}
		}
		return (DataBase[]) list.toArray(new DataBase[0]);
	}
	
	public DataBase findDataBase(IDBConfig config) {
		TreeLeaf[] leafs = invisibleRoot.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (leaf instanceof DataBase) {
				DataBase db = (DataBase) leaf;
				if (db.getName().equals(config.getDbName())) {
					return db;
				}
			}
		}
		return null;
	}
	
	public IDBConfig[] getDBConfigs() {
		return dbConfigs;
	}

}
