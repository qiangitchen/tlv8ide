package com.tulin.v8.fuzzyxml;


public interface FuzzyXMLNode {
	
//	public String getNamespaceURI();
//	
//	public String getPrefix();
	
	public FuzzyXMLNode getParentNode();
	
	public int getOffset();
	
	public int getLength();
	
	public String toXMLString();
	
}
