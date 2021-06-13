package com.tulin.v8.core;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XMLFormator {
	public static String formatXML(Document doc) {
		if (doc == null) {
			return "";
		}
		StringWriter out = null;
		try {
			/** 格式化输出,类型IE浏览一样 */
			OutputFormat formate = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			formate.setEncoding("UTF-8");
			formate.setExpandEmptyElements(true);
			out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, formate);
			writer.write(doc);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toString();
	}

	public static String formatXMLStr(String text) {
		boolean isall = true;
		if (text.indexOf("<?xml") < 0) {
			isall = false;
			// 将xml部分代码补全
			text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><formatroot>"
					+ text + "</formatroot>";
		}
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(text);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		String result = "";
		if (isall) {
			result = formatXML(doc);
		} else {
			result = doc.getRootElement().asXML();
			result = result.replace("<formatroot>", "").replace(
					"</formatroot>", "");
		}
		return result;
	}
}
