/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;

import zigen.plugin.db.core.XMLManager;

public class DefaultXmlManager {

	protected File file;

	public DefaultXmlManager(IPath path, String fileName) {
		String readWritePath = path.append(fileName).toOSString();
		file = new File(readWritePath);
	}

	public void saveXml(Object obj) throws IOException {
		XMLManager.save(file, obj);
	}

	public Object loadXml() throws Exception {
		Object obj = null;
		if (file.exists()) {
			try {
				int limit = 6400;
				String _limit = System.getProperty("entityExpansionLimit"); //$NON-NLS-1$
				if (_limit != null) {
					limit = Integer.parseInt(_limit);
				}
				obj = XMLManager.load(file, limit);
			} catch (Exception e) {
				throw e;
			}
		}
		return obj;
	}


}
