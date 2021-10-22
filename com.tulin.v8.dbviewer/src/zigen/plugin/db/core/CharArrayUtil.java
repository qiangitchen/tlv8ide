/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StringWriter;

import zigen.plugin.db.DbPlugin;

public class CharArrayUtil {

	public static String toString(char[] chars) {

		if (chars == null)
			return null;

		String out = null;
		CharArrayReader br = null;
		StringWriter sw = null;
		char[] buf = new char[1024];
		int i;
		try {
			br = new CharArrayReader(chars);
			sw = new StringWriter();
			while ((i = br.read(buf, 0, buf.length)) != -1) {
				sw.write(buf, 0, i);
			}
			sw.flush();
			sw.close();

			out = sw.toString();
		} catch (Exception e) {
			DbPlugin.log(e);
			DbPlugin.getDefault().showWarningMessage(Messages.getString("CharArrayUtil.Message")); //$NON-NLS-1$
		}
		return out;

	}

}
