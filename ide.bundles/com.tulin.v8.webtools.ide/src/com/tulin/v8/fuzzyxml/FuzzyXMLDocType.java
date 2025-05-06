package com.tulin.v8.fuzzyxml;

public interface FuzzyXMLDocType extends FuzzyXMLNode {

	public String getName();
	
	public String getPublicId();
	
	public String getSystemId();
	
	public String getInternalSubset();
}
