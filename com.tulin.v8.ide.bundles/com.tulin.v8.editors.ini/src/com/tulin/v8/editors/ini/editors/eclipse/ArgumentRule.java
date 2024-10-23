package com.tulin.v8.editors.ini.editors.eclipse;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WordPatternRule;

public final class ArgumentRule extends WordPatternRule {
	private int fCount = 0;

	public ArgumentRule(IToken token) {
		super(new ArgumentDetector(), "{", "}", token);
	}

	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		this.fCount += 1;

		if (scanner.read() == 125) {
			return this.fCount > 2;
		}
		scanner.unread();
		return super.endSequenceDetected(scanner);
	}

	protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
		this.fCount = 0;
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}

	private static class ArgumentDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return '{' == c;
		}

		public boolean isWordPart(char c) {
			return (c == '}') || (Character.isDigit(c));
		}
	}
}