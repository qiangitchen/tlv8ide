package com.tulin.v8.editors.vue.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class VUETextDocumentProvider extends TextFileDocumentProvider{

	protected FileInfo createFileInfo(Object element) throws CoreException {
		FileInfo info = super.createFileInfo(element);
		if(info==null){
			info = createEmptyFileInfo();
		}
		IDocument document = info.fTextFileBuffer.getDocument();
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new VUEPartitionScanner(),
					new String[] {
						VUEPartitionScanner.HTML_TAG,
						VUEPartitionScanner.PREFIX_TAG,
						VUEPartitionScanner.HTML_COMMENT,
						VUEPartitionScanner.HTML_SCRIPT,
						VUEPartitionScanner.HTML_DOCTYPE,
						VUEPartitionScanner.HTML_DIRECTIVE,
						VUEPartitionScanner.JAVASCRIPT,
						VUEPartitionScanner.HTML_CSS});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return info;
	}
	
}
