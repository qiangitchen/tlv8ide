package com.tulin.v8.webtools.ide.jsp.editors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.eclipse.ui.IFileEditorInput;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.editors.HTMLEditor;
import com.tulin.v8.webtools.ide.html.editors.HTMLEditorPart;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;

/**
 * JSP editor.
 */
public class JSPEditor extends HTMLEditor {

	public JSPEditor() {
		super();
	}

	@Override
	protected HTMLSourceEditor createHTMLSourceEditor() {
		return new JSPSourceEditor();
	}

	/**
	 * Update preview.
	 */
	@Override
	public void updatePreview() {
		if (!(editor instanceof HTMLEditorPart)) {
			return;
		}
		try {
			if (!((HTMLEditorPart) editor).isFileEditorInput()) {
				return;
			}
			// write to temporary file
			HTMLEditorPart editor = (HTMLEditorPart) this.editor;
			IFileEditorInput input = (IFileEditorInput) this.editor.getEditorInput();
			String charset = input.getFile().getCharset();
			String html = editor.getSourceEditor().getDocumentProvider().getDocument(input).get();
			// replace JSP parts
			html = JSPPreviewConverter.convertJSP((IFileEditorInput) getEditorInput(), html);

			File tmpFile = editor.getSourceEditor().getTempFile();
			FileOutputStream out = new FileOutputStream(tmpFile);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, charset), true);
			pw.write(html);
			pw.close();

			if (prevTempFile != null && prevTempFile.equals(tmpFile)) {
				editor.getBrowser().refresh();
			} else {
				if (prevTempFile != null) {
					prevTempFile.delete();
				}
				prevTempFile = tmpFile;
				editor.getBrowser().setUrl("file://" + tmpFile.getAbsolutePath()); //$NON-NLS-1$
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
			// ex.printStackTrace();
		}
	}
}
