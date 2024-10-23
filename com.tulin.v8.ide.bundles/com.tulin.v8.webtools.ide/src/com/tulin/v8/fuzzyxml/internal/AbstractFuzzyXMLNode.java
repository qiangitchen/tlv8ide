package com.tulin.v8.fuzzyxml.internal;

import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;

public abstract class AbstractFuzzyXMLNode implements FuzzyXMLNode {

	private int offset = -1;
	private int length = -1;
	private FuzzyXMLNode parent;
	private FuzzyXMLDocumentImpl doc;
//	private String namespaceURI;
//	private String prefix;

	public AbstractFuzzyXMLNode() {
		super();
	}

	public AbstractFuzzyXMLNode(FuzzyXMLNode parent, int offset, int length) {
		super();
		setParentNode(parent);
		setOffset(offset);
		setLength(length);
	}

//	public void setNamespaceURI(String namespaceURI){
//		this.namespaceURI = namespaceURI;
//	}
//	
//	public String getNamespaceURI(){
//		return this.namespaceURI;
//	}
//	
//	public void setPrefix(String prefix){
//		this.prefix = prefix;
//	}
//	
//	public String getPrefix(){
//		return this.prefix;
//	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public int getOffset() {
		return offset;
	}

	public FuzzyXMLNode getParentNode() {
		return parent;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setParentNode(FuzzyXMLNode parent) {
		this.parent = parent;
	}

	/**
	 * @param newText
	 * @param offset
	 * @param length
	 */
	protected void fireModifyEvent(String newText, int offset, int length) {
		FuzzyXMLDocumentImpl doc = getDocument();
		if (doc == null) {
			return;
		}
		doc.fireModifyEvent(newText, offset, length);
	}

	/**
	 * @param parent
	 * @param offset
	 * @param append
	 */
	protected void appendOffset(FuzzyXMLElement parent, int offset, int append) {
		FuzzyXMLDocumentImpl doc = getDocument();
		if (doc == null) {
			return;
		}
		doc.appendOffset(parent, offset, append);
	}

	public void setDocument(FuzzyXMLDocumentImpl doc) {
		this.doc = doc;
	}

	public FuzzyXMLDocumentImpl getDocument() {
		return doc;
	}
}
