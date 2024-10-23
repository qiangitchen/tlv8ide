package com.tulin.v8.webtools.ide.formatter;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.editors.text.TextEditor;

import com.tulin.v8.webtools.ide.css.editors.CSSEditor;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;
import com.tulin.v8.webtools.ide.js.editors.JavaScriptEditor;
import com.tulin.v8.webtools.ide.xml.editors.XMLEditor;

/**
 * 通用格式化
 * 
 * @author chenqian
 *
 */
public class Formater {

	public void format(TextEditor editor) throws ExecutionException {
		if (editor instanceof XMLEditor) {
			new FormatXMLAction(editor).run();
		} else if (editor instanceof HTMLSourceEditor) {
			try {
				String tText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
				ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
				String stext = sel.getText().trim();
				if (("".equals(stext)) || (stext == null)) {
					String text = HtmlFormator.format(tText);
					editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(text);
					return;
				}
				String text = HtmlFormator.format(stext);
				IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
				doc.replace(sel.getOffset(), sel.getLength(), text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (editor instanceof JavaScriptEditor) {
			try {
				String tText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
				ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
				String stext = sel.getText().trim();
				if (("".equals(stext)) || (stext == null)) {
					String text = JavascriptFormator.format(tText);
					editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(text);
					return;
				}
				String text = JavascriptFormator.format(stext);
				IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
				doc.replace(sel.getOffset(), sel.getLength(), text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (editor instanceof CSSEditor) {
			try {
				String tText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
				ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
				String stext = sel.getText().trim();
				if (("".equals(stext)) || (stext == null)) {
					String text = CSSFormator.format(tText);
					editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(text);
					return;
				}
				String text = CSSFormator.format(stext);
				IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
				doc.replace(sel.getOffset(), sel.getLength(), text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
