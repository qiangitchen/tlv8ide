package com.tulin.v8.ide.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IResource;

import com.tulin.v8.core.config.AppConfig;

public class DocServerData {
	public static String mvn_doc_db = "src/main/resources/jdbc.properties";
	public static String ant_doc_db = "WEB-INF/classes/jdbc.properties";

	public static IResource getDocDBSource() {
		IResource dsource = AppConfig.getProject("DocServer").findMember(mvn_doc_db);
		if (dsource == null) {
			dsource = AppConfig.getProject("DocServer").findMember(ant_doc_db);
		}
		return dsource;
	}

	public static Map<String, String> getConfig() throws Exception {
		Map<String, String> m = new HashMap<String, String>();
		IResource dsource = getDocDBSource();
		InputStream in = new BufferedInputStream(new FileInputStream(dsource.getLocation().toString()));
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
			IResource dsource = getDocDBSource();
			String file = dsource.getLocation().toString();
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			Properties p = new Properties();
			p.load(in);
			try {
				url = url.replace("&amp;", "&");
			} catch (Exception e) {
			}
			p.setProperty("driver", driverStr);
			p.setProperty("url", url);
			p.put("url", url);
			p.setProperty("username", userName);
			p.setProperty("password", password);
			p.store(new FileOutputStream(file), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
