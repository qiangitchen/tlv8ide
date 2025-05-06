package com.tulin.v8;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

	public static double getGCCVersion() {
		double v = 0d;
		try {
			// 使用ProcessBuilder执行gcc --version命令
			ProcessBuilder builder = new ProcessBuilder("gcc", "--version");
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.contains("gcc")) {
					// 解析版本信息，例如："gcc (Ubuntu 7.5.0-3ubuntu1~18.04) 7.5.0"
					String[] inlns = line.split(" ");
					String version = inlns[inlns.length - 1];
					System.out.println("GCC Version: " + version);
					if (version.indexOf("-") > 0) {
						version = version.split("-")[0];
					}
					if (version.indexOf(".") > 0) {
						String[] versions = version.split("\\.");
						v = Double.parseDouble(versions[0] + "." + versions[1]);
					}
					break;
				}
			}
			process.waitFor(); // 等待进程结束
		} catch (Exception e) {
		}
		System.out.println("GCC Double Version: " + v);
		return v;
	}

}
