package com.tulin.v8.webtools.ide.js.editors;

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
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.ContentAssistProcessorRegistry;
import com.tulin.v8.webtools.ide.assist.EditorContentAssistant;
import com.tulin.v8.webtools.ide.color.ColorProvider;
import com.tulin.v8.webtools.ide.content.ContentTypeRelatedExtensionTracker;
import com.tulin.v8.webtools.ide.hover.CompositeTextHover;
import com.tulin.v8.webtools.ide.js.JavaScriptAssistProcessor;
import com.tulin.v8.webtools.ide.js.JavaScriptScanner;
import com.tulin.v8.webtools.ide.text.AbsTextSourceViewerConfiguration;

/**
 * SourceViewerConfiguration implementation for JavaScriptEditor.
 */
public class JavaScriptConfiguration extends AbsTextSourceViewerConfiguration {
	private ITextEditor editor;
	private ColorProvider colorProvider;
	private RuleBasedScanner commentScanner;
	private RuleBasedScanner jsdocScanner;
	private RuleBasedScanner defaultScanner;
	private JavaScriptAssistProcessor assistProcessor;
	private JavaScriptJsDocAssistProcessor jsDocAssistProcessor;
	private JavaScriptAutoEditStrategy autoEditStrategy;
	private JavaScriptDoubleClickStrategy doubleClickStrategy;
	private Set<IContentType> resolvedContentTypes;
	private Set<IContentType> fallbackContentTypes;
	private EditorContentAssistant contentAssistant;

	public JavaScriptConfiguration(ITextEditor editor, ColorProvider colorProvider) {
		this.editor = editor;
		this.colorProvider = colorProvider;
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new JavaScriptDoubleClickStrategy();
		}
		return doubleClickStrategy;
	}

	private RuleBasedScanner getCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new RuleBasedScanner();
			commentScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSCOMMENT));
		}
		return commentScanner;
	}

	private RuleBasedScanner getJsdocScanner() {
		if (jsdocScanner == null) {
			jsdocScanner = new RuleBasedScanner();
			jsdocScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_JSDOC));
		}
		return jsdocScanner;
	}

	private RuleBasedScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new JavaScriptScanner(colorProvider);
			defaultScanner.setDefaultReturnToken(colorProvider.getToken(WebToolsPlugin.PREF_COLOR_FG));
		}
		return defaultScanner;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, JavaScriptPartitionScanner.JS_COMMENT,
				JavaScriptPartitionScanner.JS_JSDOC };
	}

	public JavaScriptAssistProcessor getAssistProcessor() {
		if (assistProcessor == null) {
			assistProcessor = new JavaScriptAssistProcessor();
		}
		return assistProcessor;
	}

	public JavaScriptJsDocAssistProcessor getJsDocAssistProcessor() {
		if (jsDocAssistProcessor == null) {
			jsDocAssistProcessor = new JavaScriptJsDocAssistProcessor();
		}
		return jsDocAssistProcessor;
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
			return contentAssistant;
		} catch (Exception e) {
			ContentAssistant assistant = new ContentAssistant();
			assistant.enableAutoInsert(true);
			assistant.setContentAssistProcessor(getAssistProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
			assistant.setContentAssistProcessor(getJsDocAssistProcessor(), JavaScriptPartitionScanner.JS_JSDOC);
			assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
			assistant.install(sourceViewer);
			return assistant;
		}
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = null;

		dr = new DefaultDamagerRepairer(getDefaultScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getJsdocScanner());
		reconciler.setDamager(dr, JavaScriptPartitionScanner.JS_JSDOC);
		reconciler.setRepairer(dr, JavaScriptPartitionScanner.JS_JSDOC);

		dr = new DefaultDamagerRepairer(getCommentScanner());
		reconciler.setDamager(dr, JavaScriptPartitionScanner.JS_COMMENT);
		reconciler.setRepairer(dr, JavaScriptPartitionScanner.JS_COMMENT);

		return reconciler;
	}

	/**
	 * @since 2.0.5
	 */
	public JavaScriptAutoEditStrategy getAutoEditStrategy() {
		if (autoEditStrategy == null) {
			autoEditStrategy = new JavaScriptAutoEditStrategy();
		}
		return autoEditStrategy;
	}

	/**
	 * @since 2.0.3
	 */
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		return new IAutoEditStrategy[] { getAutoEditStrategy() };
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
