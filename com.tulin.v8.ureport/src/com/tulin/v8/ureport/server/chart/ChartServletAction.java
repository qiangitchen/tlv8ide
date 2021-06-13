package com.tulin.v8.ureport.server.chart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.bstek.ureport.cache.CacheUtils;
import com.bstek.ureport.chart.ChartData;
import com.bstek.ureport.utils.UnitUtils;
import com.tulin.v8.ureport.server.DefWebServerContent;
import com.tulin.v8.ureport.server.RenderPageServletAction;
import com.tulin.v8.ureport.server.common.UReportWebServer.HTTPRequest;
import com.tulin.v8.ureport.server.common.UReportWebServer.WebServerContent;

public class ChartServletAction extends RenderPageServletAction {

	public ChartServletAction(HTTPRequest httpRequest) {
		super(httpRequest);
	}

	@Override
	public WebServerContent execute() {
		String method = retriveMethod();
		if (method != null) {
			return invokeMethod(method);
		}
		return null;
	}

	public WebServerContent storeData() {
		String file = httpRequest.getParameter("_u");
		file = decode(file);
		String chartId = httpRequest.getParameter("_chartId");
		ChartData chartData = CacheUtils.getChartData(chartId);
		if (chartData != null) {
			String base64Data = httpRequest.getParameter("_base64Data");
			String prefix = "data:image/png;base64,";
			if (base64Data != null) {
				if (base64Data.startsWith(prefix)) {
					base64Data = base64Data.substring(prefix.length(), base64Data.length());
				}
			}
			chartData.setBase64Data(base64Data);
			String width = httpRequest.getParameter("_width");
			String height = httpRequest.getParameter("_height");
			chartData.setHeight(UnitUtils.pixelToPoint(Integer.valueOf(height)));
			chartData.setWidth(UnitUtils.pixelToPoint(Integer.valueOf(width)));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			writeObjectToJson(out, "{'status':'ok'}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DefWebServerContent(out);
	}

	protected String retriveMethod() {
		String path = "/ureport/chart";
		String uri = httpRequest.getRequestURI();
		String targetUrl = uri.substring(path.length());
		int slashPos = targetUrl.indexOf("/");
		if (slashPos > -1) {
			String methodName = targetUrl.substring(slashPos + 1).trim();
			return methodName.length() > 0 ? methodName : null;
		}
		return null;
	}

}
