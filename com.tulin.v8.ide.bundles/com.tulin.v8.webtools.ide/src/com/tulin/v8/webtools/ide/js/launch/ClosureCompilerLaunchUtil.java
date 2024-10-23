package com.tulin.v8.webtools.ide.js.launch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * Provides utility methods for JavaScript launching.
 */
public class ClosureCompilerLaunchUtil {

	private static File[] files;

	public static File[] getClassPaths() {
		if (files != null) {
			return files;
		}
		File dir = WebToolsPlugin.getDefault().getStateLocation().toFile();
		files = new File[] { new File(dir, "compiler.jar"), };
		return files;
	}

	public static String[] getClassPathAsStringArray() {
		File[] files = getClassPaths();
		String[] paths = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			paths[i] = files[i].getAbsolutePath();
		}
		return paths;
	}

	public static void copyLibraries() throws CoreException {
		File[] files = getClassPaths();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].exists()) {
				copyFile(WebToolsPlugin.getDefault().getBundle().getEntry("/lib/" + files[i].getName()), files[i]);
			}
		}
	}

	public static void removeLibraries() throws CoreException {
		File[] files = getClassPaths();
		for (int i = 0; i < files.length; i++) {
			if (files[i].exists()) {
				files[i].delete();
			}
		}
	}

	private static void copyFile(URL url, File file) throws CoreException {
		try {
			InputStream in = url.openStream();
			OutputStream out = new FileOutputStream(file);
			try {
				byte[] buf = new byte[1024 * 8];
				int length = 0;
				while ((length = in.read(buf)) != -1) {
					out.write(buf, 0, length);
				}
			} finally {
				in.close();
				out.close();
			}
		} catch (Exception ex) {
			IStatus status = new Status(IStatus.ERROR, WebToolsPlugin.getPluginId(), 0, ex.toString(), ex);
			throw new CoreException(status);
		}
	}
}
