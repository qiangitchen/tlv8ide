package com.tulin.v8.webtools.ide.js.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class JavaScriptFileDocumentProvider extends FileDocumentProvider {
	public IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new JavaScriptPartitionScanner(),
					new String[] { JavaScriptPartitionScanner.JS_COMMENT, JavaScriptPartitionScanner.JS_JSDOC });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
