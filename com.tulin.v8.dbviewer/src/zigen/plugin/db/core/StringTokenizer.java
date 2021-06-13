/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.Enumeration;

public class StringTokenizer implements Enumeration {

	private String demiliter = null;

	private String text;

	private int currentPosition;

	private int maxPosition;

	public StringTokenizer(String str) {
		this.demiliter = "\n";
		this.text = str;
		currentPosition = 0;
		maxPosition = this.text.length();
	}

	public StringTokenizer(String str, String demiliter) {
		this.demiliter = demiliter;
		this.text = str;
		currentPosition = 0;
		maxPosition = this.text.length();
	}

	private int nextDemiliter(int i) {
		boolean flg = false;

		while (i < maxPosition) {
			char ch = text.charAt(i);

			int pos = text.indexOf(demiliter, i);
			if (!flg && pos == i) {
				break;
			} else if ('\'' == ch) {
				flg = !flg;
			} else if ('"' == ch) {
				flg = !flg;
			}
			i++;
		}
		return i;
	}

	public int getTokenCount() {
		int i = 0;
		int ret = 1;
		while ((i = nextDemiliter(i)) < maxPosition) {
			i++;
			ret++;
		}
		return ret;
	}

	public String nextToken() {
		if (currentPosition > maxPosition) {
			return null;
		}
		int start = currentPosition;
		currentPosition = nextDemiliter(currentPosition);

		StringBuffer sb = new StringBuffer();
		while (start < currentPosition) {
			char ch = text.charAt(start++);
			sb.append(ch);
		}
		currentPosition++;

		return sb.toString();
	}

	public Object nextElement() {
		return nextToken();
	}

	public boolean hasMoreElements() {
		return (nextDemiliter(currentPosition) <= maxPosition);
	}

}
