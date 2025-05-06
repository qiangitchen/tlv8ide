package com.tulin.v8.editors.ini.editors.eclipse;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WordPatternRule;

public final class LeadingWhitespacePredicateRule extends WordPatternRule {
	public LeadingWhitespacePredicateRule(IToken token, String whitespace) {
		super(new DummyDetector(), whitespace, "dummy", token);
		setColumnConstraint(0);
	}

	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int c;
		do
			c = scanner.read();
		while (Character.isWhitespace((char) c));

		scanner.unread();

		return true;
	}

	private static class DummyDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return false;
		}

		public boolean isWordPart(char c) {
			return false;
		}
	}
}