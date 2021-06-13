package com.tulin.v8.core.Dao.SqlMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GetSqlMapByKey {
	private static Map keyMap = new HashMap();
	private static Map<String, Map<String, String>> keyDbDriverMap = new HashMap();

	public static void init() {
		keyMap = new HashMap();
		keyDbDriverMap = new HashMap();
		ReadmapFileList fileList = new ReadmapFileList();
		List<String> filePathList = fileList.getFileList();
		if (filePathList == null)
			return;
		for (int l = 0; l < filePathList.size(); l++) {
			try {
				String filepath = filePathList.get(l);
				File cfgfile = new File(filepath);
				String key = cfgfile.getName().replace(cfgfile.getName(), "");
				keyMap.put(key, cfgfile.getName());
				// SAXReader saxreader = new SAXReader();
				// Document doc = saxreader.read(cfgfile);
				FileInputStream fileiptstream = new FileInputStream(cfgfile);
				BufferedReader Strreader = new BufferedReader(new InputStreamReader(fileiptstream));
				StringBuffer fileText = new StringBuffer();
				String fileStr = "";
				while ((fileStr = Strreader.readLine()) != null) {
					fileText.append(fileStr);
				}
				fileiptstream.close();
				if (fileText.indexOf("<!DOCTYPE") > 0) {
					fileText.delete(fileText.indexOf("<!DOCTYPE"), fileText.indexOf("mybatis-3-config.dtd") + 22);
				}
				fileStr = fileText.toString();
				// Sys.printMsg(fileStr);
				Document doc = DocumentHelper.parseText(fileStr);
				Element environments = doc.getRootElement().element("environments");
				String dfdb = environments.attributeValue("default");
				List<Element> envs = environments.elements();
				for (int i = 0; i < envs.size(); i++) {
					Element env = envs.get(i);
					if (env.attributeValue("id").equals(dfdb)) {
						Element dataSource = env.element("dataSource");
						List propertys = dataSource.elements("property");
						Map m = new HashMap();
						// System.out.println(dataSource.asXML());
						for (int j = 0; j < propertys.size(); j++) {
							Element property = (Element) propertys.get(j);
							if (property.attributeValue("name").equalsIgnoreCase("driver")) {
								m.put("driver", property.attributeValue("value"));
							}
							if (property.attributeValue("name").equalsIgnoreCase("url")) {
								m.put("url", property.attributeValue("value"));
							}
							if (property.attributeValue("name").equalsIgnoreCase("username")) {
								m.put("username", property.attributeValue("value"));
							}
							if (property.attributeValue("name").equalsIgnoreCase("password")) {
								m.put("password", property.attributeValue("value"));
							}
						}
						String filePath = cfgfile.getAbsolutePath();
						filePath = filePath.replace("\\", "/");
						m.put("filePath", filePath);
						// System.out.println(m);
						try {
							keyDbDriverMap.put(dfdb, m);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String get(String key) {
		String result = "";
		init();
		if (keyMap == null || keyMap.isEmpty()) {
			result = (String) keyMap.get(key);
		} else {
			result = (String) keyMap.get(key);
		}
		if (result == null || "".equals(result))
			result = "system.mybatis.xml";
		return result;
	}

	public static void setKeyDbDriverMap(Map<String, Map<String, String>> keyDbDriverMap) {
		GetSqlMapByKey.keyDbDriverMap = keyDbDriverMap;
	}

	public static Map<String, Map<String, String>> getKeyDbDriverMap() {
		init();
		return keyDbDriverMap;
	}
}
