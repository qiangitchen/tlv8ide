package com.tulin.v8.flowdesigner.server.utils;

import com.tulin.v8.flowdesigner.server.common.FlowWebServer;

public class WebappManager {
	public static String getHost() {
		return FlowWebServer.getDefaultWebServer().getURLPrefix();
	}

	public static int getPort() {
		return FlowWebServer.getDefaultWebServer().getPort();
	}

	public static String getURL() {
		return FlowWebServer.getDefaultWebServer().getClassPathResourceURL("", "flw");
	}

	public static String getContextPath() {
		return getURL().substring(getHost().length());
	}

	public static String getSVGDesignerURL() {
		return getURL() + "/dwr/svg-dwr-editer.html";
	}
	
	public static String getVMLDesignerURL() {
		return getURL() + "/dwr/vml-dwr-editer.html";
	}
}
