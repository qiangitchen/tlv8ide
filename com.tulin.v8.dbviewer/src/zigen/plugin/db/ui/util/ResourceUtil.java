/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;

public class ResourceUtil {


	public static IDBConfig getDBConfig(IResource resource) {
		String dbName = getDBConfigName(resource);
		if (dbName != null) {
			return DBConfigManager.getDBConfig(dbName);
		} else {
			return null;
		}
	}


	static String getDBConfigName(IResource resource) {

		if (resource instanceof IFile) {
			String dbName = FileUtil.getDBConfigName((IFile) resource);
			if (dbName != null)
				return dbName;
		}
		return ProjectUtil.getDBConfigName(resource.getProject());
	}

}
