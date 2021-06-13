/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.ArrayList;
import java.util.List;

public class SelectIntoChecker {

	public static boolean check(String text) {
		String str = StringUtil.convertLineSep(text, " ");
		StringTokenizer t = new StringTokenizer(str, " ");
		String token = null;
		List wk = new ArrayList();

		if ((token = t.nextToken()).equalsIgnoreCase("SELECT")) { //$NON-NLS-1$
			wk.add(token);

			while ((token = t.nextToken()) != null) {
				if ("FROM".equals(token) || "INTO".equals(token)) { //$NON-NLS-1$ //$NON-NLS-2$
					wk.add(token);
				}
				if (wk.size() == 3) {
					break;
				}
			}

			if (wk.size() == 3) {
				String[] s = (String[]) wk.toArray(new String[3]);
				if (s[0].equals("SELECT") && s[1].equals("FROM") && s[2].equals("INTO")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					return true;
				}
			}
		}

		return false;
	}

}
