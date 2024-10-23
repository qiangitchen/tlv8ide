package com.tulin.v8.webtools.ide.html;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;

/**
 * HTML代码高亮(通用文本编辑器扩展)
 */
public class HtmlPresentationReconciler extends PresentationReconciler {

	public HtmlPresentationReconciler() {
		DefaultDamagerRepairer dr = null;

		ColorProvider colorProvider = WebToolsPlugin.getDefault().getColorProvider();

		HTMLTagScanner tagScanner = new HTMLTagScanner(colorProvider, false);
		tagScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG));
		dr = new HTMLTagDamagerRepairer(tagScanner);
		this.setDamager(dr, HTMLPartitionScanner.HTML_TAG);
		this.setRepairer(dr, HTMLPartitionScanner.HTML_TAG);

		dr = new HTMLTagDamagerRepairer(tagScanner);
		this.setDamager(dr, HTMLPartitionScanner.PREFIX_TAG);
		this.setRepairer(dr, HTMLPartitionScanner.PREFIX_TAG);

		HTMLRuleBasedScanner scanner = new HTMLRuleBasedScanner(colorProvider);
		scanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		dr = new DefaultDamagerRepairer(scanner);
		this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		RuleBasedScanner commentScanner = new RuleBasedScanner();
		commentScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_COMMENT));
		dr = new HTMLTagDamagerRepairer(commentScanner);
		this.setDamager(dr, HTMLPartitionScanner.HTML_COMMENT);
		this.setRepairer(dr, HTMLPartitionScanner.HTML_COMMENT);

		RuleBasedScanner scriptScanner = new RuleBasedScanner();
		scriptScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_SCRIPT));
		dr = new DefaultDamagerRepairer(scriptScanner);
		this.setDamager(dr, HTMLPartitionScanner.HTML_SCRIPT);
		this.setRepairer(dr, HTMLPartitionScanner.HTML_SCRIPT);

		RuleBasedScanner doctypeScanner = new RuleBasedScanner();
		doctypeScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_DOCTYPE));
		dr = new DefaultDamagerRepairer(doctypeScanner);
		this.setDamager(dr, HTMLPartitionScanner.HTML_DOCTYPE);
		this.setRepairer(dr, HTMLPartitionScanner.HTML_DOCTYPE);

		RuleBasedScanner directiveScanner = new RuleBasedScanner();
		directiveScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_SCRIPT));
		dr = new DefaultDamagerRepairer(directiveScanner);
		this.setDamager(dr, HTMLPartitionScanner.HTML_DIRECTIVE);
		this.setRepairer(dr, HTMLPartitionScanner.HTML_DIRECTIVE);

		InnerJavaScriptScanner javaScriptScanner = new InnerJavaScriptScanner(colorProvider);
		javaScriptScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		dr = new JavaScriptDamagerRepairer(javaScriptScanner);
		this.setDamager(dr, HTMLPartitionScanner.JAVASCRIPT);
		this.setRepairer(dr, HTMLPartitionScanner.JAVASCRIPT);

		InnerCSSScanner cssScanner = new InnerCSSScanner(colorProvider);
		cssScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		dr = new JavaScriptDamagerRepairer(cssScanner);
		this.setDamager(dr, HTMLPartitionScanner.HTML_CSS);
		this.setRepairer(dr, HTMLPartitionScanner.HTML_CSS);
	}
}