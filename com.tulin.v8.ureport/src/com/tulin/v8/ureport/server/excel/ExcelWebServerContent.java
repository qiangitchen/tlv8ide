package com.tulin.v8.ureport.server.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tulin.v8.ureport.server.common.UReportWebServer.WebServerContent;

public class ExcelWebServerContent extends WebServerContent {
	ByteArrayOutputStream out = null;
	
	public ExcelWebServerContent(ByteArrayOutputStream out) {
		this.out = out;
	}

	@Override
	public String getContentType() {
		return "application/octet-stream;charset=ISO8859-1";
	}
	
	public String getContentDisposition() {
		return "attachment;filename=report.xlsx";
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
