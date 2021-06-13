/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLHistory;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.internal.HistoryDataBaseFolder;
import zigen.plugin.db.ui.internal.HistoryFolder;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;

public class HistoryContentProvider implements ITreeContentProvider {

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$

	private SQLHistoryManager historyMgr = DbPlugin.getDefault().getHistoryManager();

	private Root invisibleRoot;

	// private Root root;

	private TreeViewer viewer;

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) v;
	}

	public void dispose() {}

	public Object[] getElements(Object inputElement) {
		if (invisibleRoot == null)
			initialize();

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
		invisibleRoot = new Root("invisible", true); //$NON-NLS-1$
		addElement();

	}

	public void addElement() {

		TimeWatcher tw = new TimeWatcher();
		tw.start();

		List historys = historyMgr.getHistory();

		// TreeLeaf last = null;

		for (Iterator iter = historys.iterator(); iter.hasNext();) {
			SQLHistory element = (SQLHistory) iter.next();

			HistoryFolder folder = new HistoryFolder(element.getDate());
			HistoryDataBaseFolder dbFolder = new HistoryDataBaseFolder(element.getConfig());

			History history = new History(element);

			String sql = history.getName();

			if (!"".equals(sql)) { //$NON-NLS-1$
				TreeNode node = (TreeNode) invisibleRoot.getChild(folder.getName());

				if (node == null) {
					invisibleRoot.addChild(folder);
					node = folder;
					// last = folder;
				}


				if (node instanceof HistoryFolder) {
					HistoryFolder f = (HistoryFolder) node;
					f.addChild(history);
					// last = history;
				}
			}

		}
		tw.stop();
	}

	public HistoryFolder getHistoryHolder(Date date) {
		if (invisibleRoot == null)
			return null;

		HistoryFolder wk = new HistoryFolder(date);
		TreeLeaf node = invisibleRoot.getChild(wk.getName());
		if (node instanceof HistoryFolder) {
			return (HistoryFolder) node;
		} else {
			return null;
		}
	}

	public void reflesh(SQLHistory newHistory) {

		List sqlHistorys = historyMgr.getHistory();

		List folders = invisibleRoot.getChildren();

		for (int i = 0; i < folders.size(); i++) {
			HistoryFolder folder = (HistoryFolder) folders.get(i);

			List historys = folder.getChildren();

			for (int k = 0; k < historys.size(); k++) {
				History history = (History) historys.get(k);
				if (sqlHistorys.contains(history.getSqlHistory())) {

				} else {
					folder.removeChild(history);
					if (folder.getChildren().size() == 0) {
						invisibleRoot.removeChild(folder);
						folders.remove(folders);
						i--;
					}
				}
			}
		}
		History h = new History(newHistory);
		HistoryFolder current = getHistoryHolder(newHistory.getDate());
		if (current != null) {
			current.addChild(h);
		} else {
			HistoryFolder folder = new HistoryFolder(newHistory.getDate());
			invisibleRoot.addChild(folder);
			folder.addChild(h);
		}

		viewer.refresh();

	}

	public Root getInvisibleRoot() {
		return invisibleRoot;
	}
}
