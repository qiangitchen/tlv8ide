package com.tulin.v8.webtools.ide.jsp.editors;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IFileEditorInput;

import com.tulin.v8.fuzzyxml.FuzzyXMLAttribute;
import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.webtools.ide.ProjectParams;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.jsp.JSPInfo;
import com.tulin.v8.webtools.ide.tag.ICustomTagConverter;
import com.tulin.v8.webtools.ide.tag.ICustomTagConverterContributer;

/**
 * This class provides some utility methods to convert JSP to HTML for preview.
 */
public class JSPPreviewConverter {

	private static Pattern petternScript = Pattern.compile("<%(.*?)%>", Pattern.DOTALL);
	private static Pattern petternTagBegin = Pattern.compile("<(|/)((\\w+?):(.*?))>");

	/**
	 * Converts JSP to HTML for preview.
	 * 
	 * @param input the editor input
	 * @param jsp   JSP source code
	 * @return converted HTML
	 */
	public static String convertJSP(IFileEditorInput input, String jsp) {
		// get JSPInfo
		jsp = HTMLUtil.comment2space(jsp, false);
		JSPInfo info = JSPInfo.getJSPInfo(input.getFile(), jsp);

		// get the web application root folder
		String root = "";
		boolean fixPath = WebToolsPlugin.getDefault().getPreferenceStore().getBoolean(WebToolsPlugin.PREF_JSP_FIX_PATH);

		try {
			root = new ProjectParams(input.getFile().getProject()).getRoot();
			if (!root.startsWith("/")) {
				root = "/" + root;
			}
			if (!root.endsWith("/")) {
				root = root + "/";
			}
			root = input.getFile().getProject().getLocation().makeAbsolute().toString() + root;
		} catch (Exception ex) {
			root = "";
		}

		// remove JSP comments
		// jsp = jsp.replaceAll("<%--(.|\n|\r)*?--%>","");
		jsp = HTMLUtil.comment2space(jsp, false);

		// split by <body>
		String lower = jsp.toLowerCase();
		int index = lower.indexOf("<body");

		if (index == -1) {
			String charset = null;
			try {
				charset = input.getFile().getCharset();
			} catch (CoreException ex) {
				charset = System.getProperty("file.encoding");
			}
			jsp = "<html><head>";
			jsp += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\"/>";
			jsp += "</head><body>" + jsp + "</body></html>";
			lower = jsp;
			index = lower.indexOf("<body");
		}
		int end = lower.indexOf("</body>");
		if (end == -1) {
			end = jsp.length();
		} else {
			end = end + 7;
		}

		String head = jsp.substring(0, index);
		String body = jsp.substring(index, end);

		// before <body>
		head = petternScript.matcher(head).replaceAll("");
		head = petternTagBegin.matcher(head).replaceAll("");
		// insert <base> into <head>
		if (fixPath) {
			if (root != null && root.length() != 0) {
				head = head.replaceAll("<[hH][eE][aA][dD]>", "$0<base href=\"" + root + "\" target=\"_self\">");
			}
		}
		head = head.replaceFirst("<[hH][tT][mM][lL].*?>", "");
		head = processTag(head, info, fixPath);

		// after <body>
		body = processScript(body);
		body = processTag(body, info, fixPath);
		body = body.replaceAll("&amp;nbsp;", "&nbsp;");
		body = body.replaceAll("&apos;", "'");

		return "<html>" + head + body + "</html>";
	}

	/**
	 * Process scriptlet.
	 * 
	 * @param jsp JSP source code
	 * @return converted JSP source code
	 */
	private static String processScript(String jsp) {
		StringBuffer sb = new StringBuffer();
		Matcher matcher = petternScript.matcher(jsp);
		int index = 0;
		while (matcher.find()) {
			sb.append(jsp.substring(index, matcher.start()));
			sb.append(HTMLUtil.escapeHTML(matcher.group(0)));
			index = matcher.end();
		}
		if (index < jsp.length() - 1) {
			sb.append(jsp.substring(index));
		}
		return sb.toString();
	}

	/**
	 * This method should be private, but it's required to be public...
	 * 
	 * @param element
	 * @param info
	 * @return
	 */
	public static String processElement(FuzzyXMLElement element, JSPInfo info, boolean fixPath) {
		StringBuffer sb = new StringBuffer();

		if (element.getName().indexOf(":") != -1) {
			String tagName = element.getName();
			String[] dim = tagName.split(":");

			// get URI from prefix
			String uri = info.getTaglibUri(dim[0]);
			// get converter
			ICustomTagConverter converter = null;
			if (uri != null) {
				ICustomTagConverterContributer contributer = WebToolsPlugin.getDefault().getCustomTagContributer(uri);
				if (contributer != null) {
					converter = contributer.getConverter(dim[1]);
				}
			}
			if (converter != null) {
				Map<String, String> attrMap = new HashMap<String, String>();
				FuzzyXMLAttribute[] attrs = element.getAttributes();
				for (int i = 0; i < attrs.length; i++) {
					attrMap.put(attrs[i].getName(), attrs[i].getValue());
				}
				sb.append(converter.process(attrMap, element.getChildren(), info, fixPath));
				return sb.toString();
			}
		}

		// rewrite absolute path of external files
		if (fixPath) {
			processHref(element, "img", "src");
			processHref(element, "input", "src");
			processHref(element, "link", "href");
			processHref(element, "script", "src");
		}

		if (element.getChildren().length == 0) {
			sb.append(element.toXMLString());
		} else {
			sb.append(element2startTag(element));
			FuzzyXMLNode[] node = element.getChildren();
			for (int i = 0; i < node.length; i++) {
				if (node[i] instanceof FuzzyXMLElement) {
					sb.append(processElement((FuzzyXMLElement) node[i], info, fixPath));
				} else {
					sb.append(node[i].toXMLString());
				}
			}
			sb.append(element2closeTag(element));
		}

		return sb.toString();
	}

	private static void processHref(FuzzyXMLElement element, String elementName, String attrName) {
		if (element.getName().toLowerCase().equals(elementName)) {
			for (FuzzyXMLAttribute attr : element.getAttributes()) {
				if (attr.getName().toLowerCase().equals(attrName)) {
					String href = attr.getValue();
					if (href != null) {
						if (href.startsWith("/")) {
							href = href.replaceFirst("/.+?/", "");
							attr.setValue(href);
							break;
						}
					}
				}
			}
		}
	}

	private static String element2startTag(FuzzyXMLElement e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<" + e.getName());
		FuzzyXMLAttribute[] attr = e.getAttributes();
		for (int i = 0; i < attr.length; i++) {
			sb.append(" " + attr[i].getName() + "=\"" + attr[i].getValue() + "\"");
		}
		sb.append(">");
		return sb.toString();
	}

	private static String element2closeTag(FuzzyXMLElement e) {
		return "</" + e.getName() + ">";
	}

	/**
	 * Process taglibs.
	 * 
	 * @param jsp  JSP
	 * @param info JSPInfo
	 * @return processed JSP
	 */
	private static String processTag(String jsp, JSPInfo info, boolean fixPath) {
		FuzzyXMLDocument doc = new FuzzyXMLParser().parse(jsp);
		FuzzyXMLNode[] nodes = doc.getDocumentElement().getChildren();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof FuzzyXMLElement) {
				sb.append(processElement((FuzzyXMLElement) nodes[i], info, fixPath));
			}
		}
		return sb.toString();
	}

}
