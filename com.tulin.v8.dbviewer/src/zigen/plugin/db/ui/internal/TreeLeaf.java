/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import java.io.Serializable;

import zigen.plugin.db.core.IDBConfig;

@SuppressWarnings({ "rawtypes" })
public class TreeLeaf implements INode, Serializable {

	private static final long serialVersionUID = 1L;

	protected int level = 0;

	protected String name;

	protected TreeNode parent;

	boolean isEnabled = true;
	
	private String tvtype;
	private String biz;
	private String path;
	private String dbkey;
	private String username;

	public TreeLeaf() {
		this(null);
	}

	public TreeLeaf(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean b) {
		this.isEnabled = b;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public TreeNode getParent() {
		return parent;
	}

	public String toString() {
		return getName();
	}

	public IDBConfig getDbConfig() {
		return getDbConfig(this);
	}

	public DataBase getDataBase() {
		return getDataBase(this);
	}

	public Schema getSchema() {
		return getSchema(this);
	}

	public Folder getFolder() {
		return getFolder(this);
	}

	public ITable getTable() {
		return getTable(this);
	}

	private DataBase getDataBase(TreeLeaf leaf) {
		if (leaf instanceof DataBase) {
			return (DataBase) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getDataBase(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	private Schema getSchema(TreeLeaf leaf) {
		if (leaf instanceof Schema) {
			return (Schema) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getSchema(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	private Folder getFolder(TreeLeaf leaf) {
		if (leaf instanceof Folder) {
			return (Folder) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getFolder(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	private ITable getTable(TreeLeaf leaf) {
		if (leaf instanceof ITable) {
			return (ITable) leaf;
		} else {
			if (leaf.getParent() != null) {
				return getTable(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	protected IDBConfig getDbConfig(TreeLeaf leaf) {

		if (leaf instanceof DataBase) {
			return ((DataBase) leaf).getDbConfig();
		} else {

			if (leaf.getParent() != null) {
				return getDbConfig(leaf.getParent());
			} else {
				return null;
			}
		}
	}

	public Object getAdapter(Class key) {
		return null;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getBiz() {
		return this.biz;
	}

	public String getPath() {
		return this.path;
	}

	public String getDbkey() {
		return this.dbkey;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setDbkey(String dbkey) {
		this.dbkey = dbkey;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public String getTvtype() {
		return tvtype;
	}

	public void setTvtype(String tvtype) {
		this.tvtype = tvtype;
	}

}
