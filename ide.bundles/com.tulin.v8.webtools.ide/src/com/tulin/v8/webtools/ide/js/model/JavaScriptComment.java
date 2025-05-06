package com.tulin.v8.webtools.ide.js.model;

/**
 * The model for the JavaScript comment.
 */
public class JavaScriptComment {
	private int start;
	private int end;
	private String text;

	public JavaScriptComment(int start, int end, String text) {
		this.start = start;
		this.end = end;
		this.text = text;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		return getText();
	}
}
