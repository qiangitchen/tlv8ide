package com.tulin.v8.ide.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GetTomcatconfig {

	public static String getConfigFilePath() {
		// String filePathDir = TomcatConfigInit.getTomcatDefaultDir();
		String filePathDir = StudioPlugin.getDefault().getConfigFile();
		// filePathDir += "/conf/server.xml";
		return filePathDir;
	}

	public static Map<String, String> getDocConfig() {
		Document doc = DocumentHelper.createDocument();
		Element docRoot = DocumentHelper.createElement("root");
		List<Node> fileElementList = new ArrayList<Node>();
		String filePath = getConfigFilePath();
		List tempList = generateNewByFile(filePath);

		fileElementList.addAll(tempList);
		docRoot.clearContent();
		docRoot.setContent(fileElementList);
		doc.add(docRoot);
		Element root = doc.getRootElement();
		List Itorate = root.elements();
		for (int i = 0; i < Itorate.size(); i++) {
			String Context = ((Element) Itorate.get(i)).getName();
			Element transactionManager = (Element) Itorate.get(i);
			if (Context.equals("Context") && transactionManager.attributeValue("docBase").endsWith("DocServer")) {
				Element dataSource = (Element) (transactionManager.elements()).get(0);
				Map<String, String> m = new HashMap<String, String>(); //
				m.put("driver", dataSource.attributeValue("driverClassName"));
				m.put("url", dataSource.attributeValue("url"));
				m.put("username", dataSource.attributeValue("username"));
				m.put("password", dataSource.attributeValue("password"));

				return m;
			}
		}
		return null;
	}

	public static List generateNewByFile(String filePath) {
		ArrayList elementList = new ArrayList();
		try {
			String fileStr = FileAndString.FileToString(filePath);
			Document doc = DocumentHelper.parseText(fileStr);
			Element root = doc.getRootElement();
			root = root.element("Service");
			root = root.element("Engine");
			root = root.element("Host");
			List hosts = root.elements();
			return hosts;
		} catch (Exception e1) {
			// e1.printStackTrace();
			Sys.packErrMsg(StudioPlugin.getResourceString("tomcat.config.messages.1") + "e：" + e1.toString());
		}
		return elementList;
	}

}
