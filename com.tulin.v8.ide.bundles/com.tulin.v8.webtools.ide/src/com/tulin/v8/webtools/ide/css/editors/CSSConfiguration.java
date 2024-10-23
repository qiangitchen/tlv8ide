package com.tulin.v8.webtools.ide.css.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.css.CSSAssistProcessor;
import com.tulin.v8.webtools.ide.css.CSSBlockScanner;

public class CSSConfiguration extends TextSourceViewerConfiguration {

	private ColorProvider colorProvider;
	private RuleBasedScanner commentScanner;
	private CSSBlockScanner defaultScanner;

	public CSSConfiguration(ColorProvider colorProvider) {
		this.colorProvider = colorProvider;
	}

	private RuleBasedScanner getCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new RuleBasedScanner();
			commentScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_CSSCOMMENT));
		}
		return commentScanner;
	}

	private RuleBasedScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new CSSBlockScanner(colorProvider);
			defaultScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG));
		}
		return defaultScanner;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, CSSPartitionScanner.CSS_COMMENT };
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.enableAutoInsert(true);
		CSSAssistProcessor processor = new CSSAssistProcessor();
		assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		assistant.install(sourceViewer);

//		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
//		assistant.enableAutoActivation(store.getBoolean(WebToolsPlugin.PREF_ASSIST_AUTO));
//		assistant.setAutoActivationDelay(store.getInt(WebToolsPlugin.PREF_ASSIST_TIMES));
		
		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		
		return assistant;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = null;

		dr = new DefaultDamagerRepairer(getDefaultScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getCommentScanner());
		reconciler.setDamager(dr, CSSPartitionScanner.CSS_COMMENT);
		reconciler.setRepairer(dr, CSSPartitionScanner.CSS_COMMENT);

		return reconciler;
	}
}
