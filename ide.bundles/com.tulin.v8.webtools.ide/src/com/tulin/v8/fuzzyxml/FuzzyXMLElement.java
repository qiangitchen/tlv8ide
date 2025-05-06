package com.tulin.v8.fuzzyxml;


public interface FuzzyXMLElement extends FuzzyXMLNode {
	
	public String getName();
	
	
	public FuzzyXMLNode[] getChildren();
	
	public boolean hasChildren();
	
	public void appendChild(FuzzyXMLNode node);
	
	public void insertBefore(FuzzyXMLNode newChild,FuzzyXMLNode refChild);
	
	public void insertAfter(FuzzyXMLNode newChild, FuzzyXMLNode refChild);
	
	public void replaceChild(FuzzyXMLNode newChild,FuzzyXMLNode refChild);
	
	public void removeChild(FuzzyXMLNode oldChild);
	
	
	public FuzzyXMLAttribute[] getAttributes();
	
	public void setAttribute(FuzzyXMLAttribute attr);
	
	public boolean hasAttribute(String name);
	
	public FuzzyXMLAttribute getAttributeNode(String name);
	
	public String getAttributeValue(String name);
	
	public void removeAttributeNode(FuzzyXMLAttribute attr);
	
	public void setAttribute(String name, String value);
	
	public void removeAttribute(String name);
	
	public String getValue();
	
	public void removeAllChildren();
	
}
