package com.tulin.v8.ureport.server.pdf;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;

import com.bstek.ureport.build.ReportBuilder;
import com.bstek.ureport.console.cache.TempObjectCache;
import com.bstek.ureport.console.exception.ReportDesignException;
import com.bstek.ureport.definition.Paper;
import com.bstek.ureport.definition.ReportDefinition;
import com.bstek.ureport.exception.ReportComputeException;
import com.bstek.ureport.export.ExportConfigure;
import com.bstek.ureport.export.ExportConfigureImpl;
import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.ReportRender;
import com.bstek.ureport.export.pdf.PdfProducer;
import com.bstek.ureport.model.Report;
import com.tulin.v8.ureport.server.Activator;
import com.tulin.v8.ureport.server.RenderPageServletAction;
import com.tulin.v8.ureport.server.common.UReportWebServer.HTTPRequest;

import chrriis.common.WebServerContent;

public class ExportPdfServletAction extends RenderPageServletAction{
	private ReportBuilder reportBuilder;
	private ExportManager exportManager;
	private ReportRender reportRender;
	private PdfProducer pdfProducer=new PdfProducer();
	
	ApplicationContext applicationContext;
	
	public ExportPdfServletAction(HTTPRequest httpRequest) {
		super(httpRequest);
		this.applicationContext = Activator.applicationContext;
		exportManager = applicationContext.getBean(ExportManager.class);
		reportBuilder = applicationContext.getBean(ReportBuilder.class);
		reportRender = applicationContext.getBean(ReportRender.class);
	}
	
	@Override
	public WebServerContent execute() {
		String method=retriveMethod();
		if(method!=null){
			return invokeMethod(method);
		}else{			
			return buildPdf(false);
		}
	}
	
	protected String retriveMethod(){
		String path="/ureport/pdf";
		String uri=httpRequest.getRequestURI();
		String targetUrl=uri.substring(path.length());
		int slashPos=targetUrl.indexOf("/");
		if(slashPos>-1){
			String methodName=targetUrl.substring(slashPos+1).trim();
			return methodName.length()>0 ? methodName : null;
		}
		return null;
	}
	
	public WebServerContent show() {
		return buildPdf(true);
	}
	
	public WebServerContent buildPdf(boolean forPrint) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String file=httpRequest.getParameter("_u");
		file=decode(file);
		if(StringUtils.isBlank(file)){
			throw new ReportComputeException("Report file can not be null.");
		}
		try {
			Map<String, Object> parameters = buildParameters();
			if(file.equals(PREVIEW_KEY)){
				ReportDefinition reportDefinition=(ReportDefinition)TempObjectCache.getObject(PREVIEW_KEY);
				if(reportDefinition==null){
					throw new ReportDesignException("Report data has expired,can not do export pdf.");
				}
				Report report=reportBuilder.buildReport(reportDefinition, parameters);	
				pdfProducer.produce(report, out);
			}else{
				ExportConfigure configure=new ExportConfigureImpl(file,parameters,out);
				exportManager.exportPdf(configure);
			}			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return new PdfWebServerContent(out,forPrint);
	}
	
	public void newPaging() {
		String file=httpRequest.getParameter("_u");
		if(StringUtils.isBlank(file)){
			throw new ReportComputeException("Report file can not be null.");
		}
		Report report=null;
		Map<String, Object> parameters = buildParameters();
		if(file.equals(PREVIEW_KEY)){
			ReportDefinition reportDefinition=(ReportDefinition)TempObjectCache.getObject(PREVIEW_KEY);
			if(reportDefinition==null){
				throw new ReportDesignException("Report data has expired,can not do export pdf.");
			}
			report=reportBuilder.buildReport(reportDefinition, parameters);	
		}else{
			ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
			report=reportRender.render(reportDefinition, parameters);
		}
		String paper=httpRequest.getParameter("_paper");
		ObjectMapper mapper=new ObjectMapper();
		try {
			Paper newPaper = mapper.readValue(paper, Paper.class);
			report.rePaging(newPaper);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
			

}
