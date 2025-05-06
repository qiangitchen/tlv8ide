package com.tulin.v8.webtools.ide.html.editors;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.ContentAssistProcessorRegistry;
import com.tulin.v8.webtools.ide.assist.EditorContentAssistant;
import com.tulin.v8.webtools.ide.assist.HTMLAssistProcessor;
import com.tulin.v8.webtools.ide.assist.InnerCSSAssistProcessor;
import com.tulin.v8.webtools.ide.assist.InnerJavaScriptAssistProcessor;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.content.ContentTypeRelatedExtensionTracker;
import com.tulin.v8.webtools.ide.hover.CompositeTextHover;
import com.tulin.v8.webtools.ide.html.HTMLHyperlinkDetector;
import com.tulin.v8.webtools.ide.html.HTMLPartitionScanner;
import com.tulin.v8.webtools.ide.html.HTMLScanner;
import com.tulin.v8.webtools.ide.html.HTMLTagScanner;
import com.tulin.v8.webtools.ide.html.HtmlAutoEditStrategy;
import com.tulin.v8.webtools.ide.html.InnerCSSScanner;
import com.tulin.v8.webtools.ide.html.InnerJavaScriptScanner;
import com.tulin.v8.webtools.ide.text.AbsTextSourceViewerConfiguration;

/**
 * <code>SourceViewerConfiguration</code> for <code>HTMLSourceEditor</code>.
 */
public class HTMLConfiguration extends AbsTextSourceViewerConfiguration {
	protected HTMLDoubleClickStrategy doubleClickStrategy;

	protected HTMLScanner scanner;
	protected HTMLTagScanner tagScanner;
	protected RuleBasedScanner commentScanner;
	protected RuleBasedScanner scriptScanner;
	protected RuleBasedScanner doctypeScanner;
	protected RuleBasedScanner directiveScanner;
	protected RuleBasedScanner javaScriptScanner;
	protected RuleBasedScanner cssScanner;

	protected ColorProvider colorProvider;

	protected ContentAssistant assistant;
	protected HTMLAssistProcessor processor;
	protected InnerJavaScriptAssistProcessor jsProcessor;
	protected InnerCSSAssistProcessor cssProcessor;

	protected HtmlAutoEditStrategy autoEditStrategy;
	protected HTMLHyperlinkDetector hyperlinkDetector;

	protected HTMLSourceEditor editor;

	private Set<IContentType> resolvedContentTypes;
	private Set<IContentType> fallbackContentTypes;
	private EditorContentAssistant contentAssistant;

	public HTMLConfiguration(HTMLSourceEditor editor, ColorProvider colorProvider) {
		this.editor = editor;
		this.colorProvider = colorProvider;
	}

	/**
	 * @since 2.0.3
	 */
	protected HtmlAutoEditStrategy createAutoEditStrategy() {
		return new HtmlAutoEditStrategy();
	}

	/**
	 * @since 2.0.3
	 */
	protected HTMLHyperlinkDetector createHyperlinkDetector() {
		HTMLHyperlinkDetector hyperlinkDetector = new HTMLHyperlinkDetector();
		hyperlinkDetector.setEditor(editor);
		return hyperlinkDetector;
	}

	/**
	 * @since 2.0.3
	 */
	public final HTMLHyperlinkDetector getHyperlinkDetector() {
		if (this.hyperlinkDetector == null) {
			this.hyperlinkDetector = createHyperlinkDetector();
		}
		return this.hyperlinkDetector;
	}

	/**
	 * @since 2.0.3
	 */
	public final HtmlAutoEditStrategy getAutoEditStrategy() {
		if (this.autoEditStrategy == null) {
			this.autoEditStrategy = createAutoEditStrategy();
		}
		return this.autoEditStrategy;
	}

