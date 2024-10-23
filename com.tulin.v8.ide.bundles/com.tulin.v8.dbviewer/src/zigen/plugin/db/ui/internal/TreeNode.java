/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TreeNode extends TreeLeaf {

	private static final long serialVersionUID = 1L;

	protected List children;

	protected boolean isRoot;

	protected boolean isExpanded = false;

	public TreeNode() {
		this(null, false);
	}

	public TreeNode(String name) {
		this(name, false);
	}

	public TreeNode(String name, boolean isRoot) {
		super(name);
		children = new ArrayList();
		this.isRoot = isRoot;
	}

	public void addChild(TreeLeaf child) {
		children.add(child);
		child.setParent(this);
		child.setLevel(level + 1);
	}

	public void removeChild(TreeLeaf child) {
		children.remove(child);
		if (child != null) {
			child.setParent(null);
		}
	}

	public void removeChildAll() {
		TreeLeaf[] elements = getChildrens();
		for (int i = 0; i < elements.length; i++) {
			TreeLeaf elem = (TreeLeaf) elements[i];
			removeChild(elem);
		}
	}

	public TreeLeaf[] getChildrens() {
		return (TreeLeaf[]) children.toArray(new TreeLeaf[children.size()]);
	}

	public TreeLeaf getChild(String name) {
		TreeLeaf[] elements = getChildrens();
		for (int i = 0; i < elements.length; i++) {
			TreeLeaf elem = (TreeLeaf) elements[i];
			if (elem.getName().equals(name)) {
				return elem;
			}
		}
		return null;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public void setChildren(List children) {
		this.children = children;

		for (Iterator iter = children.iterator(); iter.hasNext();) {
			TreeNode node = (TreeNode) iter.next();
			node.setParent(this);
			node.setLevel(level + 1);
		}

	}

	public List getChildren() {
		return this.children;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		TreeNode castedObj = (TreeNode) o;
		return ((this.children == null ? castedObj.children == null
				: this.children.equals(castedObj.children)) && (this.isRoot == castedObj.isRoot));
	}

}
