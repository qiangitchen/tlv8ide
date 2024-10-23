package com.tulin.v8.fuzzyxml.internal;

import com.tulin.v8.fuzzyxml.FuzzyXMLCDATA;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;


public class FuzzyXMLCDATAImpl extends AbstractFuzzyXMLNode implements FuzzyXMLCDATA {
    
    private String value;
    
    public FuzzyXMLCDATAImpl(String value) {
        this(null,value,-1,-1);
    }

    public FuzzyXMLCDATAImpl(FuzzyXMLNode parent, String value, int offset, int length) {
        super(parent, offset, length);
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
    
    public String toXMLString() {
        return "<![CDATA[" + FuzzyXMLUtil.escapeCDATA(getValue()) + "]]>";
    }
    
    public String toString(){
        return "CDATA: " + getValue();
    }

}