	/**
	 * Returns all supportted content types.
	 */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, HTMLPartitionScanner.HTML_COMMENT,
				HTMLPartitionScanner.HTML_TAG, HTMLPartitionScanner.PREFIX_TAG, HTMLPartitionScanner.HTML_SCRIPT,
				HTMLPartitionScanner.HTML_DOCTYPE, HTMLPartitionScanner.HTML_DIRECTIVE, HTMLPartitionScanner.JAVASCRIPT,
				HTMLPartitionScanner.HTML_CSS };
	}

	/**
	 * @since 2.0.3
	 */
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		if (sourceViewer == null) {
			return null;
		}

		IHyperlinkDetector[] detectors = super.getHyperlinkDetectors(sourceViewer);
		IHyperlinkDetector[] result = new IHyperlinkDetector[detectors.length + 1];
		System.arraycopy(detectors, 0, result, 0, detectors.length);
		result[result.length - 1] = getHyperlinkDetector();

		return result;
	}

	/**
	 * @since 2.0.3
	 */
	public final IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		return new IAutoEditStrategy[] { getAutoEditStrategy() };
	}

	/**
	 * Creates or Returns the assist processor for HTML.
	 */
	public HTMLAssistProcessor getAssistProcessor() {
		if (processor == null) {
			processor = createAssistProcessor();
		}
		return processor;
	}

	/**
	 * Creates the assist processor for HTML.
	 */
	protected HTMLAssistProcessor createAssistProcessor() {
		HTMLAssistProcessor processor = new HTMLAssistProcessor();
		return processor;
	}

	/**
	 * Creates or Returns the assist processor for inner JavaScript.
	 */
	public InnerJavaScriptAssistProcessor getJavaScriptAssistProcessor() {
		if (jsProcessor == null) {
			jsProcessor = new InnerJavaScriptAssistProcessor(getAssistProcessor());
		}
		return jsProcessor;
	}

	/**
	 * Creates or Returns the assist processor for inner JavaScript.
	 */
	public InnerCSSAssistProcessor getCSSAssistProcessor() {
		if (cssProcessor == null) {
			cssProcessor = new InnerCSSAssistProcessor(getAssistProcessor());
		}
		return cssProcessor;
	}

	public Set<IContentType> getContentTypes(IDocument document) {
		if (this.resolvedContentTypes != null) {
			return this.resolvedContentTypes;
		}
		this.resolvedContentTypes = new LinkedHashSet<>();
		ITextFileBuffer buffer = getCurrentBuffer(document);
		if (buffer != null) {
			try {
				IContentType contentType = buffer.getContentType();
				if (contentType != null) {
					this.resolvedContentTypes.add(contentType);
				}
			} catch (CoreException ex) {
				ex.printStackTrace();
			}
		}
		String fileName = getCurrentFileName(document);
		if (fileName != null) {
			Queue<IContentType> types = new LinkedList<>(
					Arrays.asList(Platform.getContentTypeManager().findContentTypesFor(fileName)));
			while (!types.isEmpty()) {
				IContentType type = types.poll();
				this.resolvedContentTypes.add(type);
				IContentType parent = type.getBaseType();
				if (parent != null) {
					types.add(parent);
				}
			}
		}
		return this.resolvedContentTypes.isEmpty() ? fallbackContentTypes : resolvedContentTypes;
	}

	private static ITextFileBuffer getCurrentBuffer(IDocument document) {
		if (document != null) {
			return FileBuffers.getTextFileBufferManager().getTextFileBuffer(document);
		}
		return null;
	}

	private String getCurrentFileName(IDocument document) {
		String fileName = null;
		if (this.editor != null) {
			fileName = editor.getEditorInput().getName();
		}
		if (fileName == null) {
			ITextFileBuffer buffer = getCurrentBuffer(document);
			if (buffer != null) {
				IPath path = buffer.getLocation();
				if (path != null) {
					fileName = path.lastSegment();
				}
			}
		}
		return fileName;
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
		if (contentType.equals(HTMLPartitionScanner.JAVASCRIPT)) {
			List<ITextHover> hovers = WebToolsPlugin.getDefault().getHoverRegistry().getAvailableHovers(sourceViewer,
					editor, getContentTypes(sourceViewer.getDocument()));
			if (hovers == null || hovers.isEmpty()) {
				return null;
			} else if (hovers.size() == 1) {
				return hovers.get(0);
			} else {
				return new CompositeTextHover(hovers);
			}
		}
		return null;
	}

	/**
	 * Creates or Returns the <code>IContentAssistant</code>.
	 */
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		try {
			ContentAssistProcessorRegistry registry = WebToolsPlugin.getDefault().getContentAssistProcessorRegistry();
			ContentTypeRelatedExtensionTracker<IContentAssistProcessor> contentAssistProcessorTracker = new ContentTypeRelatedExtensionTracker<IContentAssistProcessor>(
					WebToolsPlugin.getDefault().getBundle().getBundleContext(), IContentAssistProcessor.class,
					sourceViewer.getTextWidget().getDisplay());
			Set<IContentType> types = getContentTypes(sourceViewer.getDocument());
			contentAssistant = new EditorContentAssistant(contentAssistProcessorTracker,
					registry.getContentAssistProcessors(sourceViewer, editor, types), types, fPreferenceStore);
			if (this.document != null) {
				associateTokenContentTypes(this.document);
			}
			watchDocument(sourceViewer.getDocument());
		} catch (Exception e) {
		}
		if (assistant == null) {
			assistant = new ContentAssistant();
			assistant.setInformationControlCreator(new IInformationControlCreator() {
				public IInformationControl createInformationControl(Shell parent) {
					return new DefaultInformationControl(parent);
				}
			});
			assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
			assistant.enableAutoInsert(true);

			HTMLAssistProcessor processor = getAssistProcessor();
			assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
			assistant.setContentAssistProcessor(processor, HTMLPartitionScanner.HTML_TAG);
			assistant.setContentAssistProcessor(processor, HTMLPartitionScanner.PREFIX_TAG);

			if (contentAssistant != null) {
				assistant.setContentAssistProcessor(
						contentAssistant.getContentAssistProcessor(IDocument.DEFAULT_CONTENT_TYPE),
						HTMLPartitionScanner.JAVASCRIPT);
			} else {
				InnerJavaScriptAssistProcessor jsProcessor = getJavaScriptAssistProcessor();
				assistant.setContentAssistProcessor(jsProcessor, HTMLPartitionScanner.JAVASCRIPT);
			}

			InnerCSSAssistProcessor cssProcessor = getCSSAssistProcessor();
			assistant.setContentAssistProcessor(cssProcessor, HTMLPartitionScanner.HTML_CSS);

			assistant.install(sourceViewer);

			IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
			assistant.enableAutoActivation(store.getBoolean(WebToolsPlugin.PREF_ASSIST_AUTO));
			assistant.setAutoActivationDelay(store.getInt(WebToolsPlugin.PREF_ASSIST_TIMES));
			assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
			processor.setAutoAssistChars(store.getString(WebToolsPlugin.PREF_ASSIST_CHARS).toCharArray());
			processor.setAssistCloseTag(store.getBoolean(WebToolsPlugin.PREF_ASSIST_CLOSE));
		}
		return assistant;
	}

