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
	/* 42 */ private static final IContentType PROPERTIES_CONTENT_TYPE = Platform.getContentTypeManager()
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
		/* 78 */ super(preferencesStore);
		/* 79 */ this.fColorManager = colorManager;
		/* 80 */ this.fTextEditor = textEditor;
		/* 81 */ this.fDocumentPartitioning = partitioning;
		/* 82 */ initializeScanners();
	}

	protected IColorManager getColorManager() {
		/* 92 */ return this.fColorManager;
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		/* 98 */ if (this.doubleClickStrategy == null)
			/* 99 */ this.doubleClickStrategy = new InitializationDoubleClickStrategy();
		/* 100 */ return this.doubleClickStrategy;
	}

	private void initializeScanners() {
		/* 108 */ this.fPropertyKeyScanner = new SingleTokenJavaScanner(getColorManager(), this.fPreferenceStore,
				"pf_coloring_key");
		/* 109 */ this.fCommentScanner = new SingleTokenJavaScanner(getColorManager(), this.fPreferenceStore,
				"pf_coloring_comment");
		/* 110 */ this.fPropertyValueScanner = new PropertyValueScanner(getColorManager(), this.fPreferenceStore);
	}

	protected RuleBasedScanner getPropertyKeyScanner() {
		/* 120 */ return this.fPropertyKeyScanner;
	}

	protected RuleBasedScanner getCommentScanner() {
		/* 130 */ return this.fCommentScanner;
	}

	protected RuleBasedScanner getPropertyValueScanner() {
		/* 140 */ return this.fPropertyValueScanner;
	}

	protected ITextEditor getEditor() {
		/* 149 */ return this.fTextEditor;
	}

	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		/* 157 */ if (this.fDocumentPartitioning != null)
			/* 158 */ return this.fDocumentPartitioning;
		/* 159 */ return super.getConfiguredDocumentPartitioning(sourceViewer);
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		/* 169 */ PresentationReconciler reconciler = new JavaPresentationReconciler();
		/* 170 */ reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		/* 172 */ DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getPropertyKeyScanner());
		/* 173 */ reconciler.setDamager(dr, "__dftl_partition_content_type");
		/* 174 */ reconciler.setRepairer(dr, "__dftl_partition_content_type");

		/* 176 */ dr = new DefaultDamagerRepairer(getCommentScanner());
		/* 177 */ reconciler.setDamager(dr, "__pf_comment");
		/* 178 */ reconciler.setRepairer(dr, "__pf_comment");

		/* 180 */ dr = new DefaultDamagerRepairer(getPropertyValueScanner());
		/* 181 */ reconciler.setDamager(dr, "__pf_roperty_value");
		/* 182 */ reconciler.setRepairer(dr, "__pf_roperty_value");

		/* 184 */ return reconciler;
	}

	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		/* 197 */ return (this.fPropertyKeyScanner.affectsBehavior(event)) ||
		/* 196 */ (this.fCommentScanner.affectsBehavior(event)) ||
		/* 197 */ (this.fPropertyValueScanner.affectsBehavior(event));
	}

	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		/* 208 */ if (this.fPropertyKeyScanner.affectsBehavior(event))
			/* 209 */ this.fPropertyKeyScanner.adaptToPreferenceChange(event);
		/* 210 */ if (this.fCommentScanner.affectsBehavior(event))
			/* 211 */ this.fCommentScanner.adaptToPreferenceChange(event);
		/* 212 */ if (this.fPropertyValueScanner.affectsBehavior(event))
			/* 213 */ this.fPropertyValueScanner.adaptToPreferenceChange(event);
	}

	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		/* 220 */ if (!EditorsUI.getPreferenceStore().getBoolean("spellingEnabled")) {
			/* 221 */ return null;
		}
		/* 223 */ IReconcilingStrategy strategy = new SpellingReconcileStrategy(sourceViewer,
				EditorsUI.getSpellingService()) {
			protected IContentType getContentType() {
				/* 225 */ return InitializationConfiguration.PROPERTIES_CONTENT_TYPE;
			}
		};
		/* 229 */ MonoReconciler reconciler = new MonoReconciler(strategy, false);
		/* 230 */ reconciler.setIsIncrementalReconciler(false);
		/* 231 */ reconciler.setProgressMonitor(new NullProgressMonitor());
		/* 232 */ reconciler.setDelay(500);
		/* 233 */ return reconciler;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		/* 241 */ int length = IPropertiesFilePartitions.PARTITIONS.length;
		/* 242 */ String[] contentTypes = new String[length + 1];
		/* 243 */ contentTypes[0] = "__dftl_partition_content_type";
		/* 244 */ for (int i = 0; i < length; i++) {
			/* 245 */ contentTypes[(i + 1)] = IPropertiesFilePartitions.PARTITIONS[i];
		}
		/* 247 */ return contentTypes;
	}
}