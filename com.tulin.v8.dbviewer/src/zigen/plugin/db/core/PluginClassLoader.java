/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import zigen.plugin.db.DbPlugin;

public class PluginClassLoader extends URLClassLoader {

	private PluginClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public static PluginClassLoader getClassLoader(String[] classpaths, ClassLoader parent) {
		PluginClassLoader loader = new PluginClassLoader(convert(classpaths), parent);

		return loader;
	}

	private static URL[] convert(String[] classpaths) {
		URL[] wk = new URL[classpaths.length];
		int cnt = 0;
		for (int i = 0; i < classpaths.length; i++) {
			String classpath = classpaths[i];
			try {
				URL url = new File(classpath).toURL();
				wk[i] = url;
				cnt++;
			} catch (Exception e) {
				DbPlugin.log(e);
			}
		}

		URL[] url = new URL[cnt];
		System.arraycopy(wk, 0, url, 0, cnt);
		return url;
	}
}
