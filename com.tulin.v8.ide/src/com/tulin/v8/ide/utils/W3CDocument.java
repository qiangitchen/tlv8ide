package com.tulin.v8.ide.utils;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

@SuppressWarnings("restriction")
public class W3CDocument {
	/**
	 * XML org.w3c.dom.Document 转 String
	 */
	public static String docToString(Document doc) {
		// XML转字符串
		String xmlStr = "";
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty("encoding", "UTF-8");// 解决中文问题，试过用GBK不行
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			t.transform(new DOMSource(doc), new StreamResult(bos));
			xmlStr = bos.toString();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return xmlStr;
	}

	/**
	 * String 转 XML org.w3c.dom.Document
	 */
	public static Document stringToDoc(String xmlStr) {
		// 字符串转XML
		Document doc = null;
		try {
			xmlStr = new String(xmlStr.getBytes(), "UTF-8");
			StringReader sr = new StringReader(xmlStr);
			InputSource is = new InputSource(sr);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return doc;
	}

	/**
	 * String 转 XML org.w3c.dom.Element
	 */
	public static Element string2Doc(String xmlStr) {
		final Document doc = stringToDoc(xmlStr);
		final Element ele = doc.getDocumentElement();
		final org.jsoup.nodes.Document html = Jsoup.parse(xmlStr);
		System.out.println(ele.toString());
		System.out.println(html.selectFirst(ele.getTagName()).outerHtml());
		Element element = new ElementStyleImpl() {
			@Override
			public String getTagName() {
				return ele.getTagName();
			}

			@Override
			public String getSource() {
				return html.selectFirst(ele.getTagName()).outerHtml();
			}
		};
		org.jsoup.nodes.Element hele = html.selectFirst(ele.getTagName());
		List<Attribute> atrl = hele.attributes().asList();
		for (Attribute a : atrl) {
			System.out.println(a);
			element.setAttribute(a.getKey(), a.getValue());
		}
		element.setNodeValue(hele.text());
		return element;
	}

}
