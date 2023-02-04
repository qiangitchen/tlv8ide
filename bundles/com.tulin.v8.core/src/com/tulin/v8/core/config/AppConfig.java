package com.tulin.v8.core.config;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;

import com.tulin.v8.core.TuLinPlugin;

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

	public static String getWorkspacesPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}

	/**
	 * 获取当前项目的classes位置
	 * 
	 * @return
	 */
	public static String getClassesPath() {
		IProject project = TuLinPlugin.getCurrentProject();
		IResource resource = TuLinPlugin.getProjectWebFolder(project);
		if (resource == null) {
			return project.findMember("src/main/resources").getLocation().toString();
		}
		return project.findMember(resource.getName() + "/WEB-INF/classes").getLocation().toString();
	}

	/**
	 * 获取资源位置
	 * 
	 * @return
	 */
	public static String getResourcesPath() {
		IProject project = TuLinPlugin.getCurrentProject();
		IResource dsource = project.findMember("src/main/resources");
		if (dsource == null) {
			dsource = project.findMember("src");
		}
		return dsource.getLocation().toString();
	}
}