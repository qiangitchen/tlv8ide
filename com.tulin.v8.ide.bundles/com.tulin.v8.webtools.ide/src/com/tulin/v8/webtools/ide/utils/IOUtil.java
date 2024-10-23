package com.tulin.v8.webtools.ide.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class IOUtil {

	/**
	 * Returns stream contents as a byte array.
	 */
	public static byte[] readStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024 * 8];
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		byte[] result = out.toByteArray();
		closeQuietly(in);
		closeQuietly(out);
		return result;
	}

	/**
	 * Copy folder.
	 * 
	 * @param from the target folder
	 * @param to   the destination folder
	 * @throws IOException when I/O error occurs
	 */
	public static void copyFolder(File from, File to) throws IOException {
		File[] files = from.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				File newDir = new File(to, files[i].getName());
				if (!newDir.exists()) {
					newDir.mkdir();
					copyFolder(files[i], newDir);
				}
			} else {
				File newFile = new File(to, files[i].getName());
				if (!newFile.exists()) {
					InputStream in = new FileInputStream(files[i]);
					OutputStream out = new FileOutputStream(newFile);
					byte[] buf = new byte[1027 * 8];
					int length = 0;
					while ((length = in.read(buf)) != -1) {
						out.write(buf, 0, length);
					}
					closeQuietly(in);
					closeQuietly(out);
				}
			}
		}
	}

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ex) {
			}
		}
	}

	public static IFile getIFile(Object file) {
		IFile ifile = null;
		if (file instanceof IFile) {
			ifile = (IFile) file;
		} else if (file instanceof File) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IPath location = Path.fromOSString(((File) file).getAbsolutePath());
			ifile = workspace.getRoot().getFileForLocation(location);
		}
		return ifile;
	}
}
