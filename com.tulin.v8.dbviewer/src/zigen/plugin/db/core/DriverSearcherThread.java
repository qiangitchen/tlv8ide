/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.IOException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

public class DriverSearcherThread implements Runnable {

	private ClassLoader loader;

	private String[] classpaths;

	private List driverNames;

	public DriverSearcherThread(ClassLoader loader, String[] classpaths) {
		this.loader = loader;
		this.classpaths = classpaths;

	}

	public void run() {
		driverNames = searchDriver();
	}

	public List searchDriver() {

		List driverList = new ArrayList();
		ZipInputStream in = null;

		try {
			for (int i = 0; i < classpaths.length; i++) {
				try {

					if (classpaths[i].endsWith(".class")) { //$NON-NLS-1$
						addDriverList(driverList, classpaths[i]);

					} else {
						JarFile jarFile = new JarFile(classpaths[i]);
						Enumeration e = jarFile.entries();
						while (e.hasMoreElements()) {
							JarEntry entry = (JarEntry) e.nextElement();
							addDriverList(driverList, entry.getName());
						}

					}

				} catch (IOException e) {
				}

			}

		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
			}

		}

		return driverList;
	}

	private void addDriverList(List driverList, String name) {
		if (name.endsWith(".class")) { //$NON-NLS-1$
			String cname = name.replaceFirst(".class", "").replaceAll("/", "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			try {
				Class clazz = loader.loadClass(cname);
				if (Driver.class.isAssignableFrom(clazz) && !Driver.class.equals(clazz)) {
					driverList.add(cname);
				} else {
					return;
				}
			} catch (NoClassDefFoundError ex) {
				// DbPlugin.log(ex);
			} catch (ClassNotFoundException ex) {
				// DbPlugin.log(ex);
			} catch (SecurityException ex) {
				// DbPlugin.log(ex);
			} catch (Throwable ex) {
				// DbPlugin.log(ex);
			}
		}
	}

	public List getDriverNames() {
		return driverNames;
	}

}
