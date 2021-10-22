/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import kry.sql.format.ISqlFormat;
import kry.sql.format.SqlFormat;
import kry.sql.format.SqlFormatException;
import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import blanco.commons.sql.format.BlancoSqlFormatter;
import blanco.commons.sql.format.BlancoSqlFormatterException;
import blanco.commons.sql.format.BlancoSqlRule;

public class SQLFormatter {


	public static String format(String preSql, int formatterType, boolean onPatch, int offset) {

		if (formatterType == SQLFormatPreferencePage.TYPE_DBVIEWER) {
			/**
			 * for DBViewer SQL Formatter.
			 */
			try {
				ISqlFormat formatter = new SqlFormat(DbPlugin.getSqlFormatRult());

				return formatter.format(preSql, offset);
			} catch (SqlFormatException e) {
				DbPlugin.getDefault().log(e);
			}
			return preSql;
		} else {
			/**
			 * for blancoCommons Formatter.
			 */
			try {
				BlancoSqlFormatter formatter = new BlancoSqlFormatter(new BlancoSqlRule());
				String result = formatter.format(preSql);
				if (onPatch) {
					result = SQLFormatterPach.format(result);
				}

				return StringUtil.convertLineSep(result, DbPluginConstant.LINE_SEP);

			} catch (BlancoSqlFormatterException e) {
				DbPlugin.getDefault().log(e);
			}
			return preSql;

		}

	}

	public static String format(String preSql, int formatterType, boolean onPatch) {
		return format(preSql, formatterType, onPatch, 0);

	}


	public static String unformat(String sql) {
		ISqlFormat formatter = new SqlFormat(DbPlugin.getSqlFormatRult());
		return formatter.unFormat(sql);
	}

	public static String[] splitOrderCause(String whereCause) {
		String[] result = new String[2];
		StringBuffer main = new StringBuffer();
		StringBuffer order = new StringBuffer();

		if (whereCause == null) {
			result[0] = main.toString();
			result[1] = order.toString();
			return result;
		}

		SqlStreamTokenizer tokenizer = new SqlStreamTokenizer(whereCause);
		boolean isOrderCause = false;
		try {
			String s = null;
			for (;;) {
				switch (tokenizer.nextToken()) {
				case SqlStreamTokenizer.TT_EOF:
					result[0] = main.toString();
					result[1] = order.toString();
					return result;

				case SqlStreamTokenizer.TT_COMMA:

					if (isOrderCause) {
						order = new StringBuffer(order.toString().trim());
						order.append(SqlStreamTokenizer.TT_COMMA);
						order.append(SqlStreamTokenizer.TT_SPACE);
					} else {
						main = new StringBuffer(main.toString().trim());
						main.append(SqlStreamTokenizer.TT_COMMA);
						main.append(SqlStreamTokenizer.TT_SPACE);
					}
					break;

				default:
					s = tokenizer.getToken();
					if (isOrderCause || "order".equalsIgnoreCase(s)) {
						isOrderCause = true;
						order.append(s);
						// order.append(SqlStreamTokenizer.TT_SPACE);
					} else {
						main.append(s);
						// main.append(SqlStreamTokenizer.TT_SPACE);
					}
					break;
				}

			}

		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return result;
	}

}

class SqlStreamTokenizer extends StreamTokenizer {

	public final static char TT_SPACE = ' ';

	public final static char TT_QUOTE = '\'';

	public final static char TT_DOUBLE_QUOTE = '"';

	public final static char TT_COMMA = ',';

	public final static int TT_WORD = StreamTokenizer.TT_WORD;

	private int tokenType = TT_EOF;

	private String token;

	public SqlStreamTokenizer(String sql) {
		super(new StringReader(sql));
		resetSyntax();
		wordChars('0', '9');
		wordChars('a', 'z');
		wordChars('A', 'Z');
		wordChars('_', '_');
		wordChars('.', '.');

		// whitespaceChars(' ', ' ');
		whitespaceChars('\t', '\t');
		whitespaceChars('\n', '\n');
		whitespaceChars('\r', '\r');

		quoteChar(TT_QUOTE);
		quoteChar(TT_DOUBLE_QUOTE);
		// tokenizer.parseNumbers();
		eolIsSignificant(false);
		slashStarComments(true);
		slashSlashComments(true);
	}

	public int nextToken() {
		try {
			tokenType = super.nextToken();

			switch (tokenType) {
			case SqlStreamTokenizer.TT_EOF:
				tokenType = SqlStreamTokenizer.TT_EOF;
				token = null;
				break;

			case SqlStreamTokenizer.TT_WORD:
				token = sval;
				tokenType = SqlStreamTokenizer.TT_WORD;
				break;
			case SqlStreamTokenizer.TT_QUOTE:
				token = "'" + sval + "'";
				break;
			case SqlStreamTokenizer.TT_DOUBLE_QUOTE:
				token = "\"" + sval + "\"";
				break;

			default:
				token = String.valueOf((char) ttype);
			}

		} catch (IOException e) {
			DbPlugin.log(e);
		}

		return tokenType;
	}

	public int getTokenType() {
		return tokenType;
	}

	public String getToken() {
		return token;
	}

}
