/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.io.Serializable;

public class OracleSourceDetailInfo extends OracleSourceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String text;

	public OracleSourceDetailInfo() {}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[OracleSourceDetailInfo:"); //$NON-NLS-1$
		buffer.append(" text: "); //$NON-NLS-1$
		buffer.append(text);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

}
