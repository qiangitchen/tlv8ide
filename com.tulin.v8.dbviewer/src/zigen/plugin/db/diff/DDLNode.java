/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.io.Serializable;
import java.util.ArrayList;

import zigen.plugin.db.ui.internal.TreeNode;


abstract public class DDLNode extends TreeNode implements IDDLDiff, Serializable {

	private static final long serialVersionUID = 1L;

	public DDLNode() {
		this(null, false);
	}

	public DDLNode(String name) {
		this(name, false);
	}

	public DDLNode(String name, boolean isRoot) {
		this.name = name;
		this.isRoot = isRoot;
		this.children = new ArrayList();
	}

}
