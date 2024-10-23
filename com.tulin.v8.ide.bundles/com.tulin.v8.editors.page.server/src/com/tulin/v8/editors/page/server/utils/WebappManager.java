package com.tulin.v8.editors.page.server.utils;

import com.tulin.v8.editors.page.server.common.PageWebServer;

public class WebappManager {
	public static String getHost() {
		return PageWebServer.getDefaultWebServer().getURLPrefix();
	}

	public static int getPort() {
		return PageWebServer.getDefaultWebServer().getPort();
	}

	public static String getURL() {
		return PageWebServer.getDefaultWebServer().getClassPathResourceURL("", "webdesign");
	}

	public static String getContextPath() {
		return getURL().substring(getHost().length());
	}

	public static String getDesignerURL() {
		return getURL() + "/index.html";
	}
	
	public static String getMDesignerURL() {
		return getURL() + "/mindex.html";
	}
}
