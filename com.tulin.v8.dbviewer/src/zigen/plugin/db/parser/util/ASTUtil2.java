/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.parser.util;

import java.util.ArrayList;
import java.util.List;

import zigen.sql.parser.INode;

public class ASTUtil2 {

	public static INode findParent(INode node, String type) {
		if (node == null || type.equals(node.getNodeClassName())) {
			return node;
		} else {
			return findParent((INode) node.getParent(), type);
		}

	}

	public static INode findFirstChild(INode node, String type) {
		INode[] nodes = findChildren(node, type);
		if (nodes != null && nodes.length > 0) {
			return nodes[0];
		} else {
			return null;
		}
	}

	public static INode[] findChildren(INode node, String type) {
		List list = new ArrayList();
		if (node != null) {
			for (int i = 0; i < node.getChildrenSize(); ++i) {
				INode n = (INode) node.getChild(i);
				if (n.getNodeClassName().equals(type)) {
					list.add(n);
				} else {
					INode[] nn = findChildren(n, type);
					if (nn != null) {
						for (int j = 0; j < nn.length; j++) {
							list.add(nn[j]);
						}
					}
				}
			}
		}
		return (INode[]) list.toArray(new INode[0]);
	}

}
