package com.tulin.v8.editors.ini.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.tulin.v8.editors.ini.Activator;
import com.tulin.v8.editors.ini.editors.eclipse.InitializationFileDocumentSetupParticipant;

public class DocumentProvider extends TextFileDocumentProvider {
	private static final IContentType JAVA_PROPERTIES_FILE_CONTENT_TYPE = Platform.getContentTypeManager()
			.getContentType("org.eclipse.jdt.core.javaProperties");

	public DocumentProvider() {
		IDocumentProvider provider = new TextFileDocumentProvider();
		provider = new ForwardingDocumentProvider("___pf_partitioning", new InitializationFileDocumentSetupParticipant(),
				provider);
		setParentDocumentProvider(provider);
	}

	protected TextFileDocumentProvider.FileInfo createFileInfo(Object element) throws CoreException {
		if ((JAVA_PROPERTIES_FILE_CONTENT_TYPE == null) || (!(element instanceof IFileEditorInput))) {
			return null;
		}
		IFileEditorInput input = (IFileEditorInput) element;

		IFile file = input.getFile();
		if ((file == null) || (!file.isAccessible())) {
			return null;
		}
		IContentDescription description = file.getContentDescription();
		if ((description == null) || (description.getContentType() == null)
				|| (!description.getContentType().isKindOf(JAVA_PROPERTIES_FILE_CONTENT_TYPE))) {
			return null;
		}
		Activator.getDefault().setReadingPropertiesDocument(true);
		TextFileDocumentProvider.FileInfo result;
		try {
			result = super.createFileInfo(element);
		} finally {
			Activator.getDefault().setReadingPropertiesDocument(false);
		}

		return result;
	}

	protected TextFileDocumentProvider.DocumentProviderOperation createSaveOperation(Object element, IDocument document,
			boolean overwrite) throws CoreException {
		if (getFileInfo(element) == null) {
			return null;
		}
		return super.createSaveOperation(element, document, overwrite);
	}

	public void resetDocument(Object element) throws CoreException {
		Activator.getDefault().setReadingPropertiesDocument(true);
		try {
			super.resetDocument(element);
		} finally {
			Activator.getDefault().setReadingPropertiesDocument(false);
		}
	}

	protected void commitFileBuffer(IProgressMonitor monitor, TextFileDocumentProvider.FileInfo info, boolean overwrite)
			throws CoreException {
		IDocument document = info.fTextFileBuffer.getDocument();
		if ((document instanceof InitializationDocument)) {
			InitializationDocument pd = (InitializationDocument) document;
			pd.setSaving(true);
			try {
				super.commitFileBuffer(monitor, info, overwrite);
			} finally {
				pd.setSaving(false);
			}
		} else {
			super.commitFileBuffer(monitor, info, overwrite);
		}
	}
}