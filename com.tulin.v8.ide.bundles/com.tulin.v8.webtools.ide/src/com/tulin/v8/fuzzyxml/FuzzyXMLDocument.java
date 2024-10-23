package com.tulin.v8.fuzzyxml;

import com.tulin.v8.fuzzyxml.event.FuzzyXMLModifyListener;

public interface FuzzyXMLDocument {

	public FuzzyXMLComment createComment(String value);

	public FuzzyXMLElement createElement(String name);

	public FuzzyXMLAttribute createAttribute(String name);

	public FuzzyXMLText createText(String value);

	public FuzzyXMLCDATA createCDATASection(String value);

	public FuzzyXMLProcessingInstruction createProcessingInstruction(String name, String data);

	public FuzzyXMLElement getDocumentElement();

	public FuzzyXMLDocType getDocumentType();

	public FuzzyXMLElement getElementByOffset(int offset);

	public void addModifyListener(FuzzyXMLModifyListener listener);

	public void removeModifyListener(FuzzyXMLModifyListener listener);

	public boolean isHTML();

}
