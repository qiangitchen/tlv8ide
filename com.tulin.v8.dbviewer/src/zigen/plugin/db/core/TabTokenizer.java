/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class TabTokenizer implements Enumeration {

	public static final String DEMILITER = "\t"; //$NON-NLS-1$

	public static final char QUOTE = '"';

	private String text;

	private int currentPosition;

	private int maxPosition;

	public TabTokenizer(String str) {
		this.text = str;
		currentPosition = 0;
		maxPosition = this.text.length();
	}

	private int nextDemiliter(int i) {
		boolean flg = false;

		while (i < maxPosition) {
			char ch = text.charAt(i);
			int pos = -1;

			pos = text.indexOf(DEMILITER, i);
			if (!flg && pos == i) {
				break;
			} else if (QUOTE == ch) {
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
		if (currentPosition > maxPosition)
			throw new NoSuchElementException(toString());

		int start = currentPosition;
		currentPosition = nextDemiliter(currentPosition);
		StringBuffer sb = new StringBuffer();
		while (start < currentPosition) {
			char ch = text.charAt(start++);
			sb.append(ch);
		}

		currentPosition += (DEMILITER).length();

		// <-- 2008/01/30 no trime.
		// String out = sb.toString().trim();
		String out = sb.toString();
		// -->

		if (out.length() == 0) {
			return null;
		} else {
			return out;
		}
	}

	public Object nextElement() {
		return nextToken();
	}

	public boolean hasMoreElements() {
		return (nextDemiliter(currentPosition) <= maxPosition);
	}

}
