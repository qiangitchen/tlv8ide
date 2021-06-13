package com.tulin.v8.core.config;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class AppConfig {
	private static AppConfig a = null;

	public static AppConfig getConfig() {
		if (a == null)
			a = new AppConfig();
		return a;
	}

	public static String getStudioPath() {
		if (isMacOS())
			return new File(System.getProperty("user.dir")).getParentFile().getParentFile().getParent();
		else
			return new File(System.getProperty("user.dir")).getAbsolutePath();
	}

	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static boolean isMacOS() {
		return getOSName().contains("mac");
	}

	public static IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	public static String getProjectName() {
		IProject project = getProject("tlv8");
		if (project != null) {
			return "tlv8";
		}
		return "JBIZ";// 为了兼容云捷项目资源保留
	}

	public static String getProjectWebFolder(String name) {
		IProject project = getProject(name);
		String WEB_FOLDER = project.getFolder("WebContent").getName();
		if (WEB_FOLDER == null) {
			WEB_FOLDER = project.getFolder("WebRoot").getName();
		}
		return WEB_FOLDER;
	}

	public static String getWorkspacesPath() {
//		String realPath = getStudioPath();
//		if (realPath.indexOf("workspace") > 0) {
//			realPath = realPath.substring(0, realPath.indexOf("workspace"));
//			realPath += "/workspace";
//		} else {
//			File file = new File(realPath);
//			realPath = file.getParent() + "/workspace";
//		}
//		return realPath;
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}

	public static String getClassesPath() {
		String pname = getProjectName();
		IProject project = getProject(pname);
		String webfolder = getProjectWebFolder(pname);
		return project.findMember(webfolder + "/WEB-INF/classes").getLocation().toString();
	}
}