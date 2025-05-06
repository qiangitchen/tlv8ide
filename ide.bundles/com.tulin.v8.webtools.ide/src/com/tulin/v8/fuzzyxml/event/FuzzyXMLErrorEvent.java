package com.tulin.v8.fuzzyxml.event;

import com.tulin.v8.fuzzyxml.FuzzyXMLNode;

public class FuzzyXMLErrorEvent {
	private int offset;
	private int length;
	private String message;
	private FuzzyXMLNode node;

	public FuzzyXMLErrorEvent(int offset, int length, String message, FuzzyXMLNode node) {
		this.offset = offset;
		this.length = length;
		this.message = message;
		this.node = node;
	}

	public int getLength() {
		return length;
	}

	public String getMessage() {
		return message;
	}

	public int getOffset() {
		return offset;
	}

	public FuzzyXMLNode getNode() {
		return node;
	}
}
