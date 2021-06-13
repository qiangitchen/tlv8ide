/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

public class JDBCUnicodeConvertor {

	public static final String convert(String str) {

		if (str == null) {
			return null;
		}

		char[] chars = str.toCharArray();
 		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '\u301c': // '～'
				chars[i] = '\uff5e';
				break;
			case '\u2016': // '∥'
				chars[i] = '\u2225';
				break;
			case '\u2212': // '－'
				chars[i] = '\uff0d';
				break;
			case '\u00a2': // '￠'
				chars[i] = '\uffe0';
				break;
			case '\u00a3': // '￡'
				chars[i] = '\uffe1';
				break;
			case '\u00ac': // '￢'
				chars[i] = '\uffe2';
				break;
			case '\u00a6': // '￤'
				chars[i] = '\uffe4';
				break;
			case '\u2032': // '＇'
				chars[i] = '\uff07';
				break;
			case '\u2033': // '＂'
				chars[i] = '\uff02';
				break;
			default:
				break;
			}
		}
		return new String(chars);
	}

}
