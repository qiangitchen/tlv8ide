package com.tulin.v8.fuzzyxml.util;

import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;

public class ElementFilter implements NodeFilter {
	
	private String name;
	
	public ElementFilter(){
		this(null);
	}
	
	public ElementFilter(String name){
		this.name = name;
	}
	
	public boolean filter(FuzzyXMLNode node) {
		if(node instanceof FuzzyXMLElement){
			FuzzyXMLElement element = (FuzzyXMLElement)node;
			if(name!=null && !name.equals(element.getName())){
				return false;
			}
			return true;
		}
		return false;
	}

}
