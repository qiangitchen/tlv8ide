package com.tulin.v8.editors.ini.editors.eclipse;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class JavaWhitespaceDetector implements IWhitespaceDetector {
	public boolean isWhitespace(char c) {
		return Character.isWhitespace(c);
	}
}