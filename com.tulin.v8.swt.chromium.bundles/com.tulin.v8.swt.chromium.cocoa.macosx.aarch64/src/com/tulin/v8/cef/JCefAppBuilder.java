package com.tulin.v8.cef;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

import org.cef.CefApp;
import org.cef.CefSettings;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;

public class JCefAppBuilder {
	static final String userHomeDirectory = System.getProperty("user.home");
	static final String userLogsDirectory = userHomeDirectory + File.separator + "tulinv8" + File.separator + "logs";

	private static final CefAppBuilder builder = new CefAppBuilder();
	private static CefApp cefApp;
	private static String cachePath;

	/**
	 * JCEF初始化
	 */
	static {
		CefSettings cefSettings = builder.getCefSettings();
		cefSettings.windowless_rendering_enabled = false;
		cefSettings.locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
		String installDir = userHomeDirectory + File.separator + ".jcefswt";
		cefSettings.resources_dir_path = installDir;
		String tempDirectory = System.getProperty("java.io.tmpdir");
		cachePath = tempDirectory + File.separator + ".jcefswt-cache" + File.separator + UUID.randomUUID().toString();
		cefSettings.cache_path = cachePath;
		cefSettings.log_file = userLogsDirectory + File.separator + "cefswt.log";
		builder.setInstallDir(new File(installDir));
	}

	public static void init(MavenCefAppHandlerAdapter vavenCefAppHandlerAdapter) throws Exception {
		cefApp = builder.build();
		// Must be called before CefApp is initialized
		builder.setAppHandler(vavenCefAppHandlerAdapter);
	}

	public static CefApp getCefApp() throws Exception {
		if (cefApp == null) {
			init(null);
		}
		return cefApp;
	}

	public static void release() {
		if (cefApp != null) {
			cefApp.dispose();
		}
		if (cachePath != null) {
			try {
				File t = new File(cachePath);
				if (t.exists()) {
					deleteFolder(t);
				}
			} catch (Exception ignored) {
			}
		}
	}

	public static void deleteFolder(File folder) {
		if (folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					file.delete();
				} else {
					deleteFolder(file);
				}
			}
			folder.delete();
		} else {
			folder.delete();
		}
	}
}
