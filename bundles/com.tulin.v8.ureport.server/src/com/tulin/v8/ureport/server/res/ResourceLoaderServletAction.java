package com.tulin.v8.ureport.server.res;

import com.tulin.v8.ureport.server.RenderPageServletAction;
import com.tulin.v8.ureport.server.common.UReportWebServer;
import com.tulin.v8.ureport.server.common.UReportWebServer.HTTPRequest;
import com.tulin.v8.ureport.server.common.UReportWebServer.WebServerContent;
import com.tulin.v8.ureport.server.utils.UreportWebappManager;

public class ResourceLoaderServletAction extends RenderPageServletAction {

	public ResourceLoaderServletAction(HTTPRequest httpRequest) {
		super(httpRequest);
	}

	@Override
	public WebServerContent execute() {
		String path = httpRequest.getURLPath();
		if (path.startsWith("/ureport/res/")) {
			path = path.substring(12);
		}
		String urlpath = UreportWebappManager.getContextPath() + path;
		httpRequest.setURLPath(urlpath);
		return UReportWebServer.getWebServerContent(httpRequest);
	}

}
