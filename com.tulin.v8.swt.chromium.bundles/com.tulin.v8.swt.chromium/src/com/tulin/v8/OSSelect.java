package com.tulin.v8;

public class OSSelect {

	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}

	public static boolean isX86() {
		boolean res = false;
		String arch = System.getProperty("os.arch").toLowerCase().trim();
		if ("i386".equals(arch) || "i686".equals(arch) || "x86".equals(arch)) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}

	public static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().contains("linux");
	}

	public static boolean isARM() {
		boolean res = false;
		String arch = System.getProperty("os.arch").toLowerCase().trim();
		if ("i386".equals(arch) || "i686".equals(arch) || "x86".equals(arch)) {
			res = false;
		} else if ("x86_64".equals(arch) || "amd64".equals(arch)) {
			res = false;
		} else if (arch.startsWith("arm")) {
			res = true;
		} else {
			res = true;
		}
		return res;
	}

	public static boolean isMacOS() {
		String osName = System.getProperty("os.name");
		return (osName.toLowerCase().startsWith("mac") || osName.toLowerCase().startsWith("darwin"));
	}

	public static double osVersion() {
		double v = 0d;
		try {
			String version = System.getProperty("os.version");
			if (version.indexOf("-") > 0) {
				version = version.split("-")[0];
			}
			if (version.indexOf(".") > 0) {
				String[] versions = version.split("\\.");
				v = Double.parseDouble(versions[0] + "." + versions[1]);
			}
		} catch (Exception e) {
		}
		return v;
	}

}
