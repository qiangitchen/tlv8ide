package com.tulin.v8.webtools.ide.jsp.converters;

import java.util.Iterator;
import java.util.Map;

import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.webtools.ide.jsp.JSPInfo;

public class HTMLConverter extends AbstractCustomTagConverter {
	
	private String tagName = null;
	
	public HTMLConverter(String tagName){
		this.tagName = tagName;
	}
	
	protected String convertStartTag(Map<String, String> attributes) {
		return "<" + createTag(tagName,attributes) + ">";
	}
	
	protected String convertEndTag() {
		if(tagName.indexOf(" ")!=-1){
			return "</" + tagName.substring(0,tagName.indexOf(" ")) + ">";
		} else {
			return "</" + tagName + ">";
		}
	}
	
	protected String createTag(String tagName,Map<String, String> attributes){
		StringBuffer sb = new StringBuffer();
		sb.append(tagName);
		
		Iterator<String> ite = attributes.keySet().iterator();
		while(ite.hasNext()){
			String key = ite.next();
			sb.append(" " + key + "=\"" + attributes.get(key) + "\"");
		}
		
		return sb.toString();
	}
	
	public String process(Map<String, String> attrs, 
			FuzzyXMLNode[] children, JSPInfo info, boolean fixPath) {
		StringBuffer sb = new StringBuffer();
		sb.append(convertStartTag(attrs));
		sb.append(evalBody(children, info, fixPath));
		sb.append(convertEndTag());
		return sb.toString();
	}
}
