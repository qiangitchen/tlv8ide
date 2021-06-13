/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLUtil;

public class Schema extends TreeNode {

	private static final long serialVersionUID = 1L;

	private String[] sourceTypes;

	public Schema() {
		super();
	}

	public Schema(String name) {
		super(name);
	}

	public void update(Schema node) {
		this.sourceTypes = node.sourceTypes;
	}

	private Table[] convertTables(TreeLeaf[] leafs) {
		List list = new ArrayList(leafs.length);
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Table) {
				list.add((Table) leafs[i]);
			}
		}
		return (Table[]) list.toArray(new Table[0]);
	}

	public Table[] getTables() {
		TreeLeaf[] leafs = getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			if (leafs[i] instanceof Folder) {
				Folder folder = (Folder) leafs[i];
				if ("TABLE".equals(folder.getName())) {
					return convertTables(folder.getChildrens());
				}
			}

		}
		return null;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}

		Schema castedObj = (Schema) o;
		IDBConfig config = castedObj.getDbConfig();

		if (config == null) {
			return false;
		}

		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig())) {
			return true;
		} else {
			return false;
		}


	}

	public Object clone() {
		Schema inst = new Schema();
		inst.name = this.name == null ? null : new String(this.name);
		return inst;
	}

	public String[] getSourceType() {
		return sourceTypes;
	}

	public void setSourceType(String[] sourceTypes) {
		this.sourceTypes = sourceTypes;
	}

	public String getLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		return sb.toString();
	}

	public String getEscapedName() {
		return SQLUtil.enclose(name, getDataBase().getEncloseChar());
	}

}
