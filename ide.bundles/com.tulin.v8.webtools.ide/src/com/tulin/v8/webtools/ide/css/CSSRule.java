package com.tulin.v8.webtools.ide.css;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * An implementation of IPredicateRule for CSS properties and values.
 * <p>
 * For exmaple:
 * </p>
 * 
 * <pre>
 * <span style="color:red;">background:</span> <span style=
"color:blue;">black;</span>
 * </pre>
 * 
 * This rule returns following tokens.
 * <ul>
 * <li><span style="color:red;">A part ends with ':'</span> - returns a property
 * token</li>
 * <li><span style="color:blue;">A part ends with ';'</span> - returns a value
 * token</li>
 * </ul>
 */
public class CSSRule extends MultiLineRule {
	private IToken tagToken;
	private IToken propToken;
	private IToken valueToken;

	private boolean isStart = false;

	/**
	 * Constructor.
	 * 
	 * @param propToken  a property token
	 * @param valueToken a value token
	 */
	public CSSRule(IToken tagToken, IToken propToken, IToken valueToken) {
		super("{", "}", tagToken);
		this.tagToken = tagToken;
		this.propToken = propToken;
		this.valueToken = valueToken;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if (isStart) {
			int ch;
			while ((ch = scanner.read()) != ICharacterScanner.EOF) {
				if (ch == fEndSequence[fEndSequence.length - 1]) {
					isStart = false;
					return tagToken;
				}
				if (ch == ':') {
					return propToken;
				} else if (ch == ';' || ch == '\r' || ch == '\n') {
					return valueToken;
				}
			}
		}
		int c = scanner.read();
		if (c == fStartSequence[0]) {
			if (sequenceDetected(scanner, fStartSequence, false)) {
				isStart = true;
			}
		} else if (c == fEndSequence[fEndSequence.length - 1]) {
			isStart = false;
		}
		scanner.unread();
		return Token.UNDEFINED;
	}

}
