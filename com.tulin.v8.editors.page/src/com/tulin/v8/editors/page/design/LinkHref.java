package com.tulin.v8.editors.page.design;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkHref {

	public static void addLink(Element headItem, String fileName) {
		if (checkLink(headItem, fileName)) {
			Element link = headItem.appendElement("link");
			link.attr("rel", "STYLESHEET");
			link.attr("type", "text/css");
			link.attr("href", fileName);
		}
	}

	public static void addScript(Element headItem, String fileName) {
		if (checkScript(headItem, fileName)) {
			Element script = headItem.appendElement("script");
			script.attr("type", "text/javascript");
			script.attr("src", fileName);
		}
	}

	public static boolean checkScript(Element headItem, String fileName) {
		boolean rs = true;
		Elements scripts = headItem.getElementsByTag("script");
		for (int i = 0; i < scripts.size(); i++) {
			Element element = (Element) scripts.get(i);
			String name = element.attr("src");
			if (fileName.equals(name)) {
				return false;
			}
		}
		return rs;
	}

	public static boolean checkLink(Element headItem, String fileName) {
		boolean rs = true;
		Elements scripts = headItem.getElementsByTag("link");
		for (int i = 0; i < scripts.size(); i++) {
			Element element = (Element) scripts.get(i);
			String name = element.attr("href");
			if (fileName.equals(name)) {
				return false;
			}
		}
		return rs;
	}
}
