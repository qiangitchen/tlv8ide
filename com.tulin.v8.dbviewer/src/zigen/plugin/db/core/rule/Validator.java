/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {

	public static final String entry_Check(String filedName, String text) {
		if (text == null || text.length() == 0) {
			return filedName + " is not null.";
		}
		return null;
	}

	public static final String length_Check(String filedName, String text, int maxBytes) {

		if (text == null || text.equals(""))
			return null;

		int cnt = 0;
		for (int i = 0; i < text.length(); i++) {
			String s = text.substring(i, i + 1);
			cnt = cnt + s.getBytes().length;
		}

		if (cnt > maxBytes) {
			return filedName + " is " + maxBytes + "byte limit.";
		}
		return null;
	}

	public static final String numeric_Check(String filedName, String text) {
		if (text == null || text.equals(""))
			return null;

		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			if (!(chr >= '0' && chr <= '9')) {
				return filedName + " is numeric only,";
			}
		}
		return null;
	}

	public static final String decimal_Check(String filedName, String text) {
		try {
			new BigDecimal(text);

		} catch (NumberFormatException ex) {
			return (filedName + " is numeric only.");
		}
		return null;
	}

	public static final String date_Check(String filedName, String text) {
		final String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);
		} catch (ParseException e) {
			return filedName + " is the format of " + pattern + ".";
		}

		return null;

	}
	public static final String timestamp_Check(String filedName, String text) {
		String pattern = "yyyy-MM-dd HH:mm:ss";

		if (text.indexOf("/") > 0) {
			pattern = "yyyy/MM/dd HH:mm:ss";
		}

		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {
			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	public static final String timestamp2_Check(String filedName, String text) {
		String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		if (text.indexOf("/") > 0) {
			pattern = "yyyy/MM/dd HH:mm:ss.SSS";
		}

		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {
			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	public static final String time_Check(String filedName, String text) {
		final String pattern = "HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(text);

		} catch (ParseException e) {

			return filedName + " should be input " + pattern + ".";
		}

		return null;

	}

	public static final String boolean_Check(String filedName, String text) {
		String str = text.toLowerCase();
		if ("true".equals(str) || "false".equals(str)) {
			return null;
		} else {
			return filedName + " is true or false";
		}

	}

	public static final String tinyint_Check(String filedName, String text) {
		final String msg = "  is range from -128 to 127. ";

		try {
			if (decimal_Check(filedName, text) == null) {
				int value = Integer.parseInt(text);
				if (-128 <= value && value <= 127) {
					return null;
				} else {
					return filedName + msg;
				}
			} else {
				return filedName + msg;
			}
		} catch (NumberFormatException e) {
			return filedName + msg;
		}

	}

}
