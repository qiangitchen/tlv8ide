package com.tulin.v8.ureport.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import chrriis.common.Request;
import chrriis.common.WebServerContent;

public abstract class RenderPageServletAction {
	protected VelocityEngine ve;
	public static final String PREVIEW_KEY = "p";

	protected Request httpRequest;

	public RenderPageServletAction(Request httpRequest) {
		this.httpRequest = httpRequest;
		ve = new VelocityEngine();
		ve.setProperty(Velocity.RESOURCE_LOADER, "class");
		ve.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,new NullLogChute());
		ve.init();
	}

	public abstract WebServerContent execute();

	protected void writeObjectToJson(ByteArrayOutputStream out, Object obj) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		try {
			mapper.writeValue(out, obj);
		} finally {
			out.flush();
			out.close();
		}
	}

	protected String decode(String value) {
		if (value == null) {
			return value;
		}
		try {
			value = URLDecoder.decode(value, "utf-8");
			value = URLDecoder.decode(value, "utf-8");
			return value;
		} catch (Exception ex) {
			return value;
		}
	}

	protected String decodeContent(String content) {
		if (content == null) {
			return content;
		}
		try {
			content = URLDecoder.decode(content, "utf-8");
			return content;
		} catch (Exception ex) {
			return content;
		}
	}

	protected Throwable buildRootException(Throwable throwable) {
		if (throwable.getCause() == null) {
			return throwable;
		}
		return buildRootException(throwable.getCause());
	}

	protected Map<String, Object> buildParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Enumeration<?> enumeration = httpRequest.getParameterNames();
		while (enumeration.hasMoreElements()) {
			Object obj = enumeration.nextElement();
			if (obj == null) {
				continue;
			}
			String name = obj.toString();
			String value = httpRequest.getParameter(name);
			if (name == null || value == null || name.startsWith("_")) {
				continue;
			}
			parameters.put(name, decode(value));
		}
		return parameters;
	}

	protected WebServerContent invokeMethod(String methodName) {
		try {
			Method method = this.getClass().getMethod(methodName);
			return (WebServerContent) method.invoke(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
