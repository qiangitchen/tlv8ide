package com.tulin.v8.ureport.server.word;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import chrriis.common.WebServerContent;

public class WordWebServerContent extends WebServerContent {
	ByteArrayOutputStream out = null;
	
	public WordWebServerContent(ByteArrayOutputStream out) {
		this.out = out;
	}

	@Override
	public String getContentType() {
		return "application/octet-stream;charset=ISO8859-1";
	}
	
	public String getContentDisposition() {
		return "attachment;filename=report.docx";
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
