/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.io.Serializable;

public class OracleSourceErrorInfo extends OracleSourceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int line;

	private int position;

	private String text;

	public OracleSourceErrorInfo() {}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getErrorText() {
		if (text == null) {
			return null;
		} else {
			StringBuffer sb = new StringBuffer();
			// sb.append(Messages.getString("OracleSourceErrorInfo.0") + (line + 1)); //$NON-NLS-1$
			sb.append(Messages.getString("OracleSourceErrorInfo.0") + (line)); //$NON-NLS-1$
			sb.append(Messages.getString("OracleSourceErrorInfo.1") + position); //$NON-NLS-1$
			sb.append(", "); //$NON-NLS-1$
			sb.append(text.replaceAll("\n", "")); //$NON-NLS-1$ //$NON-NLS-2$
			return sb.toString();
		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[OracleSourceErrorInfo:"); //$NON-NLS-1$
		buffer.append(" line: "); //$NON-NLS-1$
		buffer.append(line);
		buffer.append(" position: "); //$NON-NLS-1$
		buffer.append(position);
		buffer.append(" text: "); //$NON-NLS-1$
		buffer.append(text);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

}
