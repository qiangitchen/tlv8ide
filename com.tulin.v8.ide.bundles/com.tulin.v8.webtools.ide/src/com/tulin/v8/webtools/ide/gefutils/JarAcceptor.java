package com.tulin.v8.webtools.ide.gefutils;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.jsp.TLDInfo;

/**
 * An acceptor for each entries of <tt>WEB-INF/lib/*.jar</tt>.
 * 
 * @see IJarVisitor
 */
public class JarAcceptor {

	public static Object accept(IProject project, IJarVisitor visitor) {
		try {
			IContainer container = TLDInfo.getBaseDir(project);
			File basedir = container.getLocation().makeAbsolute().toFile();
			return accept(basedir, visitor);
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
		return null;
	}

	public static Object accept(File basedir, IJarVisitor visitor) {
		try {
			File lib = new File(basedir, "/WEB-INF/lib");

			if (lib.exists() && lib.isDirectory()) {
				File[] files = lib.listFiles();
				try {
					for (int i = 0; i < files.length; i++) {
						if (files[i].getName().endsWith(".jar")) {
							JarFile jarFile = new JarFile(files[i]);
							Enumeration<JarEntry> e = jarFile.entries();
							while (e.hasMoreElements()) {
								JarEntry entry = e.nextElement();
								Object result = visitor.visit(jarFile, entry);
								if (result != null) {
									return result;
								}
							}
						}
					}
				} catch (Exception ex) {
					WebToolsPlugin.logException(ex);
				}
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}

		return null;
	}
}
