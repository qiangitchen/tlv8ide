package com.tulin.v8.webtools.ide.js.editors;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class JavaScriptPartitionScanner extends RuleBasedPartitionScanner {

	public final static String JS_SINGLE_COMMENT = "__js_single_comment";
	public final static String JS_COMMENT = "__js_comment";
	public final static String JS_JSDOC = "__js_jsdoc";
	public final static String JS_STRING = "__js_string";
	public final static String JS_CHARACTER = "__js_character";

	public JavaScriptPartitionScanner() {
		IToken singlecomment = new Token(JS_SINGLE_COMMENT);
		IToken comment = new Token(JS_COMMENT);
		IToken jsdoc = new Token(JS_JSDOC);
		IToken jsstring = new Token(JS_STRING);
		IToken jscharacter = new Token(JS_CHARACTER);

		IPredicateRule[] rules = new IPredicateRule[7];
		rules[0] = new EndOfLineRule("//", singlecomment, '\\', true);
		rules[1] = new SingleLineRule("\"", "\"", jsstring, '\\', false, true);
		rules[2] = new SingleLineRule("'", "'", jscharacter, '\\');
		rules[3] = new SingleLineRule("'", "'", jscharacter, '\\');
		rules[4] = new EmptyCommentRule(comment);
		rules[5] = new MultiLineRule("/**", "*/", jsdoc);
		rules[6] = new MultiLineRule("/*", "*/", comment);

		setPredicateRules(rules);
	}

	static class EmptyCommentRule extends WordRule implements IPredicateRule {

		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return evaluate(scanner);
		}

		public IToken getSuccessToken() {
			return fSuccessToken;
		}

		private IToken fSuccessToken;

		public EmptyCommentRule(IToken successToken) {
			super(new EmptyCommentDetector());
			fSuccessToken = successToken;
			addWord("/**/", fSuccessToken);
		}
	}

	static class EmptyCommentDetector implements IWordDetector {

		public boolean isWordStart(char c) {
			return c == '/';
		}

		public boolean isWordPart(char c) {
			return c == '*' || c == '/';
		}

		EmptyCommentDetector() {
		}
	}
}
