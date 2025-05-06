package com.tulin.v8.web.common;

import java.util.Enumeration;
import java.util.Map;

/**
 * http请求request接口
 * 
 * @author 陈乾
 *
 */
public interface Request {
	public void setURLPath(String urlPath);

	public String getURLPath();

	public void setResourcePath(String resourcePath);

	public String getResourcePath();

	public String getRequestURI();

	public String getHeader(String name);

	public Enumeration<?> getParameterNames();

	public String getParameter(String param);

	public Map<String, String> getParameterMap();

	public Request clone();
}
