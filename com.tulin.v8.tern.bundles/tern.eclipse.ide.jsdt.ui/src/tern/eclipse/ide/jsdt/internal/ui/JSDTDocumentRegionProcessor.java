package tern.eclipse.ide.jsdt.internal.ui;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.wst.sse.ui.internal.reconcile.DocumentRegionProcessor;

/**
 * Extends SSE {@link DocumentRegionProcessor} to be able to validate JSDT
 * Editor content with "org.eclipse.wst.sse.ui.sourcevalidation" (see
 * tern.eclipse.ide.linter.ui/plugin.xml) since JSDT Editor have not an SSE
 * IStructuredModel.
 * 
 */
@SuppressWarnings("restriction")
public class JSDTDocumentRegionProcessor extends DocumentRegionProcessor {

	@Override
	public synchronized void startReconciling() {
		super.startReconciling();
	}

	@Override
	protected IReconcilingStrategy getSpellcheckStrategy() {
		// don't use SSE spelling strategy.
		return null;
	}

	@Override
	protected String getContentType(IDocument doc) {
		return "org.eclipse.wst.jsdt.core.jsSource";
	}
}