//	public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer) {
//		return new IInformationControlCreator() {
//			public IInformationControl createInformationControl(Shell parent) {
//				return new DefaultInformationControl(parent, SWT.NONE, new HTMLTextPresenter(true));
//			}
//		};
//	}

	/**
	 * Returns <code>ITextDoubleClickStrategy</code>.
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new HTMLDoubleClickStrategy();
		}
		return doubleClickStrategy;
	}

	/**
	 * Creates or Returns the scanner for HTML.
	 */
	protected HTMLScanner getHTMLScanner() {
		if (scanner == null) {
			scanner = new HTMLScanner(colorProvider);
			scanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		}
		return scanner;
	}

	/**
	 * Creates or Returns the scanner for HTML tags.
	 */
	protected HTMLTagScanner getTagScanner() {
		if (tagScanner == null) {
			tagScanner = new HTMLTagScanner(colorProvider, false);
			tagScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG));
		}
		return tagScanner;
	}

	/**
	 * Creates or Returns the scanner for prefixed HTML tags.
	 */
	protected HTMLTagScanner getPrefixTagScanner() {
		return getTagScanner();
	}

	/**
	 * Creates or Returns the scanner for HTML comments.
	 */
	protected RuleBasedScanner getCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new RuleBasedScanner();
			commentScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_COMMENT));
		}
		return commentScanner;
	}

	/**
	 * Creates or Returns the scanner for scriptlets (&lt;% ... %&gt;).
	 */
	protected RuleBasedScanner getScriptScanner() {
		if (scriptScanner == null) {
			scriptScanner = new RuleBasedScanner();
			scriptScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_SCRIPT));
		}
		return scriptScanner;
	}

	/**
	 * Creates or Returns the scanner for directives (&lt;%@ ... %&gt;).
	 */
	protected RuleBasedScanner getDirectiveScanner() {
		if (directiveScanner == null) {
			directiveScanner = new RuleBasedScanner();
			directiveScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_SCRIPT));
		}
		return directiveScanner;
	}

	/**
	 * Creates or Returns the scanner for DOCTYPE decl.
	 */
	protected RuleBasedScanner getDoctypeScanner() {
		if (doctypeScanner == null) {
			doctypeScanner = new RuleBasedScanner();
			doctypeScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_DOCTYPE));
		}
		return doctypeScanner;
	}

	/**
	 * Creates or Returns the scanner for inner JavaScript.
	 */
	protected RuleBasedScanner getJavaScriptScanner() {
		if (javaScriptScanner == null) {
			javaScriptScanner = new InnerJavaScriptScanner(colorProvider);
			javaScriptScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		}
		return javaScriptScanner;
	}

	/**
	 * Creates or Returns the scanner for inner CSS.
	 */
	protected RuleBasedScanner getCSSScanner() {
		if (cssScanner == null) {
			cssScanner = new InnerCSSScanner(colorProvider);
			cssScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_TAG));
		}
		return cssScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = null;

		dr = new HTMLTagDamagerRepairer(getTagScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.HTML_TAG);
		reconciler.setRepairer(dr, HTMLPartitionScanner.HTML_TAG);

		dr = new HTMLTagDamagerRepairer(getPrefixTagScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.PREFIX_TAG);
		reconciler.setRepairer(dr, HTMLPartitionScanner.PREFIX_TAG);

		dr = new HTMLTagDamagerRepairer(getHTMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new HTMLTagDamagerRepairer(getCommentScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.HTML_COMMENT);
		reconciler.setRepairer(dr, HTMLPartitionScanner.HTML_COMMENT);

		dr = new DefaultDamagerRepairer(getScriptScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.HTML_SCRIPT);
		reconciler.setRepairer(dr, HTMLPartitionScanner.HTML_SCRIPT);

		dr = new DefaultDamagerRepairer(getDoctypeScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.HTML_DOCTYPE);
		reconciler.setRepairer(dr, HTMLPartitionScanner.HTML_DOCTYPE);

		dr = new DefaultDamagerRepairer(getDirectiveScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.HTML_DIRECTIVE);
		reconciler.setRepairer(dr, HTMLPartitionScanner.HTML_DIRECTIVE);

		dr = new JavaScriptDamagerRepairer(getJavaScriptScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.JAVASCRIPT);
		reconciler.setRepairer(dr, HTMLPartitionScanner.JAVASCRIPT);

		dr = new JavaScriptDamagerRepairer(getCSSScanner());
		reconciler.setDamager(dr, HTMLPartitionScanner.HTML_CSS);
		reconciler.setRepairer(dr, HTMLPartitionScanner.HTML_CSS);

		return reconciler;
	}

	/**
	 * Returns the <code>ColorProvider</code>.
	 */
	protected ColorProvider getColorProvider() {
		return this.colorProvider;
	}

	private class HTMLTagDamagerRepairer extends DefaultDamagerRepairer {

		public HTMLTagDamagerRepairer(ITokenScanner scanner) {
			super(scanner);
		}

		// TODO This method works with 3.0 and 3.1.2 but does't work well with Eclipse
		// 3.1.1.
		public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged) {
			if (!documentPartitioningChanged) {
				String source = fDocument.get();
				int start = source.substring(0, e.getOffset()).lastIndexOf('<');
				if (start == -1) {
					start = 0;
				}
				int end = source.indexOf('>', e.getOffset());
				int nextEnd = source.indexOf('>', end + 1);
				if (nextEnd >= 0 && nextEnd > end) {
					end = nextEnd;
				}
				int end2 = e.getOffset() + (e.getText() == null ? e.getLength() : e.getText().length());
				if (end == -1) {
					end = source.length();
				} else if (end2 > end) {
					end = end2;
				} else {
					end++;
				}

				return new Region(start, end - start);
			}
			return partition;
		}

	}

	private class JavaScriptDamagerRepairer extends DefaultDamagerRepairer {

		public JavaScriptDamagerRepairer(ITokenScanner scanner) {
			super(scanner);
		}

		// TODO This method works with 3.0 and 3.1.2 but does't work well with Eclipse
		// 3.1.1.
		public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged) {
			if (!documentPartitioningChanged) {
				String source = fDocument.get();
				int start = source.substring(0, e.getOffset()).lastIndexOf("/*");
				if (start == -1) {
					start = 0;
				}
				int end = source.indexOf("*/", e.getOffset());
				int end2 = e.getOffset() + (e.getText() == null ? e.getLength() : e.getText().length());
				if (end == -1) {
					end = source.length();
				} else if (end2 > end) {
					end = end2;
				} else {
					end++;
				}

				return new Region(start, end - start);
			}
			return partition;
		}

	}

	private IDocument document;

	public void watchDocument(IDocument document) {
		if (this.document == document) {
			return;
		}
		if (this.document != null) {
			this.document.removeDocumentPartitioningListener(this);
		}
		if (document != null) {
			this.document = document;
			associateTokenContentTypes(document);
			document.addDocumentPartitioningListener(this);
		}
	}

	@Override
	public void documentPartitioningChanged(IDocument document) {
		associateTokenContentTypes(document);
	}

	private void associateTokenContentTypes(IDocument document) {
		if (contentAssistant == null) {
			return;
		}
		contentAssistant.updateTokens(document);
	}

}