package com.tulin.v8.webtools.ide.jsp.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.html.HTMLWhitespaceDetector;

public class JSPDirectiveScanner extends RuleBasedScanner {

	public JSPDirectiveScanner(ColorProvider provider) {
		IToken string = provider.getToken(WebToolsPlugin.PREF_COLOR_STRING);
		IToken script = provider.getToken(WebToolsPlugin.PREF_COLOR_SCRIPT);
		List<IRule> rules = new ArrayList<IRule>();

		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("\'", "\'", string, '\\'));
		rules.add(new WhitespaceRule(new HTMLWhitespaceDetector()));

		WordRule delimitor = new WordRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				if (c == '<' || c == '%' || c == '@') {
					return true;
				}
				return false;
			}

			public boolean isWordPart(char c) {
				if (c == '<' || c == '%' || c == '=' || c == '>' || c == '@') {
					return true;
				}
				return false;
			}
		});
		delimitor.addWord("<%@", script);
		delimitor.addWord("%>", script);
		rules.add(delimitor);

		setRules(rules.toArray(new IRule[rules.size()]));
	}
}
