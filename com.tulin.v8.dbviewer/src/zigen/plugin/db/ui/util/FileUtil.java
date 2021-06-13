/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.QualifiedName;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBConfigManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.properties.DBPropertyPage;

public class FileUtil {

	public static IDBConfig getDBConfig(IFile file) {
		String dbName = getDBConfigName(file);
		if (dbName != null) {
			return DBConfigManager.getDBConfig(dbName);
		} else {
			return null;
		}
	}

	static String getDBConfigName(IFile file) {
		String dbName = null;
		try {
			if (file.isAccessible()) {
				dbName = file.getPersistentProperty(new QualifiedName(DBPropertyPage.QUALIFIER, DBPropertyPage.SELECTED_DB));
			} else {
				;
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return dbName;
	}


	public static String getContents(IFile file) {
		int len;
		char[] buffer = new char[4096];
		// FileReader reader = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			// reader = new FileReader(file);
			reader = new BufferedReader(new InputStreamReader(file.getContents()));
			while ((len = reader.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				;
			}
		}
		return null;
	}

	public static boolean isSqlFile(IFile file) {
		String fileName = file.getName().toLowerCase();
		if (fileName.endsWith(".sql")) { //$NON-NLS-1$
			return true;
		} else {
			return false;
		}

	}

	public static String getContents(File file) {
		int len;
		char[] buffer = new char[4096];
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((len = reader.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				;
			}
		}
		return null;
	}

	public static boolean isSqlFile(File file) {
		String fileName = file.getName().toLowerCase();
		if (fileName.endsWith(".sql")) { //$NON-NLS-1$
			return true;
		} else {
			return false;
		}

	}

	public static boolean rename(File file) {
		String folder = file.getParent();
		String fileName = file.getName();

		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd"); //$NON-NLS-1$
		String date = format.format(Calendar.getInstance().getTime());
		String suffix = "." + date + "_bak"; //$NON-NLS-1$ //$NON-NLS-2$

		return file.renameTo(new File(folder + File.separator + fileName + suffix));
	}

}
