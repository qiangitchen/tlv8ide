/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternUtil {

	/**
	 * It is a method for the retrieval such as not the regular expression but A, A, and B.
     * Two or more key words are delimited by the comma.
	 * It becomes the back and forth agreement retrieval.
	 * @param key
	 * @param caseSensitive
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static final Pattern getPattern(String key, boolean caseSensitive) throws PatternSyntaxException {
		Pattern pattern = null;
		String[] keys = key.split(",| "); //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		for (int i = 0; i < keys.length; i++) {
			String keywd = keys[i];
			if (keywd != null && keywd.trim().length() > 0) {
				// keywd = keywd.trim().replaceAll("\\*", "\\.\\*"); //$NON-NLS-1$ //$NON-NLS-2$
				StringBuffer w = new StringBuffer();
				w.append(".*");
				w.append(keywd.trim());
				w.append(".*");

				if (cnt == 0) {
					sb.append(w.toString());
				} else {
					sb.append("|"); //$NON-NLS-1$
					sb.append(w.toString());
				}
				cnt++;
			}
		}
		if (!caseSensitive) {
			pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(sb.toString());
		}
		return pattern;

	}

	public static final Pattern getPattern2(String key, boolean caseSensitive) throws PatternSyntaxException {
		Pattern pattern = null;
		String[] keys = key.split(","); //$NON-NLS-1$
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		for (int i = 0; i < keys.length; i++) {
			String keywd = keys[i];
			if (keywd != null && keywd.trim().length() > 0) {
				keywd = keywd.trim().replaceAll("\\*", "\\.\\*"); //$NON-NLS-1$ //$NON-NLS-2$
				if (cnt == 0) {
					sb.append(keywd);
				} else {
					sb.append("|"); //$NON-NLS-1$
					sb.append(keywd);
				}
				cnt++;
			}
		}
		if (!caseSensitive) {
			pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(sb.toString());
		}
		return pattern;

	}
}
