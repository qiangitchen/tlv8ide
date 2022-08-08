package com.tulin.v8.editors.page.server.echat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.editors.page.server.common.PageWebServer.HTTPRequest;

import chrriis.common.WebServerContent;

/**
 * echat文件翻译
 * 
 * @author chenqian
 *
 */
public class EChatExecuter {

	public static WebServerContent handleChart(InputStream resourceStream, HTTPRequest httpRequest) throws Exception {
		String html = FileAndString
				.FileToString(EChatExecuter.class.getResourceAsStream("/webdesign/echarts/chartsmodle.html"));
		Document doc = getChartFileDoc(resourceStream);
		ModleParse mpas = new ModleParse(doc, httpRequest);
		String theme = doc.getRootElement().element("chart").attributeValue("theme");
		html = html.replace("{{theme}}", theme);
		String chartoptions = "";
		if (mpas.toJSON().length() > 0) {
			chartoptions = mpas.toJSONString();
		} else {
			chartoptions = mpas.getChartText();
		}
		if ("".equals(chartoptions)) {
			chartoptions = "''";
		}
		html = html.replace("{{chartoptions}}", chartoptions);
		List<Element> scripts = doc.getRootElement().elements("script");
		StringBuilder scrts = new StringBuilder();
		for (int s = 0; s < scripts.size(); s++) {
			String scrt = scripts.get(s).getText();
			scrt = mpas.valueTranseStr(scrt);
			scrts.append(scrt);
		}
		final String htmls = html.replace("{{javascripts}}", scrts.toString());
		WebServerContent webServerContent = new WebServerContent() {
			@Override
			public String getContentType() {
				return "text/html";
			}

			@Override
			public InputStream getInputStream() {
				InputStream stream = new ByteArrayInputStream(htmls.getBytes(StandardCharsets.UTF_8));
				return stream;
			}

			@Override
			public long getContentLength() {
				return htmls.getBytes().length;
			}

			@Override
			public long getLastModified() {
				return new Date().getTime();
			}
		};
		return webServerContent;
	}

	static Document getChartFileDoc(InputStream resourceStream) throws Exception {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(resourceStream);
		return doc;
	}
}
