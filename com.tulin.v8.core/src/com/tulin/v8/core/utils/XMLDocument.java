package com.tulin.v8.core.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.tulin.v8.core.FileAndString;

public class XMLDocument {
	// 字符串与DOM转换
	public static Document str2dom(String domStr) throws Exception {
		String xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		domStr = transeDomstr(domStr);
		if (domStr.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") < 0) {
			domStr = xmlstr + domStr;
		}
		Document dom = null;
		dom = DocumentHelper.parseText(domStr);
		return dom;
	}

	/*
	 * 将html文件转换为xmlDom
	 */
	public static Document htmlFtoxmlDom(File file) throws Exception {
		String domStr = FileAndString.file2String(file, "UTF-8");
		Document dom = null;
		try {
			domStr = transeDomstr(domStr);
			dom = DocumentHelper.parseText(domStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dom;
	}

	public static String transeDomstr(String domStr) {
		domStr = domStr.replaceAll("\n", "");
		domStr = domStr.replaceAll("<!DOCTYPE[\\s\\S]*?>", "");
		domStr = domStr.replaceAll("<meta name=\"generator\"[\\s\\S]*?>", "");
		domStr = domStr.replaceAll("<%@[\\s\\S]*?%>", "");
		String regstr = "<meta[\\s\\S]*?>";
		Pattern p = Pattern.compile(regstr);
		Matcher m = p.matcher(domStr);
		while (m.find()) {
			String ps = m.group();
			if (ps.indexOf("/>") < 0 && domStr.indexOf("</meta>") < 0) {
				domStr = domStr.replace(ps, ps.replace(">", "/>"));
			}
		}
		String regstr1 = "<link[\\s\\S]*?>";
		Pattern p1 = Pattern.compile(regstr1);
		Matcher m1 = p1.matcher(domStr);
		while (m1.find()) {
			String ps = m1.group();
			if (ps.indexOf("/>") < 0 && domStr.indexOf("</link>") < 0) {
				domStr = domStr.replace(ps, ps.replace(">", "/>"));
			}
		}
		String regstr2 = "<script[\\s\\S]*?>";
		Pattern p2 = Pattern.compile(regstr2);
		Matcher m2 = p2.matcher(domStr);
		while (m2.find()) {
			String ps = m2.group();
			String scripttext = domStr.substring(domStr.indexOf(ps) + ps.length());
			if (ps.indexOf("/>") < 0 && scripttext.indexOf("</script>") > 0) {
				String fntext = scripttext.substring(0, scripttext.indexOf("</script>"));
				if (fntext.trim().length() > 0 && fntext.indexOf("<!--") < 0) {
					domStr = domStr.replace(fntext, "<!--" + fntext + "-->");
				}
			}
		}
		String regstr3 = "<input[\\s\\S]*?>";
		Pattern p3 = Pattern.compile(regstr3);
		Matcher m3 = p3.matcher(domStr);
		while (m3.find()) {
			String ps3 = m3.group();
			String text3 = domStr.substring(domStr.indexOf(ps3) + ps3.length());
			if (ps3.indexOf("/>") < 0 && text3.indexOf("</input>") < 0) {
				domStr = domStr.replace(ps3, ps3.replace(">", "/>"));
			}
		}
		String regstr4 = "<INPUT[\\s\\S]*?>";
		Pattern p4 = Pattern.compile(regstr4);
		Matcher m4 = p4.matcher(domStr);
		while (m4.find()) {
			String ps4 = m4.group();
			String text4 = domStr.substring(domStr.indexOf(ps4) + ps4.length());
			if (ps4.indexOf("/>") < 0 && text4.indexOf("</INPUT>") < 0) {
				domStr = domStr.replace(ps4, ps4.replace(">", "/>"));
			}
		}
		domStr = domStr.replace("s:", "");
		return domStr;
	}

}
