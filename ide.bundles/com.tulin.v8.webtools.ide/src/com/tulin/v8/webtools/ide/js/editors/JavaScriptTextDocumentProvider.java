package com.tulin.v8.webtools.ide.js.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class JavaScriptTextDocumentProvider extends TextFileDocumentProvider {

	protected FileInfo createFileInfo(Object element) throws CoreException {
		FileInfo info = super.createFileInfo(element);
		if (info == null) {
			info = createEmptyFileInfo();
		}
		IDocument document = info.fTextFileBuffer.getDocument();
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new JavaScriptPartitionScanner(),
					new String[] { JavaScriptPartitionScanner.JS_COMMENT, JavaScriptPartitionScanner.JS_JSDOC });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return info;
	}

}
