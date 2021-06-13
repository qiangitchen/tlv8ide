package com.tulin.v8.ide.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tulin.v8.core.config.AppConfig;

public class DocServerData {
	public static Map<String, String> getConfig() throws Exception {
		Map<String, String> m = new HashMap<String, String>();
		InputStream in = new BufferedInputStream(new FileInputStream(AppConfig.getProject("DocServer")
				.findMember("WEB-INF/classes/jdbc.properties").getLocation().toString()));
		Properties p = new Properties();
		p.load(in);
		m.put("driver", p.getProperty("driver"));
		m.put("url", p.getProperty("url"));
		m.put("username", p.getProperty("username"));
		m.put("password", p.getProperty("password"));
		return m;
	}

	public static void writeConfig(String name, String driverStr, String url, String userName, String password) {
		try {
			String file = AppConfig.getProject("DocServer").findMember("WEB-INF/classes/jdbc.properties").getLocation()
					.toString();
			String sfile = AppConfig.getProject("DocServer").findMember("src/jdbc.properties").getLocation().toString();
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			Properties p = new Properties();
			p.load(in);
			try {
				url = url.replace("&amp;", "&");
			}catch (Exception e) {
			}
			p.setProperty("driver", driverStr);
			p.setProperty("url", url);
			p.put("url", url);
			p.setProperty("username", userName);
			p.setProperty("password", password);
			p.store(new FileOutputStream(file), null);
			p.store(new FileOutputStream(sfile), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
