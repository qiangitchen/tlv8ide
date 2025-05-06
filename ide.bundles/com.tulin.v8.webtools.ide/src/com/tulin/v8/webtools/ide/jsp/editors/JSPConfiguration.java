package com.tulin.v8.webtools.ide.jsp.editors;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;

import com.tulin.v8.webtools.ide.IHyperlinkProvider;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.HTMLAssistProcessor;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.html.HTMLHyperlinkDetector;
import com.tulin.v8.webtools.ide.html.HTMLPartitionScanner;
import com.tulin.v8.webtools.ide.html.HTMLTagScanner;
import com.tulin.v8.webtools.ide.html.HtmlAutoEditStrategy;
import com.tulin.v8.webtools.ide.html.editors.HTMLConfiguration;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * SourceViewerConfiguration for the JSP editor.
 */
public class JSPConfiguration extends HTMLConfiguration {

	private HTMLTagScanner prefixTagScanner;
	private JSPScriptletScanner scriptletScanner = null;
	private JSPDirectiveScanner directiveScanner = null;
	private IContentAssistant assistant = null;

	private JSPDirectiveAssistProcessor directiveProcessor;
	private JSPScriptletAssistProcessor scriptletProcessor;

	/**
	 * The constructor.
	 * 
	 * @param colorProvider the <code>ColorProvider</code>.
	 */
	public JSPConfiguration(HTMLSourceEditor editor, ColorProvider colorProvider) {
		super(editor, colorProvider);
	}

	/**
	 * @since 2.0.3
	 */
	@Override
	protected HTMLHyperlinkDetector createHyperlinkDetector() {
		HTMLHyperlinkDetector hyperlink = super.createHyperlinkDetector();
		hyperlink.addHyperlinkProvider(new JSPHyperlinkProvider());

		IHyperlinkProvider[] providers = WebToolsPlugin.getDefault().getHyperlinkProviders();
		for (int i = 0; i < providers.length; i++) {
			hyperlink.addHyperlinkProvider(providers[i]);
		}

		return hyperlink;
	}

	/**
	 * Creates and returns the <code>JSPAssistProcessor</code>.
	 * 
	 * @return the <code>JSPAssistProcessor</code>
	 * @see JSPAssistProcessor
	 */
	@Override
	protected HTMLAssistProcessor createAssistProcessor() {
		return new JSPAssistProcessor();
	}

	/**
	 * Returns the <code>JSPScriptletScanner</code>.
	 * 
	 * @return the <code>JSPScriptletScanner</code>
	 * @see JSPScriptletScanner
	 */
	@Override
	protected RuleBasedScanner getScriptScanner() {
		if (scriptletScanner == null) {
			scriptletScanner = new JSPScriptletScanner(getColorProvider());
			scriptletScanner.setDefaultReturnToken(getColorProvider().getToken(WebToolsPlugin.PREF_COLOR_FG));
		}
		return scriptletScanner;
	}

	/**
	 * Returns the <code>JSPDirectiveScanner</code>.
	 * 
	 * @return the <code>JSPDirectiveScanner</code>
	 * @see JSPDirectiveScanner
	 */
	@Override
	protected RuleBasedScanner getDirectiveScanner() {
		if (directiveScanner == null) {
			directiveScanner = new JSPDirectiveScanner(getColorProvider());
			directiveScanner.setDefaultReturnToken(getColorProvider().getToken(WebToolsPlugin.PREF_COLOR_TAG));
		}
		return directiveScanner;
	}

	@Override
	protected HTMLTagScanner getPrefixTagScanner() {
		if (prefixTagScanner == null) {
			prefixTagScanner = new HTMLTagScanner(getColorProvider(), true);
			prefixTagScanner.setDefaultReturnToken(getColorProvider().getToken(WebToolsPlugin.PREF_COLOR_TAGLIB));
		}
		return prefixTagScanner;
	}

	/**
	 * Returns the <code>JSPAutoEditStrategy</code>.
	 * 
	 * @return the <code>JSPAutoEditStrategy</code>
	 * @since 2.0.3
	 * @see JSPAutoEditStrategy
	 */
	@Override
	protected HtmlAutoEditStrategy createAutoEditStrategy() {
		return new JSPAutoEditStrategy();
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		if (assistant == null) {
			assistant = super.getContentAssistant(sourceViewer);

			directiveProcessor = new JSPDirectiveAssistProcessor();
			((ContentAssistant) assistant).setContentAssistProcessor(directiveProcessor,
					HTMLPartitionScanner.HTML_DIRECTIVE);

			scriptletProcessor = new JSPScriptletAssistProcessor();
			((ContentAssistant) assistant).setContentAssistProcessor(scriptletProcessor,
					HTMLPartitionScanner.HTML_SCRIPT);
		}
		return assistant;
	}

	/**
	 * Returns the <code>JSPDirectiveAssistProcessor</code>.
	 * 
	 * @return the <code>JSPDirectiveAssistProcessor</code>.
	 * @see JSPDirectiveAssistProcessor
	 */
	public JSPDirectiveAssistProcessor getDirectiveAssistProcessor() {
		return directiveProcessor;
	}

	/**
	 * Returns the <code>JSPScriptletAssistProcessor</code>.
	 * 
	 * @return the <code>JSPScriptletAssistProcessor</code>.
	 * @see JSPScriptletAssistProcessor
	 */
	public JSPScriptletAssistProcessor getScriptletAssistProcessor() {
		return scriptletProcessor;
	}
}
