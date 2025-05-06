package com.tulin.v8.fuzzyxml.event;

public class FuzzyXMLModifyEvent {
	private int offset;
	private int length;
	private String newText;

	public FuzzyXMLModifyEvent(String newText, int offset, int length) {
		super();
		this.offset = offset;
		this.length = length;
		this.newText = newText;
	}

	public int getLength() {
		return length;
	}

	public String getNewText() {
		return newText;
	}

	public int getOffset() {
		return offset;
	}
}
