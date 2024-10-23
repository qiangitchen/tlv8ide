package tern.server.nodejs.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tern.OS;

/**
 * Node path helper.
 *
 */
public class NodejsProcessHelper {

	private static final String[] WINDOWS_NODE_PATHS = new String[] {
			"C:/Program Files/nodejs/node.exe".replace('/', File.separatorChar),
			"C:/Program Files (x86)/nodejs/node.exe".replace('/',
					File.separatorChar), "node" };

	private static final String[] MACOS_NODE_PATHS = new String[] {
			"/usr/local/bin/node", "/opt/local/bin/node", "node" };

	private static final String[] LINUX_NODE_PATHS = new String[] {
			"/usr/local/bin/node", "node" };

	private NodejsProcessHelper() {
	}

	public static String getNodejsPath(OS os) {
		String path = getDefaultNodejsPath(os);
		if (path != null) {
			return path;
		}
		File nodeFile = findNode(os);
		if (nodeFile != null) {
			return nodeFile.getAbsolutePath();
		}
		return "node";
	}

	public static String getDefaultNodejsPath(OS os) {
		String[] paths = getDefaultNodejsPaths(os);
		String path = null;
		for (int i = 0; i < paths.length; i++) {
			path = paths[i];
			if (new File(path).exists()) {
				return path;
			}
		}
		return null;
	}

	public static String[] getDefaultNodejsPaths(OS os) {
		switch (os) {
		case Windows:
			return WINDOWS_NODE_PATHS;
		case MacOS:
			return MACOS_NODE_PATHS;
		default:
			return LINUX_NODE_PATHS;
		}
	}

	public static File findNode(OS os) {
		String nodeFileName = getNodeFileName(os);
		String path = System.getenv("PATH");
		String[] paths = path.split("" + File.pathSeparatorChar, 0);
		List<String> directories = new ArrayList<String>();
		for (String p : paths) {
			directories.add(p);
		}

		// ensure /usr/local/bin is included for OS X
		if (os == OS.MacOS) {
			directories.add("/usr/local/bin");
		}

		// search for Node.js in the PATH directories
		for (String directory : directories) {
			File nodeFile = new File(directory, nodeFileName);

			if (nodeFile.exists()) {
				return nodeFile;
			}
		}

		return null;
	}

	private static String getNodeFileName(OS os) {
		if (os == OS.Windows) {
			return "node.exe";
		}
		return "node";
	}

}
