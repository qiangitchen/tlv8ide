package com.tulin.v8.tomcat;

import java.io.File;

public class CommonUtil {
	public static boolean isEmpty(Object paramObject) {
		return (paramObject == null) || (paramObject.toString().trim().equals(""));
	}

	public static String rebuildFilePath(String paramString) {
		return paramString.replace("\\", "/");
	}

	public static String getOSName() {
		String osname = System.getProperty("os.name").toLowerCase();
		// System.out.println("OS Name:" + osname);
		return osname;
	}

	public static double getOSVersion() {
		// window 版本号，例如win2000是5.0，xp是5.1，vista是6.0，win7是6.1 win10是10.0
		String osversion = System.getProperty("os.version");
		System.out.println("OS Name:" + getOSName());
		System.out.println("OS Version:" + osversion);
		String[] osvs = osversion.split("\\.");
		String osversions = "1";
		if (osvs.length > 2) {
			osversions = osvs[0] + "." + osvs[1];
		}
		return Double.parseDouble(osversions);
	}

	public static boolean isWinOS() {
		return getOSName().contains("win");
	}

	public static boolean isMacOS() {
		return getOSName().contains("mac");
	}

	public static boolean isLinuxOS() {
		return getOSName().contains("linux");
	}

	public static String getPathdep() {
		if (isWinOS()) {
			return "\\";
		} else {
			return "/";
		}
	}
	
	public static String getStudioPath() {
		// 苹果版eclipse.app封装3层
		if (CommonUtil.isMacOS()) {
			return new File(System.getProperty("user.dir")).getParentFile().getParentFile().getParentFile()
					.getAbsolutePath();
		} else {
			return new File(System.getProperty("user.dir")).getAbsolutePath();
		}
	}
}
