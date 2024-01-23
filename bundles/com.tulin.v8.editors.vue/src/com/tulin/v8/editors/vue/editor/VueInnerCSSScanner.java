package com.tulin.v8.editors.vue.editor;

import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.html.InnerCSSScanner;

public class VueInnerCSSScanner extends InnerCSSScanner {

	public VueInnerCSSScanner(ColorProvider colorProvider) {
		super(colorProvider);
	}

	protected List<IRule> createRules(ColorProvider colorProvider) {
		IToken tag = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG);

		List<IRule> rules = super.createRules(colorProvider);
		rules.add(new SingleLineRule("<style", ">", tag));
		rules.add(new SingleLineRule("</style", ">", tag));

		return rules;
	}

}
