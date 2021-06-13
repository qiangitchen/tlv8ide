/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.StringTokenizer;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.preference.SQLTemplatesPreferencePage;

public class SQLTemplateFormatter {

	IDocument fDocument;

	int originalOffset;

	IPreferenceStore ps;

	public SQLTemplateFormatter(IDocument doc, int originalOffset) {
		this.fDocument = doc;
		this.originalOffset = originalOffset;
		ps = DbPlugin.getDefault().getPreferenceStore();
	}

	public String format(String templatePattern) {
		try {
			int line = fDocument.getLineOfOffset(originalOffset);
			int lineOffset = fDocument.getLineOffset(line);
			int formattOffset = originalOffset - lineOffset;

			boolean onPatch = ps.getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
			int type = ps.getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);
			boolean useCodeFormatter = ps.getBoolean(SQLTemplatesPreferencePage.TEMPLATES_USE_CODEFORMATTER);

			if (useCodeFormatter) {
				templatePattern = SQLFormatter.format(templatePattern, type, onPatch, formattOffset);
				templatePattern = reConvert(templatePattern);
			}

			return templatePattern;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return templatePattern;
	}

	String reConvert(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(sql, " ");
		String token = null;
		int indent = 0;
		int preIndent = 0;

		boolean isDoru = false;
		boolean isStart = false;
		boolean isEnd = false;
		while ((token = tokenizer.nextToken()) != null) {
			if (token.trim().length() == 0) {
				indent++;
			} else {
				String wk = token.trim();
				if (wk.startsWith("$")) {
					isDoru = true;
					sb.append(" ");
					sb.append(StringUtil.indent(token, indent));

					preIndent = indent;
					indent = 0;

				} else if (wk.startsWith("{")) {
					isStart = true;
					sb.append(StringUtil.indent(token, indent));
					preIndent = indent;
					indent = 0;

				} else if (wk.startsWith("}")) {
					isEnd = true;
					sb.append(StringUtil.indent(token, indent));
					preIndent = indent;
					indent = 0;
				} else {
					if (sb.length() == 0) {
						sb.append(token);
					} else if (isDoru || isStart) {
						sb.append(StringUtil.indent(token, indent));
						isDoru = false;
						isStart = false;

					} else if (isEnd) {
						sb.append(" ");
						sb.append(StringUtil.indent(token, indent));
						isEnd = false;

					} else {
						sb.append(" ");
						sb.append(StringUtil.indent(token, indent));
					}
					preIndent = indent;
					indent = 0;
				}

			}
		}

		return sb.toString();
	}
}
