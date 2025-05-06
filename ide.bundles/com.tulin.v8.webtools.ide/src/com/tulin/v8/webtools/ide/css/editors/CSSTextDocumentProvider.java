package com.tulin.v8.webtools.ide.css.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class CSSTextDocumentProvider extends TextFileDocumentProvider {

	@Override
	protected FileInfo createFileInfo(Object element) throws CoreException {
		FileInfo info = super.createFileInfo(element);
		if (info == null) {
			info = createEmptyFileInfo();
		}
		IDocument document = info.fTextFileBuffer.getDocument();
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new CSSPartitionScanner(),
					new String[] { CSSPartitionScanner.CSS_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return info;
	}

}
