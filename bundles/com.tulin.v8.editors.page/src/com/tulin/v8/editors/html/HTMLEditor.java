package com.tulin.v8.editors.html;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * 多页面编辑器中的HTML文件编辑
 * 
 * @see org.eclipse.wst.sse.ui.StructuredTextEditor
 * @see com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor
 */
public class HTMLEditor extends HTMLSourceEditor {

	public File getFile() {
		try {
			IFile file = ((FileEditorInput) getEditorInput()).getFile();
			return file.getLocation().makeAbsolute().toFile();
		} catch (Exception e) {
			return new File(getEditorInput().getToolTipText());
		}
	}

}
