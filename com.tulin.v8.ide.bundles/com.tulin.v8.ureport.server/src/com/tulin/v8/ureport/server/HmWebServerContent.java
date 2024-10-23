package com.tulin.v8.ureport.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import chrriis.common.WebServerContent;

public class HmWebServerContent extends WebServerContent {
	ByteArrayOutputStream out = null;

	public HmWebServerContent(ByteArrayOutputStream out) {
		this.out = out;
	}

	@Override
	public String getContentType() {
		return "text/html";
	}

	@Override
	public long getContentLength() {
		return out.size();
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(out.toByteArray());
	}

	@Override
	public String getContentDisposition() {
		return null;
	}

}
