package com.tulin.v8.webtools.ide.html;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;

public class HTMLRuleBasedScanner extends InnerJavaScriptScanner {

	public HTMLRuleBasedScanner(ColorProvider colorProvider) {
		super(colorProvider);
		List<IRule> rules = createRules(colorProvider);
		setRules(rules.toArray(new IRule[rules.size()]));
	}

	protected List<IRule> createRules(ColorProvider colorProvider) {
		IToken string = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_STRING);
		IToken attr = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_ATTR);
		IToken val = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_VALUE);
		IToken tag = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG);

		List<IRule> prules = super.createRules(colorProvider);
		List<IRule> rules = new ArrayList<>();
		rules.add(new WhitespaceRule(new HTMLWhitespaceDetector()));
		rules.add(new SingleLineRule("<", " ", tag));
		rules.add(new SingleLineRule("</", ">", tag));
		rules.add(new MultiLineRule("\"", "\"", val, '\\'));
		rules.add(new MultiLineRule("'", "'", string, '\\'));
		rules.add(new MultiLineRule(" ", "=", attr, '\\'));
		rules.addAll(prules);
		rules.add(new SingleLineRule("<styl", "e", tag));
		rules.add(new SingleLineRule("</style", ">", tag));

		return rules;
	}

}
