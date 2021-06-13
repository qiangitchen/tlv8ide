package com.tulin.v8.ureport.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.tulin.v8.ureport.server.common.UReportWebServer.WebServerContent;

public class DefWebServerContent extends WebServerContent {
	ByteArrayOutputStream out = null;

	public DefWebServerContent(ByteArrayOutputStream out) {
		this.out = out;
	}

	@Override
	public String getContentType() {
		return "text/json";
	}

	@Override
	public long getContentLength() {
		return out.size();
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(out.toByteArray());
	}

}
