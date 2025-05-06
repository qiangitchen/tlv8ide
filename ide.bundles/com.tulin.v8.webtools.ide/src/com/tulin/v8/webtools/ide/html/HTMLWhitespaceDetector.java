package com.tulin.v8.webtools.ide.html;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class HTMLWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
