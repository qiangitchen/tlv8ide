/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import zigen.plugin.db.DbPlugin;

public class ByteArrayUtil {

	public static String toString(byte[] bytes, String charsetName) {

		if (bytes == null)
			return null;

		String out = null;
		InputStreamReader br = null;
		StringWriter sw = null;
		char[] buf = new char[1024];
		int i;
		try {
			br = new InputStreamReader(new ByteArrayInputStream(bytes), charsetName);
			sw = new StringWriter();
			while ((i = br.read(buf, 0, buf.length)) != -1) {
				sw.write(buf, 0, i);
			}
			sw.flush();
			sw.close();

			out = sw.toString();
		} catch (Exception e) {
			DbPlugin.log(e);
			DbPlugin.getDefault().showWarningMessage(Messages.getString("ByteArrayUtil.Message")); //$NON-NLS-1$
		}
		return out;

	}

}
