/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;

public class OracleSource extends TreeNode {

	private static final long serialVersionUID = 1L;

	private OracleSourceInfo info;

	boolean hasError;

	public OracleSource(String name) {
		super(name);
	}

	public OracleSource() {
		super();
	}

	public OracleSourceInfo getOracleSourceInfo() {
		return info;
	}

	public void setOracleSourceInfo(OracleSourceInfo info) {
		this.info = info;
	}

	public String getName() {
		if (info != null) {
			return this.info.getName();
		} else {
			return super.name;
		}
	}

	public String getType() {
		if (info != null) {
			return this.info.getType();
		} else {
			return "";
		}
	}


	public OracleSourceInfo getInfo() {
		return info;
	}

	public void setInfo(OracleSourceInfo info) {
		this.info = info;
	}


	public boolean hasError() {
		return hasError;
	}


	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public void update(OracleSource node) {
		this.info = node.info;
		this.hasError = node.hasError;
	}
}
