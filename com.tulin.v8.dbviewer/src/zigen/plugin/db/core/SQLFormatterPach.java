/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

public class SQLFormatterPach {

	public static final String LINE_SEP = "\n"; // BlancoFormattert the line feed code is treated with \n.

	public static String format(String sql) {
		sql = formatOpenParen(sql);
		sql = formatCase(sql);
		sql = formatCoron(sql);
		return sql;
	}

	private static final int SCOPE_NONE = 0;

	private static final int SCOPE_BETWEEN = 100;

	private static String formatOpenParen(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer t3 = new StringTokenizer(sql, " ");
		String token3 = null;
		int indent = 0;
		int preIndent = 0;
		String preWord = "";

		int scope = SCOPE_NONE;
		boolean setBetweenAnd = false;

		while ((token3 = t3.nextToken()) != null) {
			if (token3.trim().length() == 0) {
				indent++;
			} else {
				if (!sb.toString().endsWith(LINE_SEP) && token3.startsWith("(")) {
					if ("=".equalsIgnoreCase(preWord) || "IN".equalsIgnoreCase(preWord) || "THEN".equalsIgnoreCase(preWord)) {
						sb.append(" ");
						sb.append(token3);

					} else if ("AND".equalsIgnoreCase(token3) || "OR".equalsIgnoreCase(preWord)) {
						sb.append(LINE_SEP);
						sb.append(" ");
						sb.append(StringUtil.indent(token3, preIndent));

					} else {
						sb.append(token3);
					}

				} else {
					if (sb.length() == 0) {
						sb.append(StringUtil.indent(token3, indent));

					} else if ("BETWEEN".equalsIgnoreCase(token3)) {
						scope = SCOPE_BETWEEN;
						sb.append(" ");
						sb.append(StringUtil.indent(token3, indent));
						setBetweenAnd = false;

					} else if ("AND".equalsIgnoreCase(token3)) {

						if (scope == SCOPE_BETWEEN) {
							if (!setBetweenAnd) {
								sb = new StringBuffer(sb.toString().trim());
								sb.append(" ");
								sb.append(token3);
								setBetweenAnd = true;
							} else {
								sb.append(" ");
								sb.append(StringUtil.indent(token3, indent));
							}

						} else {
							sb.append(" ");
							sb.append(StringUtil.indent(token3, indent));
						}
					} else {
						if (!sb.toString().endsWith(".")) {
							sb.append(" ");
						}
						sb.append(StringUtil.indent(token3, indent));

					}

					preIndent = indent;
					indent = 0;
				}

				preWord = token3;

			}
		}

		return sb.toString();
	}

	private static String formatCase(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer t3 = new StringTokenizer(sql, " ");
		String token3 = null;
		int indent = 0;
		int preIndent = 0;

		boolean isCase = false;
		while ((token3 = t3.nextToken()) != null) {
			if (token3.trim().length() == 0) {
				indent++;
			} else {
				String wk = token3.trim().toUpperCase();
				if (wk.endsWith("CASE")) {
					isCase = true;
					sb.append(" ");
					sb.append(StringUtil.indent(token3, indent));

					preIndent = indent;
					indent = 0;

				} else if ("WHEN".equalsIgnoreCase(token3)) {
					if (isCase) {
						sb.append(" ");
						sb.append("    ");
						sb.append(StringUtil.indent(token3, preIndent));
					} else {
						sb.append(LINE_SEP);
						sb.append(" ");
						sb.append(StringUtil.indent(token3, preIndent));
					}
					isCase = false;

				} else if ("THEN".equalsIgnoreCase(token3)) {
					sb = new StringBuffer(sb.toString().trim());
					sb.append(" ");
					sb.append(token3);

				} else {

					if (sb.length() == 0) {
						sb.append(token3);

					} else if (sb.toString().toUpperCase().endsWith("WHEN") || sb.toString().toUpperCase().endsWith("THEN")) {
						sb.append(" ");
						sb.append(token3);
					} else {
						sb.append(" ");
						sb.append(StringUtil.indent(token3, indent));
						isCase = false;
					}

					preIndent = indent;
					indent = 0;
				}

			}
		}

		return sb.toString();
	}


	// for BIND(:)
	private static String formatCoron(String sql) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer t3 = new StringTokenizer(sql, " ");
		String token3 = null;
		int indent = 0;
		int preIndent = 0;

		boolean isCoron = false;
		while ((token3 = t3.nextToken()) != null) {
			if (token3.trim().length() == 0) {
				indent++;
			} else {
				String wk = token3.trim();
				if (wk.endsWith(":")) {
					isCoron = true;
					sb.append(" ");
					sb.append(StringUtil.indent(token3, indent));

					preIndent = indent;
					indent = 0;

				} else {
					if (sb.length() == 0) {
						sb.append(token3);
					} else if (isCoron) {
						sb.append(StringUtil.indent(token3, indent));
						isCoron = false;
					} else {
						sb.append(" ");
						sb.append(StringUtil.indent(token3, indent));
					}
					preIndent = indent;
					indent = 0;
				}

			}
		}

		return sb.toString();
	}
}
