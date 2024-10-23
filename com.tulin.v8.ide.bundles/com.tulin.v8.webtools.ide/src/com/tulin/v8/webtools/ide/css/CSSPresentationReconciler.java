package com.tulin.v8.webtools.ide.css;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.css.editors.CSSPartitionScanner;

/**
 * CSS代码高亮(通用文本编辑器扩展)
 */
public class CSSPresentationReconciler extends PresentationReconciler {
	private ColorProvider colorProvider;
	private CSSBlockScanner defaultScanner;
	private RuleBasedScanner commentScanner;

	public CSSPresentationReconciler() {
		colorProvider = WebToolsPlugin.getDefault().getColorProvider();

		DefaultDamagerRepairer dr = null;
		dr = new DefaultDamagerRepairer(getDefaultScanner());
		setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getCommentScanner());
		setDamager(dr, CSSPartitionScanner.CSS_COMMENT);
		setRepairer(dr, CSSPartitionScanner.CSS_COMMENT);
	}

	private RuleBasedScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new CSSBlockScanner(colorProvider);
			defaultScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		}
		return defaultScanner;
	}

	private RuleBasedScanner getCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new RuleBasedScanner();
			commentScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_CSSCOMMENT));
		}
		return commentScanner;
	}

}
