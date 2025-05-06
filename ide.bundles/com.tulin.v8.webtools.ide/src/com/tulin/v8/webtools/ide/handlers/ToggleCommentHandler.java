package com.tulin.v8.webtools.ide.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.css.editors.CSSEditor;
import com.tulin.v8.webtools.ide.html.editors.HTMLEditor;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;
import com.tulin.v8.webtools.ide.js.editors.JavaScriptEditor;
import com.tulin.v8.webtools.ide.xml.editors.XMLEditor;

public class ToggleCommentHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = null;
		if (event == null)
			window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		else {
			window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		}
		IWorkbenchPage page = window.getActivePage();
		if (page.getActiveEditor() instanceof MultiPageEditorPart) {
			MultiPageEditorPart editor = (MultiPageEditorPart) page.getActiveEditor();
			if (editor.getSelectedPage() instanceof XMLEditor) {
				XMLEditor xmleditor = (XMLEditor) editor.getSelectedPage();
				toggleComment(xmleditor);
			} else if (editor.getSelectedPage() instanceof HTMLSourceEditor) {
				HTMLSourceEditor htmleditor = (HTMLSourceEditor) editor.getSelectedPage();
				toggleComment(htmleditor);
			} else if (editor.getSelectedPage() instanceof JavaScriptEditor) {
				JavaScriptEditor jseditor = (JavaScriptEditor) editor.getSelectedPage();
				toggleJSComment(jseditor);
			} else if (editor.getSelectedPage() instanceof CSSEditor) {
				CSSEditor csseditor = (CSSEditor) editor.getSelectedPage();
				toggleCSSComment(csseditor);
			}
		} else if (page.getActiveEditor() instanceof XMLEditor) {
			XMLEditor editor = (XMLEditor) page.getActiveEditor();
			toggleComment(editor);
		} else if (page.getActiveEditor() instanceof HTMLEditor) {
			HTMLSourceEditor editor = ((HTMLEditor) page.getActiveEditor()).getPaletteTarget();
			toggleComment(editor);
		} else if (page.getActiveEditor() instanceof JavaScriptEditor) {
			JavaScriptEditor editor = (JavaScriptEditor) page.getActiveEditor();
			toggleJSComment(editor);
		} else if (page.getActiveEditor() instanceof CSSEditor) {
			CSSEditor csseditor = (CSSEditor) page.getActiveEditor();
			toggleCSSComment(csseditor);
		}
		return null;
	}

	public static void toggleComment(HTMLSourceEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = sel.getText().trim();
		if ("".equals(text)) {
			return;
		}
		try {
			if (text.startsWith("<!--") && text.indexOf("-->") > 3) {
				text = sel.getText().replaceFirst("<!--", "");
				text = text.replaceFirst("-->", "");
				doc.replace(sel.getOffset(), sel.getLength(), text);
			} else {
				doc.replace(sel.getOffset(), sel.getLength(), "<!--" + sel.getText() + "-->");
			}
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

	public static void toggleJSComment(JavaScriptEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		try {
			int startline = sel.getStartLine();
			int endline = sel.getEndLine();
			int offset = doc.getLineOffset(startline);
			int length = doc.getLineOffset(endline) + doc.getLineLength(endline) - offset - 2;
			String text = doc.get(offset, length);
			if (startline == endline) {// 单行注释/取消注释
				if (text.trim().startsWith("//")) {
					text = text.replaceFirst("//", "");
				} else {
					text = "//" + text;
				}
			} else {// 多行注释/取消注释
				if (text.trim().startsWith("//")) {
					text = text.replaceAll("(^|\r\n|\r|\n)//", "$1");
				} else {
					text = text.replaceAll("(^|\r\n|\r|\n)", "$1//");
				}
			}
			doc.replace(offset, length, text);
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

	public static void toggleCSSComment(CSSEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = sel.getText().trim();
		if ("".equals(text)) {
			return;
		}
		try {
			if (text.startsWith("/*") && text.indexOf("*/") > 3) {
				text = text.replaceFirst("/\\*", "");
				text = text.replaceFirst("\\*/", "");
				doc.replace(sel.getOffset(), sel.getLength(), text);
			} else {
				doc.replace(sel.getOffset(), sel.getLength(), "/*" + sel.getText() + "*/");
			}
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

}
