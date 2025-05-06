package com.tulin.v8.webtools.ide.html;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

public class TagRule extends MultiLineRule {

	/**
	 * Only prefixed tags
	 */
	public static final int PREFIX = 0;

	/**
	 * Only no prefixed tags
	 */
	public static final int NO_PREFIX = 1;

	/**
	 * Both prefixed and no prefixed tags
	 */
	public static final int BOTH = 2;

	private int prefix;

	/**
	 * Constructor.
	 * 
	 * @param token  the token
	 * @param prefix {@link #PREFIX}, {@link #NO_PREFIX} or {@link #BOTH}
	 */
	public TagRule(IToken token, int prefix) {
		super("<", ">", token);
		this.prefix = prefix;
	}

	protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
		if (sequence[0] == '<') {
			int c = scanner.read();
			if (c == '?' || c == '!' || c == '%') {
				scanner.unread();
				return false;
			}

			int back = 1;
			try {
				while (true) {
					c = scanner.read();
					back++;
					if (c == -1) {
						return false;
					}
					if (Character.isWhitespace(c) || c == '>') {
						if (prefix == PREFIX) {
							return false;
						}
						break;
					} else if (c == ':') {
						if (prefix == NO_PREFIX) {
							return false;
						}
						break;
					}
				}
			} finally {
				for (int i = 0; i < back; i++) {
					scanner.unread();
				}
			}

		} else if (sequence[0] == '>') {
			// read previous char
			scanner.unread();
			scanner.unread();
			int c = scanner.read();
			// repair position
			scanner.read();

			if (c == '%') {
				return false;
			}
		}
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}

	protected boolean endSequenceDetected(ICharacterScanner scanner) {

		int c;
		boolean doubleQuoted = false;
		boolean singleQuoted = false;

		char[][] delimiters = scanner.getLegalLineDelimiters();
		boolean previousWasEscapeCharacter = false;
		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			if (c == fEscapeCharacter) {
				// Skip the escaped character.
				scanner.read();
			} else if (c == '"') {
				if (singleQuoted == false) {
					doubleQuoted = !doubleQuoted;
				}
			} else if (c == '\'') {
				if (doubleQuoted == false) {
					singleQuoted = !singleQuoted;
				}
			} else if (fEndSequence.length > 0 && c == fEndSequence[0]) {
				// Check if the specified end sequence has been found.
				if (doubleQuoted == false && singleQuoted == false && sequenceDetected(scanner, fEndSequence, true))
					return true;
			} else if (fBreaksOnEOL) {
				// Check for end of line since it can be used to terminate the pattern.
				for (int i = 0; i < delimiters.length; i++) {
					if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true)) {
						if (!fEscapeContinuesLine || !previousWasEscapeCharacter)
							return true;
					}
				}
			}
			previousWasEscapeCharacter = (c == fEscapeCharacter);
		}
		if (fBreaksOnEOF)
			return true;
		scanner.unread();
		return false;
	}

}
