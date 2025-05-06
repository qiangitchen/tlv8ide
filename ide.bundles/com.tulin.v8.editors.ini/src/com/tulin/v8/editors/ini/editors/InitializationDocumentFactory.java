package com.tulin.v8.editors.ini.editors;

import org.eclipse.core.filebuffers.IDocumentFactory;
import org.eclipse.jface.text.IDocument;

@SuppressWarnings("deprecation")
public class InitializationDocumentFactory implements IDocumentFactory {
	public IDocument createDocument() {
		return new InitializationDocument();
	}
}
