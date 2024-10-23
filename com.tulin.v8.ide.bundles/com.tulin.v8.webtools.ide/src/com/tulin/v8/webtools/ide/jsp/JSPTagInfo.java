package com.tulin.v8.webtools.ide.jsp;

import com.tulin.v8.webtools.ide.assist.TagInfo;

public class JSPTagInfo extends TagInfo {

	private boolean dynamicAttributes;

	public JSPTagInfo(String tagName, boolean hasBody) {
		super(tagName, hasBody);
	}

	public JSPTagInfo(String tagName, boolean hasBody, boolean emptyTag) {
		super(tagName, hasBody, emptyTag);
	}

	public boolean isDynamicAttributes() {
		return dynamicAttributes;
	}

	public void setDynamicAttributes(boolean dynamicAttributes) {
		this.dynamicAttributes = dynamicAttributes;
	}

	@Override
	public String getDisplayString() {
		if (dynamicAttributes) {
			return super.getDisplayString() + " (dynamic-attributes)";
		}
		return super.getDisplayString();
	}

}
