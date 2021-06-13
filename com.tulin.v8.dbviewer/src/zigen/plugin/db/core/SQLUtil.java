/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.PreferencePage;

public class SQLUtil {

	private static Pattern BIN = Pattern.compile("^BIN\\$.*==\\$0$");

	public static String getNullSymbol(){
		if(DbPlugin.getDefault() != null){
			return DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);
		}else{
			return null;
		}
	}
	public static boolean isBinTableForOracle(String str) {
		Matcher matcher = BIN.matcher(str);
		return matcher.matches();
	}

	private static boolean requireEnclose(String str) {
		if (isBinTableForOracle(str) || StringUtil.isNumeric(str) || str.indexOf("-") > 0 || str.indexOf(".") > 0) {			
			return true;
		} else {
			return false;
		}
	}

	public static final String removeEnclosedChar(String str, char encloseChar){
		if(str == null) return str;

		char s = str.charAt(0);
		char e = str.charAt(str.length()-1);
		if(s == encloseChar && e == encloseChar){
			return str.substring(1, str.length()-1);
		}else{
			return str;
		}
	}

	public static final String enclose(String str, char encloseChar){
		if(str == null) return str;

		if(requireEnclose(str)){
			return encloseChar + str + encloseChar;
		}else{
			return str;
		}
	}

	public static final String encodeQuotation(String str) {
		if (str == null)
			return str;
		int nLen = str.length();
		StringBuffer sb = new StringBuffer(nLen * 2);
		for (int i = 0; i < nLen; i++) {
			char c = str.charAt(i);
			if (c == '\'') {
				sb.append("''");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static final String encodeEscape(String str) {
		int nLen = str.length();
		StringBuffer sb = new StringBuffer(nLen * 2);
		for (int i = 0; i < nLen; i++) {
			char c = str.charAt(i);
			switch (c) {
			case '%':
				sb.append("\\%");
				break;
			case '_':
				sb.append("\\_");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	public static boolean isSelect(String sql) throws Exception {
		String s = StringUtil.removeComment(sql);
		s = StringUtil.removeLineComment(s);
		s = StringUtil.removeLeftFullSpace(s);
		s = s.trim();

		if (s.startsWith("(")) {
			s = s.substring(1);
			if (s.endsWith(")")) {
				s = s.substring(0, s.length() - 1);
				return isSelect(s);
			} else {
				throw new IllegalArgumentException("The mistake is found in SQL. There is no ')'.");
			}
		} else {
			s = s.toUpperCase();
			if (s.startsWith("SELECT") || s.startsWith("SHOW") || s.startsWith("DESCRIBE")) {
				return true;
			} else {
				return false;
			}

		}

	}

	public static String toUpperCase(String sql) {
		return toCase(sql, MODE_TO_UPPER);
	}

	public static String toLowerCase(String sql) {
		return toCase(sql, MODE_TO_LOWER);
	}

	private static final int MODE_TO_UPPER = 1;

	private static final int MODE_TO_LOWER = 2;

	private static String toCase(String sql, int mode) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer t = new StringTokenizer(sql, " ");
		String token = null;
		int indent = 0;
		int preIndent = 0;

		while ((token = t.nextToken()) != null) {
			if (token.trim().length() == 0) {
				indent++;
			} else {
				StringBuffer buff = new StringBuffer();
				Tokenizer tokenizer = new Tokenizer(token);
				while (tokenizer.nextToken() != Tokenizer.TT_EOF) {
					String token2 = tokenizer.getToken();
					String temp = token2.trim();
					if (temp.startsWith("'") && temp.endsWith("'")) {
						;
					} else if (temp.startsWith("\"") && temp.endsWith("\"")) {
						;
					} else {
						if (mode == MODE_TO_UPPER) {
							token2 = token2.toUpperCase();
						} else {
							token2 = token2.toLowerCase();
						}
					}
					buff.append(token2);
				}

				if (sb.length() == 0) {
					sb.append(buff.toString());

				} else {
					sb.append(" ");
					sb.append(StringUtil.indent(buff.toString(), indent));
				}

				preIndent = indent;
				indent = 0;

			}
		}

		return sb.toString();
	}

}

class Tokenizer extends StreamTokenizer {

	public final static char TT_SPACE = ' ';

	public final static char TT_QUOTE = '\'';

	public final static char TT_DOUBLE_QUOTE = '"';

	public final static char TT_COMMA = ',';

	public final static int TT_SYMBOL = -100;

	public final static int TT_SQL_KEYWORD = -200;

	private int tokenType = TT_EOF;

	private String token;

	public Tokenizer(String sql) {
		super(new StringReader(sql));
		resetSyntax();
		wordChars('0', '9');
		wordChars('a', 'z');
		wordChars('A', 'Z');
		wordChars('_', '_');
		wordChars(' ', ' ');
		wordChars('\'', '\'');
		wordChars('"', '"');
		wordChars('\n', '\n');
		wordChars('\r', '\r');
		wordChars('.', '.');
		wordChars('*', '*');
		wordChars('/', '/');
		// quoteChar(TT_QUOTE);
		// quoteChar(TT_DOUBLE_QUOTE);
		// tokenizer.parseNumbers();
		eolIsSignificant(false);
		// slashStarComments(true);
		// slashSlashComments(true);
	}

	public int nextToken() {
		try {
			tokenType = super.nextToken();

			switch (tokenType) {
			case Tokenizer.TT_EOF:
				tokenType = Tokenizer.TT_EOF;
				token = null;
				break;

			case Tokenizer.TT_WORD:
				token = sval;
				if (isNumeric(token)) {
					tokenType = Tokenizer.TT_NUMBER;
				}
				break;
			case Tokenizer.TT_QUOTE:
				token = "'" + sval + "'";
				break;
			case Tokenizer.TT_DOUBLE_QUOTE:
				token = "\"" + sval + "\"";
				break;

			default:
				token = String.valueOf((char) ttype);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return tokenType;
	}

	public int getTokenType() {
		return tokenType;
	}

	public String getToken() {
		return token;
	}

	boolean isNumeric(String text) {
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			if (!(chr >= '0' && chr <= '9')) {
				return false;
			}
		}
		return true;
	}

}
