package com.tulin.v8.webtools.ide.css;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.swt.SWT;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;

public class CSSBlockScanner extends RuleBasedScanner {

	public CSSBlockScanner(ColorProvider colorProvider) {
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
		IToken tag = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG);
		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("@", " ", colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG)));
		rules.add(new MultiLineRule("/*", "*/", colorProvider.getToken(WebToolsPlugin.PREF_COLOR_CSSCOMMENT)));
		rules.add(new CSSRule(tag, colorProvider.getToken(WebToolsPlugin.PREF_COLOR_CSSPROP),
				colorProvider.getToken(WebToolsPlugin.PREF_COLOR_CSSVALUE, SWT.ITALIC)));
		return rules;
	}

}
