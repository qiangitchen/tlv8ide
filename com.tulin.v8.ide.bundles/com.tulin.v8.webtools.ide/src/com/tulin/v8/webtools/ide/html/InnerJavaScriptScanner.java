package com.tulin.v8.webtools.ide.html;

import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.js.JavaScriptScanner;

/**
 * <code>RuleBasedScanner</code> for the inner JavaScript in the HTML.
 */
public class InnerJavaScriptScanner extends JavaScriptScanner {

	public InnerJavaScriptScanner(ColorProvider colorProvider) {
		super(colorProvider);
	}

	@Override
	protected List<IRule> createRules(ColorProvider colorProvider) {
		IToken tag = colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG);

		List<IRule> rules = super.createRules(colorProvider);
		rules.add(new SingleLineRule("<scrip", "t", tag));
		rules.add(new SingleLineRule("</script", ">", tag));

		return rules;
	}

}
