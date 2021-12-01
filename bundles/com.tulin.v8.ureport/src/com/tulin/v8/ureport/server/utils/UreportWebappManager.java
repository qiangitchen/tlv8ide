package com.tulin.v8.ureport.server.utils;

import com.tulin.v8.ureport.server.common.UReportWebServer;

public class UreportWebappManager {
	public static String getHost() {
		return UReportWebServer.getDefaultWebServer().getURLPrefix();
	}

	public static int getPort() {
		return UReportWebServer.getDefaultWebServer().getPort();
	}

	public static String getURL() {
		return UReportWebServer.getDefaultWebServer().getClassPathResourceURL("", "webapp");
	}

	public static String getContextPath() {
		return getURL().substring(getHost().length());
	}

	public static String getUreportDesignerURL() {
		return getURL() + "/ureport-html/designer.html";
	}
	
	public static String getUreportPreviewURL() {
		return getHost() + "/ureport/preview";
	}

}
