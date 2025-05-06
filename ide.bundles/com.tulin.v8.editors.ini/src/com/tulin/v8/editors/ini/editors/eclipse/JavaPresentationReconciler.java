package com.tulin.v8.editors.ini.editors.eclipse;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.PresentationReconciler;

public class JavaPresentationReconciler extends PresentationReconciler {
	private IDocument fLastDocument;

	public TextPresentation createRepairDescription(IRegion damage, IDocument document) {
		if (document != this.fLastDocument) {
			setDocumentToDamagers(document);
			setDocumentToRepairers(document);
			this.fLastDocument = document;
		}
		return createPresentation(damage, document);
	}
}