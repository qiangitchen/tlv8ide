package com.tulin.v8.ureport.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;

import com.bstek.ureport.console.designer.ReportDefinitionWrapper;
import com.bstek.ureport.console.designer.ReportUtils;
import com.bstek.ureport.console.exception.ReportDesignException;
import com.bstek.ureport.definition.ReportDefinition;
import com.bstek.ureport.export.ReportRender;
import com.bstek.ureport.parser.ReportParser;
import com.bstek.ureport.provider.report.ReportProvider;
import com.tulin.v8.ureport.server.cache.TempObjectCache;
import com.tulin.v8.ureport.server.common.UReportWebServer.HTTPRequest;

import chrriis.common.WebServerContent;

public class DesignerServletAction extends RenderPageServletAction {
	private ReportRender reportRender;
	private ReportParser reportParser;
	private List<ReportProvider> reportProviders=new ArrayList<ReportProvider>();

	ApplicationContext applicationContext;

	public DesignerServletAction(HTTPRequest httpRequest) {
		super(httpRequest);
		this.applicationContext = Activator.applicationContext;
		reportRender = applicationContext.getBean(ReportRender.class);
		reportParser = applicationContext.getBean(ReportParser.class);
		Collection<ReportProvider> providers=applicationContext.getBeansOfType(ReportProvider.class).values();
		for(ReportProvider provider:providers){
			if(provider.disabled() || provider.getName()==null){
				continue;
			}
			reportProviders.add(provider);
		}
	}

	public WebServerContent execute() {
		if (httpRequest.getURLPath().endsWith("/loadReport")) {
			return loadReport();
		} else if (httpRequest.getURLPath().endsWith("/savePreviewData")) {
			return savePreviewData();
		} else if (httpRequest.getURLPath().endsWith("/saveReportFile")) {
			return saveReportFile();
		}
		return null;
	}

	/**
	 * 加载报表文件
	 * 
	 * @param httpRequest
	 * @return
	 */
	private WebServerContent loadReport() {
		String pfile = httpRequest.getParameter("file");
		String file = ReportUtils.decodeFileName(pfile);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (file.startsWith("file:")) {
			String files = file.substring(5);
			File rfile = new File(files);
			if (rfile.exists() && rfile.isFile()) {
				try {
					ReportDefinition reportDef = reportParser.parse(new FileInputStream(rfile), rfile.getName());
					writeObjectToJson(out, new ReportDefinitionWrapper(reportDef));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new DefWebServerContent(out);
	}

	private WebServerContent savePreviewData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			String content = httpRequest.getParameter("content");
			content = decodeContent(content);
			InputStream inputStream = IOUtils.toInputStream(content, "utf-8");
			ReportDefinition reportDef = reportParser.parse(inputStream, "p");
			reportRender.rebuildReportDefinition(reportDef);
			IOUtils.closeQuietly(inputStream);
			TempObjectCache.putObject(PREVIEW_KEY, reportDef);
			writeObjectToJson(out, "{'status':'ok'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DefWebServerContent(out);
	}

	private WebServerContent saveReportFile() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String file = httpRequest.getParameter("file");
		file = ReportUtils.decodeFileName(file);
		String content = httpRequest.getParameter("content");
		content = decodeContent(content);
		System.out.println(content);
		ReportProvider targetReportProvider=null;
		for(ReportProvider provider:reportProviders){
			if(file.startsWith(provider.getPrefix())){
				targetReportProvider=provider;
				break;
			}
		}
		if(targetReportProvider==null){
			throw new ReportDesignException("File ["+file+"] not found available report provider.");
		}
		System.out.println(targetReportProvider);
		targetReportProvider.saveReport(file, content);
		try {
			writeObjectToJson(out, "{'status':'ok'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DefWebServerContent(out);
	}

}