package com.tulin.v8.ide.wizards.utils;

import com.tulin.v8.core.TuLinPlugin;

public class FilePathUtils {

	public static String getContainerPath(String containername) {
		String PROJECT_WEB_FOLDER = TuLinPlugin.getCurrentProjectWebFolderName();
		String containerPath = containername
				.substring(containername.indexOf("/" + PROJECT_WEB_FOLDER + "/") + PROJECT_WEB_FOLDER.length() + 2);
		return containerPath;
	}

	public static String getRootPath(String containername) {
		String containerPath = FilePathUtils.getContainerPath(containername);
		String rootpath = "";
		if (!"".equals(containerPath)) {
			String[] dotns = containerPath.split("/");
			for (int i = 0; i < dotns.length; i++) {
				rootpath += "../";
			}
		}
		return rootpath;
	}

}
