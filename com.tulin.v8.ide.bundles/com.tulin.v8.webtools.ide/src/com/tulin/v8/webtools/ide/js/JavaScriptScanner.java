package com.tulin.v8.webtools.ide.js;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WordRule;

import com.tulin.v8.webtools.ide.JavaWordDetector;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;

public class JavaScriptScanner extends RuleBasedScanner {

	public static final String KEYWORDS[] = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class",
			"const", "continue", "debugger", "default", "delete", "do", "double", "else", "enum", "export", "extends",
			"false", "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in",
			"instanceof", "int", "interface", "let", "long", "native", "new", "null", "package", "private", "protected",
			"prototype", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw",
			"throws", "transient", "true", "try", "typeof", "var", "void", "while", "with", "typeof", "yield",
			"undefined", "Infinity", "NaN" };

	public JavaScriptScanner(ColorProvider colorProvider) {
		List<IRule> rules = createRules(colorProvider);
		setRules(rules.toArray(new IRule[rules.size()]));
	}

	/**
	 * Creates the list of <code>IRule</code>. If you have to customize rules,
	 * override this method.
	 *
	 * @param colorProvider ColorProvider
	 * @return the list of <code>IRule</code>
	 */
	protected List<IRule> createRules(ColorProvider colorProvider) {
		IToken keyword = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSKEYWORD, true);
		IToken string = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSSTRING);
		IToken normal = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG);
		IToken jsdoc = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSDOC);
		IToken comment = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSCOMMENT);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));
		rules.add(new SingleLineRule("\\//", null, normal));
		rules.add(new MultiLineRule("/**", "*/", jsdoc, '\\'));
		rules.add(new MultiLineRule("/*", "*/", comment, '\\'));
		rules.add(new EndOfLineRule("//", comment));

		WordRule wordRule = new WordRule(new JavaWordDetector(), normal);
		for (int i = 0; i < KEYWORDS.length; i++) {
			wordRule.addWord(KEYWORDS[i], keyword);
		}
		rules.add(wordRule);
		return rules;
	}

}
