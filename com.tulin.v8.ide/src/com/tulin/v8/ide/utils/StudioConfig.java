package com.tulin.v8.ide.utils;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.StudioPlugin;

public class StudioConfig {
	public static final String PHANTOM_PROJECT_NAME = getProjectName();
	public static final String PROJECT_WEB_FOLDER = getWebFolder();
	public static final String PROJECT_PWEB_FOLDER = PHANTOM_PROJECT_NAME + "/" + PROJECT_WEB_FOLDER;
	private static StudioConfig a = null;

	private static String getProjectName() {
		IProject project = StudioPlugin.getProject("tlv8");
		if (project != null) {
			return "tlv8";
		}
		return "JBIZ";// 为了兼容云捷项目资源保留
	}

	private static String getWebFolder() {
		IProject project = StudioPlugin.getProject(PHANTOM_PROJECT_NAME);
		String WEB_FOLDER = project.getFolder("WebContent").getName();
		if (WEB_FOLDER == null) {
			WEB_FOLDER = project.getFolder("WebRoot").getName();
		}
		return WEB_FOLDER;
	}

	public static StudioConfig getConfig() {
		if (a == null)
			a = new StudioConfig();
		return a;
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

	// 苹果系统专用
	public static String getContentsPath() {
		return new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath();
	}

	public static String getWorkspacesPath() {
		// String realPath = getStudioPath();
		// if (realPath.indexOf("workspace") > 0) {
		// realPath = realPath.substring(0, realPath.indexOf("workspace"));
		// realPath += "/workspace";
		// } else {
		// File file = new File(realPath);
		// realPath = file.getParent() + "/workspace";
		// }
		// return realPath;
		// return Platform.getLocation().toString();
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}

	public static String getStudioAppRootPath() {
		if (CommonUtil.isMacOS()) {
			return getContentsPath() + "/Eclipse/dropins/studio-app";
		} else {
			return getStudioPath() + "/dropins/studio-app";
		}
	}

	public static String getTempletPath() {
		return getStudioAppRootPath() + "/templet";
	}

	public static String getMobileTempletPath() {
		return getStudioAppRootPath() + "/mobile";
	}

	public static String getTomcatPath() {
		IPreferenceStore localIPreferenceStore = StudioPlugin.getDefault().getPreferenceStore();
		return localIPreferenceStore.getString("tomcatBase");
	}

	public static String getStudioConfigBasePath() {
		return getStudioAppRootPath() + "/config";
	}

	public static String getStudioPluginLibPath() {
		return getStudioAppRootPath() + "/plugin/lib";
	}

	public static String getUIServerResourcePath() {
		return new File(getUIPath()).getParent();
	}

	public static String getUIPath() {
		IPreferenceStore localIPreferenceStore = StudioPlugin.getDefault().getPreferenceStore();
		String str = localIPreferenceStore.getString(PHANTOM_PROJECT_NAME);
		if (CommonUtil.isEmpty(str) || !new File(str).exists()) {
			str = StudioPlugin.getProject(PHANTOM_PROJECT_NAME).getLocation().toString();
			localIPreferenceStore.setValue(PHANTOM_PROJECT_NAME, str);
		}
		return CommonUtil.rebuildFilePath(str);
	}

	public static String getUISrcPath() {
		return getUIPath();
	}

	public static String getUISettingPath() {
		return getUIPath() + "/.settings";
	}

	public static String getRootPath(String paramString) {
		if (CommonUtil.rebuildFilePath(paramString).indexOf("/" + PHANTOM_PROJECT_NAME + "/") != -1)
			return getUIPath();
		return paramString;
	}

	public static String getRootDirName(String paramString) {
		paramString = CommonUtil.rebuildFilePath(paramString);
		if ((paramString.indexOf("/" + PHANTOM_PROJECT_NAME + "/") != -1)
				|| (paramString.endsWith("/" + PHANTOM_PROJECT_NAME)))
			return PHANTOM_PROJECT_NAME;
		return "";
	}
}
