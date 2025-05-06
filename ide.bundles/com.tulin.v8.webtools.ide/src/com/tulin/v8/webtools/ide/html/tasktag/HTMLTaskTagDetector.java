package com.tulin.v8.webtools.ide.html.tasktag;

import com.tulin.v8.fuzzyxml.FuzzyXMLComment;
import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.fuzzyxml.FuzzyXMLText;

/**
 * {@link ITaskTagDetector} implementation for HTML / XML / JSP. This detector
 * supports following extensions:
 * 
 * <ul>
 * <li>.html</li>
 * <li>.htm</li>
 * <li>.xml</li>
 * <li>.tld</li>
 * <li>.xsd</li>
 * <li>.jsp</li>
 * <li>.jspf</li>
 * <li>.jspx</li>
 * </ul>
 */
public class HTMLTaskTagDetector extends AbstractTaskTagDetector {

	public HTMLTaskTagDetector() {
		addSupportedExtension("html");
		addSupportedExtension("htm");
		addSupportedExtension("xml");
		addSupportedExtension("tld");
		addSupportedExtension("xsd");
		addSupportedExtension("jsp");
		addSupportedExtension("jspf");
		addSupportedExtension("jspx");
	}

	public void doDetect() throws Exception {
		FuzzyXMLDocument doc = new FuzzyXMLParser().parse(this.contents);
		processElement(doc.getDocumentElement());
	}

	private void processElement(FuzzyXMLElement element) {
		FuzzyXMLNode[] children = element.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof FuzzyXMLElement) {
				processElement((FuzzyXMLElement) children[i]);

			} else if (children[i] instanceof FuzzyXMLComment) {
				// for HTML/XML comment
				detectTaskTag(((FuzzyXMLComment) children[i]).getValue(), children[i].getOffset());

			} else if (children[i] instanceof FuzzyXMLText) {
				// for JSP comment
				String value = ((FuzzyXMLText) children[i]).getValue();
				if (value.startsWith("<%--")) {
					if (value.endsWith("--%>")) {
						value = value.substring(0, value.length() - 4);
					}
					detectTaskTag(value, children[i].getOffset());
				}
			}
			// TODO Should support tags in Java comment
		}
	}

}
