package com.tulin.v8.webtools.ide.js;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.js.editors.JavaScriptPartitionScanner;

/**
 * JS代码高亮(通用文本编辑器扩展)
 */
public class JavaScriptPresentationReconciler extends PresentationReconciler {
	private ColorProvider colorProvider;
	private RuleBasedScanner jsdocScanner;
	private RuleBasedScanner commentScanner;
	private RuleBasedScanner defaultScanner;

	public JavaScriptPresentationReconciler() {
		colorProvider = WebToolsPlugin.getDefault().getColorProvider();

		DefaultDamagerRepairer dr = null;

		dr = new DefaultDamagerRepairer(getDefaultScanner());
		setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getJsdocScanner());
		setDamager(dr, JavaScriptPartitionScanner.JS_JSDOC);
		setRepairer(dr, JavaScriptPartitionScanner.JS_JSDOC);

		dr = new DefaultDamagerRepairer(getCommentScanner());
		setDamager(dr, JavaScriptPartitionScanner.JS_COMMENT);
		setRepairer(dr, JavaScriptPartitionScanner.JS_COMMENT);
	}

	private RuleBasedScanner getJsdocScanner() {
		if (jsdocScanner == null) {
			jsdocScanner = new RuleBasedScanner();
			jsdocScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSDOC));
		}
		return jsdocScanner;
	}

	private RuleBasedScanner getCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new RuleBasedScanner();
			commentScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSCOMMENT));
		}
		return commentScanner;
	}

	private RuleBasedScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new JavaScriptScanner(colorProvider);
			defaultScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		}
		return defaultScanner;
	}

}
