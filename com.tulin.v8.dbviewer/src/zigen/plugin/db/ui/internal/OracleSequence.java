/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.ext.oracle.internal.OracleSequenceInfo;

public class OracleSequence extends TreeNode {

	private static final long serialVersionUID = 1L;

	private OracleSequenceInfo oracleSequenceInfo;

	public OracleSequence(String name) {
		super(name);
	}

	public OracleSequence() {
		super();
	}

	public String getName() {
		if (oracleSequenceInfo != null) {
			return this.oracleSequenceInfo.getSequence_name();
		} else {
			return super.name;
		}
	}

	public OracleSequenceInfo getOracleSequenceInfo() {
		return oracleSequenceInfo;
	}

	public void setOracleSequenceInfo(OracleSequenceInfo oracleSequenceInfo) {
		this.oracleSequenceInfo = oracleSequenceInfo;
	}

}
