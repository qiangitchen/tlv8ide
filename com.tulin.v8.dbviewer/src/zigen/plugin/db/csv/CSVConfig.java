/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.csv;

import java.io.Serializable;


public class CSVConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String query;

	private String csvFile;

	private String csvEncoding;

	private String separator;

	private boolean nonHeader;

	private boolean nonDoubleQuate;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public boolean isNonHeader() {
		return nonHeader;
	}

	public void setNonHeader(boolean nonHeader) {
		this.nonHeader = nonHeader;
	}

	public boolean isNonDoubleQuate() {
		return nonDoubleQuate;
	}

	public void setNonDoubleQuate(boolean nonDoubleQuate) {
		this.nonDoubleQuate = nonDoubleQuate;
	}

	public String getCsvEncoding() {
		return csvEncoding;
	}

	public void setCsvEncoding(String csvEncoding) {
		this.csvEncoding = csvEncoding;
	}

	public String getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[CSVConfig:"); //$NON-NLS-1$
		buffer.append(" query: "); //$NON-NLS-1$
		buffer.append(query);
		buffer.append(" csvFile: "); //$NON-NLS-1$
		buffer.append(csvFile);
		buffer.append(" csvEncoding: "); //$NON-NLS-1$
		buffer.append(csvEncoding);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}
}
