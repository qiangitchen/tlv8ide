package com.tulin.v8.editors.css;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.internal.genericeditor.ExtensionBasedTextEditor;
import org.eclipse.ui.part.FileEditorInput;

@SuppressWarnings("restriction")
public class CSSEditor extends ExtensionBasedTextEditor {
	public CSSEditor() {
		super();
	}

	public boolean isFileEditorInput() {
		if (getEditorInput() instanceof IFileEditorInput) {
			return true;
		}
		return false;
	}

	public File getTempFile() {
		IFile file = ((FileEditorInput) this.getEditorInput()).getFile();
		return new File(file.getLocation().makeAbsolute().toFile()
				.getParentFile(), "." + file.getName());
	}
}
