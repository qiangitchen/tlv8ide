package com.tulin.v8.ide.ui.editors.page.design;

import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("restriction")
public class ElementLinkHref {

	public static void addScript(Node headItem, String fileName) {
		if (checkScript(headItem, fileName)) {
			ElementStyleImpl newnode = new ElementStyleImpl(){
				@Override
				public String getTagName() {
					return "script";
				}
			};
			headItem.appendChild(newnode);
			newnode.setAttribute("type", "text/javascript");
			newnode.setAttribute("src", fileName);
		}
	}

	public static void addLink(Node headItem, String fileName) {
		if (checkLink(headItem, fileName)) {
			ElementStyleImpl newnode = new ElementStyleImpl(){
				@Override
				public String getTagName() {
					return "link";
				}
			};
			headItem.appendChild(newnode);
			newnode.setAttribute("type", "text/css");
			newnode.setAttribute("href", fileName);
		}
	}

	public static boolean checkScript(Node headItem, String fileName) {
		boolean rs = true;
		NodeList scripts = headItem.getChildNodes();
		for (int i = 0; i < scripts.getLength(); i++) {
			Node element = scripts.item(i);
			if ("script".equals(element.getNodeName())) {
				NamedNodeMap ss = element.getAttributes();
				String name = ss.getNamedItem("src").getNodeValue();
				if (fileName.equals(name)) {
					return false;
				}
			}
		}
		return rs;
	}
	
	public static boolean checkLink(Node headItem, String fileName) {
		boolean rs = true;
		NodeList scripts = headItem.getChildNodes();
		for (int i = 0; i < scripts.getLength(); i++) {
			Node element = scripts.item(i);
			if ("link".equals(element.getNodeName())) {
				NamedNodeMap ss = element.getAttributes();
				String name = ss.getNamedItem("href").getNodeValue();
				if (fileName.equals(name)) {
					return false;
				}
			}
		}
		return rs;
	}
}
