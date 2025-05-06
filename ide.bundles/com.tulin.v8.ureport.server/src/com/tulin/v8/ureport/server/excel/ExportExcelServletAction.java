package com.tulin.v8.ureport.server.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.bstek.ureport.build.ReportBuilder;
import com.bstek.ureport.console.cache.TempObjectCache;
import com.bstek.ureport.console.exception.ReportDesignException;
import com.bstek.ureport.definition.ReportDefinition;
import com.bstek.ureport.exception.ReportComputeException;
import com.bstek.ureport.export.ExportConfigure;
import com.bstek.ureport.export.ExportConfigureImpl;
import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.excel.high.ExcelProducer;
import com.bstek.ureport.model.Report;
import com.tulin.v8.ureport.server.Activator;
import com.tulin.v8.ureport.server.RenderPageServletAction;
import com.tulin.v8.ureport.server.common.UReportWebServer.HTTPRequest;

import chrriis.common.WebServerContent;

public class ExportExcelServletAction extends RenderPageServletAction {
	private ReportBuilder reportBuilder;
	private ExportManager exportManager;
	private ExcelProducer excelProducer = new ExcelProducer();

	ApplicationContext applicationContext;

	public ExportExcelServletAction(HTTPRequest httpRequest) {
		super(httpRequest);
		this.applicationContext = Activator.applicationContext;
		exportManager = applicationContext.getBean(ExportManager.class);
		reportBuilder = applicationContext.getBean(ReportBuilder.class);
	}

	@Override
	public WebServerContent execute() {
		String method = retriveMethod();
		if (method != null) {
			return invokeMethod(method);
		} else {
			return buildExcel(false, false);
		}
	}

	protected String retriveMethod() {
		String path = "/ureport/excel";
		String uri = httpRequest.getRequestURI();
		String targetUrl = uri.substring(path.length());
		int slashPos = targetUrl.indexOf("/");
		if (slashPos > -1) {
			String methodName = targetUrl.substring(slashPos + 1).trim();
			return methodName.length() > 0 ? methodName : null;
		}
		return null;
	}

	public void paging() throws ServletException, IOException {
		buildExcel(true, false);
	}

	public void sheet() throws ServletException, IOException {
		buildExcel(false, true);
	}

	public WebServerContent buildExcel(boolean withPage, boolean withSheet) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String file = httpRequest.getParameter("_u");
		file = decode(file);
		if (StringUtils.isBlank(file)) {
			throw new ReportComputeException("Report file can not be null.");
		}
		try {
			Map<String, Object> parameters = buildParameters();
			if (file.equals(PREVIEW_KEY)) {
				ReportDefinition reportDefinition = (ReportDefinition) TempObjectCache.getObject(PREVIEW_KEY);
				if (reportDefinition == null) {
					throw new ReportDesignException("Report data has expired,can not do export excel.");
				}
				Report report = reportBuilder.buildReport(reportDefinition, parameters);
				if (withPage) {
					excelProducer.produceWithPaging(report, out);
				} else if (withSheet) {
					excelProducer.produceWithSheet(report, out);
				} else {
					excelProducer.produce(report, out);
				}
			} else {
				ExportConfigure configure = new ExportConfigureImpl(file, parameters, out);
				if (withPage) {
					exportManager.exportExcelWithPaging(configure);
				} else if (withSheet) {
					exportManager.exportExcelWithPagingSheet(configure);
				} else {
					exportManager.exportExcel(configure);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ExcelWebServerContent(out);
	}

}
