package com.tulin.v8.fuzzyxml;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;

import com.tulin.v8.fuzzyxml.xpath.FuzzyXMLNodePointerFactory;

public class XPath {

	static {
		JXPathContextReferenceImpl.addNodePointerFactory(new FuzzyXMLNodePointerFactory());
	}

	public static FuzzyXMLNode selectSingleNode(FuzzyXMLElement element, String xpath) {
		JXPathContext ctx = JXPathContext.newContext(element);
		return (FuzzyXMLNode) ctx.selectSingleNode(xpath);
	}

	public static FuzzyXMLNode[] selectNodes(FuzzyXMLElement element, String xpath) {
		JXPathContext ctx = JXPathContext.newContext(element);
		List<?> list = ctx.selectNodes(xpath);
		return (FuzzyXMLNode[]) list.toArray(new FuzzyXMLNode[list.size()]);
	}

	public static Object getValue(FuzzyXMLElement element, String xpath) {
		JXPathContext ctx = JXPathContext.newContext(element);
		return ctx.getValue(xpath);
	}
}
