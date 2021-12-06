package com.tulin.v8.ureport.server.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tulin.v8.ureport.server.common.UReportWebServer.WebServerContent;

public class PdfWebServerContent extends WebServerContent {
	ByteArrayOutputStream out = null;
	
	boolean forPrint;

	public PdfWebServerContent(ByteArrayOutputStream out,boolean forPrint) {
		this.out = out;
		this.forPrint = forPrint;
	}

	@Override
	public String getContentType() {
		if(forPrint){
			return "application/pdf";
		}
		return "application/octet-stream;charset=ISO8859-1";
	}
	
	public String getContentDisposition() {
		if(forPrint){
			return "inline;filename=report.pdf";
		}
		return "attachment;filename=report.pdf";
	}

	@Override
	public long getContentLength() {
		return out.size();
	}

	@Override
	public InputStream getInputStream() {
		ByteArrayInputStream ins = new ByteArrayInputStream(out.toByteArray());
		try {
			out.close();
		} catch (IOException e) {
		}
		return ins;
	}

}
