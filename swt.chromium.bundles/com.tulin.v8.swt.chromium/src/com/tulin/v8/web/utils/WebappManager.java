package com.tulin.v8.web.utils;

import java.io.File;

import com.tulin.v8.web.common.WebServer;

/**
 * 本地web服务管理器
 * 
 * @author chenqian
 */
public class WebappManager {
	public static String getHost() {
		return WebServer.getDefaultWebServer().getURLPrefix();
	}

	public static int getPort() {
		return WebServer.getDefaultWebServer().getPort();
	}

	/**
	 * 获取项目资源的跟地址
	 * 
	 * @return
	 */
	public static String getSourceURL() {
		return WebServer.getDefaultWebServer().getClassPathResourceURL("", "");
	}

	/**
	 * 获取项目中模板文件夹的的地址
	 * 
	 * @return
	 */
	public static String getTemplateURL() {
		return WebServer.getDefaultWebServer().getClassPathResourceURL("", "template");
	}

	/**
	 * 获取项目资源下制定位置的资源地址
	 * 
	 * @param path
	 * @return
	 */
	public static String getPathURL(String path) {
		return getSourceURL() + path;
	}
	
	/**
	 * 获取template下的资源地址
	 * 
	 * @param path
	 * @return
	 */
	public static String getTemplatePathURL(String path) {
		return getTemplateURL() + path;
	}

	/**
	 * 将本地磁盘中的文件转换成可以访问的地址
	 * 
	 * @param filePath
	 * @return
	 */
	public static String filePathToURL(String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				return WebServer.getDefaultWebServer().getResourcePathURL(file.getParentFile().getAbsolutePath(),
						file.getName());
			}
		} catch (Exception e) {
		}
		return filePath;
	}

}
