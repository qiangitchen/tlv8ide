package com.tulin.v8.editors.vue.editor;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.HTMLAssistProcessor;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.html.editors.HTMLConfiguration;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * <code>SourceViewerConfiguration</code> for <code>VueEditor</code>.
 */
public class VUEConfiguration extends HTMLConfiguration {

	public VUEConfiguration(HTMLSourceEditor editor, ColorProvider colorProvider) {
		super(editor, colorProvider);
	}

	@Override
	protected HTMLAssistProcessor createAssistProcessor() {
		return super.createAssistProcessor();
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return super.getConfiguredContentTypes(sourceViewer);
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = null;
		IPresentationReconciler ireconciler = super.getPresentationReconciler(sourceViewer);
		if (ireconciler instanceof PresentationReconciler) {
			reconciler = (PresentationReconciler) ireconciler;
		}
		return reconciler;
	}

	@Override
	protected RuleBasedScanner getJavaScriptScanner() {
		if (javaScriptScanner == null) {
			javaScriptScanner = new VueInnerJavaScriptScanner(colorProvider);
			javaScriptScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		}
		return javaScriptScanner;
	}

//	@Override
//	protected RuleBasedScanner getCSSScanner() {
//		if (cssScanner == null) {
//			cssScanner = new VueInnerCSSScanner(colorProvider);
//			cssScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
//		}
//		return cssScanner;
//	}

}
