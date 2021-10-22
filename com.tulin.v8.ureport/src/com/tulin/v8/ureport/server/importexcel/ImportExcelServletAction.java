package com.tulin.v8.ureport.server.importexcel;

import com.tulin.v8.ureport.server.RenderPageServletAction;
import com.tulin.v8.ureport.server.common.UReportWebServer.HTTPRequest;
import com.tulin.v8.ureport.server.common.UReportWebServer.WebServerContent;

public class ImportExcelServletAction extends RenderPageServletAction{

	public ImportExcelServletAction(HTTPRequest httpRequest) {
		super(httpRequest);
	}

	@Override
	public WebServerContent execute() {
		System.out.println(httpRequest.getParameterMap());
		return null;
	}

}
