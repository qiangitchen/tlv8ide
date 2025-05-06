package com.tulin.v8.webtools.ide.tag;

import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.webtools.ide.assist.AssistInfo;
import com.tulin.v8.webtools.ide.assist.AttributeInfo;

/**
 * An interface to extend taglib completion in the JSP editor.
 */
public interface ICustomTagAttributeAssist {

	/**
	 * Returns an array of completion proposals. If this class can't process
	 * arguments, returns null.
	 * 
	 * @param tagName  tag name (without prefix)
	 * @param uri      taglib URI
	 * @param value    input value
	 * @param attrInfo attribute information
	 * @param element  the current element
	 * @return completion proposals
	 */
	public AssistInfo[] getAttributeValues(String tagName, String uri, String value, AttributeInfo attrInfo,
			FuzzyXMLElement element);
}
