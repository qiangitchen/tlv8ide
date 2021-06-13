/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import zigen.plugin.db.DbPlugin;

public class CSVResultWriter {

	private char demiliter = ',';

	private boolean append = false;

	private String encording = Messages.getString("CSVResultWriter.0"); //$NON-NLS-1$

	// private List csvList = null;
	private String[] headers = null;

	public CSVResultWriter() {}

	public CSVResultWriter(char demiliter) {
		this.demiliter = demiliter;
	}

	public CSVResultWriter(char demiliter, boolean append) {
		this.demiliter = demiliter;
		this.append = append;
	}

	public CSVResultWriter(char demiliter, boolean append, String encording) {
		this.demiliter = demiliter;
		this.append = append;
		this.encording = encording;
	}

	public void execute(File csvFile, List csvList) throws Exception {
		PrintStream pout = null;

		try {
			pout = new PrintStream(new FileOutputStream(csvFile, append), true, // flashMode
					encording); // encording

			if (!append) {
				writeHeader(pout);
			}

			writeValue(pout, csvList);

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			if (pout != null)
				pout.close();
		}

	}

	private void writeHeader(PrintStream pout) throws SQLException {

		for (int i = 0; i < headers.length; i++) {
			String header = encode(headers[i]);
			if (i == headers.length - 1) {
				pout.println(header);
			} else {
				pout.print(header + demiliter);
			}

		}
	}

	private void writeValue(PrintStream pout, List csvList) throws SQLException {

		for (Iterator iter = csvList.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof List) {
				List list = (List) obj;

				for (int i = 0; i < list.size(); i++) {
					String value = encode(list.get(i).toString());
					if (i == list.size() - 1) {
						pout.println(value);
					} else {
						pout.print(value + demiliter);
					}

				}
			} else {
				throw new IllegalArgumentException("Contents of csvList should be List types."); //$NON-NLS-1$
			}

		}

	}

	private String encode(String value) {
		value = value.replaceAll("\"", "\"\""); //$NON-NLS-1$ //$NON-NLS-2$
		if (value.indexOf("\"") > 0 || value.indexOf(",") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			value = "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return value;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public char getDemiliter() {
		return demiliter;
	}

	public void setDemiliter(char demiliter) {
		this.demiliter = demiliter;
	}

	public String getEncording() {
		return encording;
	}

	public void setEncording(String encording) {
		this.encording = encording;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
}
