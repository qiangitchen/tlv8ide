package tern.eclipse.ide.server.nodejs.core;

import org.eclipse.core.runtime.Platform;

import tern.OS;
import tern.server.nodejs.process.NPMProcessHelper;
import tern.server.nodejs.process.NodejsProcessHelper;

/**
 * IDE node.js process helper.
 */
public class IDENodejsProcessHelper {

	private static final OS os;

	static {
		if (Platform.getOS().startsWith("win")) {
			os = OS.Windows;
		} else if (Platform.getOS().equals(Platform.OS_MACOSX)) {
			os = OS.MacOS;
		} else {
			os = OS.Linux;
		}
	}

	private IDENodejsProcessHelper() {
	}

	public static String getNodejsPath() {
		return NodejsProcessHelper.getNodejsPath(os);
	}

	public static String[] getDefaultNodejsPaths() {
		return NodejsProcessHelper.getDefaultNodejsPaths(os);
	}

	public static String getNPMPath() {
		return NPMProcessHelper.getNPMPath(os);
	}

	public static String[] getDefaultNPMPaths() {
		return NPMProcessHelper.getDefaultNPMPaths(os);
	}

	public static OS getOs() {
		return os;
	}
}
