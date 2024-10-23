package com.tulin.v8.webtools.ide.html.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import com.tulin.v8.webtools.ide.html.HTMLPartitionScanner;

public class HTMLFileDocumentProvider extends FileDocumentProvider {
	
	public IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if(document!=null){
			IDocumentPartitioner partitioner =
				new FastPartitioner(
						new HTMLPartitionScanner(),
						new String[]{
								HTMLPartitionScanner.HTML_TAG,
								HTMLPartitionScanner.HTML_COMMENT,
								HTMLPartitionScanner.HTML_SCRIPT,
								HTMLPartitionScanner.HTML_DOCTYPE,
								HTMLPartitionScanner.HTML_DIRECTIVE,
								HTMLPartitionScanner.JAVASCRIPT,
								HTMLPartitionScanner.HTML_CSS});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
	
}
