package com.tulin.v8.editors.html;

import java.io.File;

import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.core.resources.IFile;

import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class HTMLSourceEditor extends StructuredTextEditor {
	public HTMLSourceEditor() {
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

	public File getFile() {
		if (this.getEditorInput() instanceof FileEditorInput) {
			FileEditorInput fileeditorinput = (FileEditorInput) this
					.getEditorInput();
			IFile file = fileeditorinput.getFile();
			return file.getLocation().makeAbsolute().toFile();
		} else {
			File fle = new File(this.getEditorInput().getToolTipText());
			return fle;
		}

	}
}
