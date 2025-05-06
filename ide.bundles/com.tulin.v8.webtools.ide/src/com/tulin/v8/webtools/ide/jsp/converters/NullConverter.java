package com.tulin.v8.webtools.ide.jsp.converters;

import java.util.Map;

import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.webtools.ide.jsp.JSPInfo;

public class NullConverter extends AbstractCustomTagConverter {

	public String process(Map<String, String> attrs, FuzzyXMLNode[] children, JSPInfo info, boolean fixPath) {

		return evalBody(children, info, fixPath);
	}

}
