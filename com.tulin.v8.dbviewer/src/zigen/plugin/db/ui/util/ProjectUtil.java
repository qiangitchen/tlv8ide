/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.QualifiedName;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.properties.DBPropertyPage;

public class ProjectUtil {


	public static IDBConfig getDBConfig(IProject project) {
		String dbName = getDBConfigName(project);
		if (dbName != null) {
			return DBConfigManager.getDBConfig(dbName);
		} else {
			return null;
		}
	}


	static String getDBConfigName(IProject project) {
		String dbName = null;
		try {
			if (project.isOpen()) {
				dbName = project.getPersistentProperty(new QualifiedName(DBPropertyPage.QUALIFIER, DBPropertyPage.SELECTED_DB));
			} else {
				;
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return dbName;
	}

}
