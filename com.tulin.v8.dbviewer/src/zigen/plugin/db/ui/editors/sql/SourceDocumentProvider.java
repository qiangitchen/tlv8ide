package zigen.plugin.db.ui.editors.sql;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.editors.text.StorageDocumentProvider;

public class SourceDocumentProvider extends StorageDocumentProvider {

	public SourceDocumentProvider() {}

	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
		SourceEditorInput input = (SourceEditorInput) element;
		String source = document.get();
	}

	public IAnnotationModel getAnnotationModel(Object element) {
		return new AnnotationModel();
	}
}
