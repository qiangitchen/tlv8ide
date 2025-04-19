package com.tulin.v8.web.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * web服务返回内容封装
 * 
 * @author 陈乾
 *
 */
public abstract class WebServerContent {

	private static final String MIME_APPLICATION_OCTET_STREAM = "application/octet-stream";

	public static String getDefaultMimeType(String extension) {
		String mimeType = MimeTypes.getMimeType(extension);
		return mimeType == null ? MIME_APPLICATION_OCTET_STREAM : mimeType;
	}

	public abstract InputStream getInputStream();

	public static InputStream getInputStream(String content) {
		if (content == null) {
			return null;
		}
		try {
			return new ByteArrayInputStream(content.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getContentType() {
		return getDefaultMimeType(".html");
	}

	public long getContentLength() {
		return -1;
	}

	public long getLastModified() {
		return System.currentTimeMillis();
	}

	public String getContentDisposition() {
		return null;
	}
}
