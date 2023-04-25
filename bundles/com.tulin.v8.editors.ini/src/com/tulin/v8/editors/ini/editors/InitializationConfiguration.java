package com.tulin.v8.editors.ini.editors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.spelling.SpellingReconcileStrategy;

import com.tulin.v8.editors.ini.editors.eclipse.AbstractJavaScanner;
import com.tulin.v8.editors.ini.editors.eclipse.IPropertiesFilePartitions;
import com.tulin.v8.editors.ini.editors.eclipse.JavaPresentationReconciler;
import com.tulin.v8.editors.ini.editors.eclipse.PropertyValueScanner;
import com.tulin.v8.editors.ini.editors.eclipse.SingleTokenJavaScanner;

public class InitializationConfiguration extends TextSourceViewerConfiguration {
	private static final IContentType PROPERTIES_CONTENT_TYPE = Platform.getContentTypeManager()
			.getContentType("org.eclipse.jdt.core.javaProperties");
	private ITextEditor fTextEditor;
	private String fDocumentPartitioning;
	private InitializationDoubleClickStrategy doubleClickStrategy;
	private AbstractJavaScanner fPropertyKeyScanner;
	private AbstractJavaScanner fCommentScanner;
	private AbstractJavaScanner fPropertyValueScanner;
	private IColorManager fColorManager;

	public InitializationConfiguration(ITextEditor textEditor, IPreferenceStore preferencesStore,
			IColorManager colorManager, String partitioning) {
		super(preferencesStore);
		this.fColorManager = colorManager;
		this.fTextEditor = textEditor;
		this.fDocumentPartitioning = partitioning;
		initializeScanners();
	}

	protected IColorManager getColorManager() {
		return this.fColorManager;
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (this.doubleClickStrategy == null)
			this.doubleClickStrategy = new InitializationDoubleClickStrategy();
		return this.doubleClickStrategy;
	}

	private void initializeScanners() {
		this.fPropertyKeyScanner = new SingleTokenJavaScanner(getColorManager(), this.fPreferenceStore,
				"pf_coloring_key");
		this.fCommentScanner = new SingleTokenJavaScanner(getColorManager(), this.fPreferenceStore,
				"pf_coloring_comment");
		this.fPropertyValueScanner = new PropertyValueScanner(getColorManager(), this.fPreferenceStore);
	}

	protected RuleBasedScanner getPropertyKeyScanner() {
		return this.fPropertyKeyScanner;
	}

	protected RuleBasedScanner getCommentScanner() {
		return this.fCommentScanner;
	}

	protected RuleBasedScanner getPropertyValueScanner() {
		return this.fPropertyValueScanner;
	}

	protected ITextEditor getEditor() {
		return this.fTextEditor;
	}

	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		if (this.fDocumentPartitioning != null)
			return this.fDocumentPartitioning;
		return super.getConfiguredDocumentPartitioning(sourceViewer);
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new JavaPresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getPropertyKeyScanner());
		reconciler.setDamager(dr, "__dftl_partition_content_type");
		reconciler.setRepairer(dr, "__dftl_partition_content_type");

		dr = new DefaultDamagerRepairer(getCommentScanner());
		reconciler.setDamager(dr, "__pf_comment");
		reconciler.setRepairer(dr, "__pf_comment");

		dr = new DefaultDamagerRepairer(getPropertyValueScanner());
		reconciler.setDamager(dr, "__pf_roperty_value");
		reconciler.setRepairer(dr, "__pf_roperty_value");

		return reconciler;
	}

	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		return (this.fPropertyKeyScanner.affectsBehavior(event)) || (this.fCommentScanner.affectsBehavior(event))
				|| (this.fPropertyValueScanner.affectsBehavior(event));
	}

	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (this.fPropertyKeyScanner.affectsBehavior(event))
			this.fPropertyKeyScanner.adaptToPreferenceChange(event);
		if (this.fCommentScanner.affectsBehavior(event))
			this.fCommentScanner.adaptToPreferenceChange(event);
		if (this.fPropertyValueScanner.affectsBehavior(event))
			this.fPropertyValueScanner.adaptToPreferenceChange(event);
	}

	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		if (!EditorsUI.getPreferenceStore().getBoolean("spellingEnabled")) {
			return null;
		}
		IReconcilingStrategy strategy = new SpellingReconcileStrategy(sourceViewer, EditorsUI.getSpellingService()) {
			protected IContentType getContentType() {
				return InitializationConfiguration.PROPERTIES_CONTENT_TYPE;
			}
		};
		MonoReconciler reconciler = new MonoReconciler(strategy, false);
		reconciler.setIsIncrementalReconciler(false);
		reconciler.setProgressMonitor(new NullProgressMonitor());
		reconciler.setDelay(500);
		return reconciler;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		int length = IPropertiesFilePartitions.PARTITIONS.length;
		String[] contentTypes = new String[length + 1];
		contentTypes[0] = "__dftl_partition_content_type";
		for (int i = 0; i < length; i++) {
			contentTypes[(i + 1)] = IPropertiesFilePartitions.PARTITIONS[i];
		}
		return contentTypes;
	}
}