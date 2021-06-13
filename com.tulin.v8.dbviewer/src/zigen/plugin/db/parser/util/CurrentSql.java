/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.parser.util;

import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.StringUtil;

public class CurrentSql {

	private String sql;

	private String offsetSql;

	private int begin;

	private int offset;

	private String demiliter;

	public CurrentSql(IDocument doc, int offset, String demiliter) {
		this.offset = offset;
		if ("/".equals(demiliter)) {
			this.demiliter = DbPluginConstant.LINE_SEP + demiliter + DbPluginConstant.LINE_SEP;
		} else {
			this.demiliter = demiliter + DbPluginConstant.LINE_SEP;
		}
		parse(doc);
	}

	public String getSql() {
		return sql;
	}

	public String getOffsetSql() {
		return (offsetSql == null) ? "" : offsetSql;
	}

	private void parse(IDocument doc) {
		try {
			String text = doc.get();

			if (text == null || "".equals(text) || offset < 0)
				return;

			String cText = text.substring(0, offset);
			String nText = StringUtil.convertLineSep(cText, DbPluginConstant.LINE_SEP);
			int x = nText.length() - cText.length();
			if (x != 0) {
				offset += x;
			}

			text = StringUtil.convertLineSep(doc.get(), DbPluginConstant.LINE_SEP);

			int len = text.length();
			int startSlash = findStartSlash(text);
			int endSlash = findEndSlash(text);

			startSlash = startSlash == -1 ? 0 : startSlash;
			endSlash = endSlash == -1 ? len : endSlash;

			int p = 0;
			while (isIntoQuot(text, startSlash)) {
				p = startSlash - 1;
				p = text.lastIndexOf(demiliter, p);
				startSlash = p == -1 ? 0 : p;
			}

			while (isIntoQuot(text, endSlash)) {
				p = endSlash + 1;
				p = text.indexOf(demiliter, p);
				endSlash = p == -1 ? len : p;
			}

			this.sql = trimSeparator(text.substring(startSlash, endSlash), startSlash);

			this.offsetSql = trimSeparator(text.substring(startSlash, offset), startSlash);

		} catch (Exception e) {
			DbPlugin.log(e);
		}

	}

	private int findStartSlash(String str) {
		int i = str.lastIndexOf(demiliter, offset - demiliter.length());
		if (i > 1) {
			if (str.startsWith("*/", i - 1)) {
				i = str.lastIndexOf(demiliter, i - 1);
			}
		}
		return i;

	}

	private int findEndSlash(String str) {
		int i = str.indexOf(demiliter, offset - demiliter.length() + 1);
		if (i > 1) {
			if (str.startsWith("*/", i - 1)) {
				i = str.indexOf(demiliter, i + 1);
			}
		}
		return i;
	}

	private String trimSeparator(String executeSql, int startSlash) {
		int start = 0;
		int end = executeSql.length();
		int sepLen = demiliter.length();

		if (executeSql.startsWith(demiliter)) {
			start += sepLen;
		}
		this.begin = startSlash + start;

		return executeSql.substring(start, end);
	}

	private static boolean isIntoQuot(String str, int pos) {
		int len = str.length();

		boolean intoQuotFlag = false;
		int startQuot = Integer.MAX_VALUE;
		int endQuot = -1;

		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);

			if (c == '\'') {
				if (intoQuotFlag && (i + 1) < len && '\'' == str.charAt(i + 1)) {
					continue;

				} else {
					intoQuotFlag ^= true;
					if (intoQuotFlag) {
						startQuot = i;
					} else {
						endQuot = i;
					}
				}
			}

			if (!intoQuotFlag && startQuot <= pos && pos < endQuot)
				return true;
		}

		return false;
	}

	public int getBegin() {
		return begin;
	}


	public int getEnd() {
		return begin + getLength();
	}

	public int getLength() {
		return (sql != null) ? sql.length() : 0;
	}
}
