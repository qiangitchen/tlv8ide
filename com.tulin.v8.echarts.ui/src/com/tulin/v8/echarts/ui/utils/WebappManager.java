package com.tulin.v8.echarts.ui.utils;

import com.tulin.v8.echarts.ui.common.WebServer;

public class WebappManager {
	public static String getHost() {
		return WebServer.getDefaultWebServer().getURLPrefix();
	}

	public static int getPort() {
		return WebServer.getDefaultWebServer().getPort();
	}

	public static String getURL() {
		return WebServer.getDefaultWebServer().getClassPathResourceURL("", "chartsnewizard");
	}

	public static String getContextPath() {
		return getURL().substring(getHost().length());
	}

	public static String getDesignUrlURL() {
		return getURL() + "/chartpreview/chartsmodle.html";
	}
	
}
