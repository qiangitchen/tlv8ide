package com.tulin.v8.webtools.ide;

import org.eclipse.jface.text.rules.IWordDetector;

public class JavaWordDetector implements IWordDetector {

	/*
	 * @see IWordDetector#isWordStart
	 */
	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	/*
	 * @see IWordDetector#isWordPart
	 */
	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

}
