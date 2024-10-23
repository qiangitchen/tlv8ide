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

public class MultilineCommentHandler extends AbstractHandler implements IHandler{

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
				ToggleCommentHandler.toggleComment(xmleditor);
			} else if (editor.getSelectedPage() instanceof HTMLSourceEditor) {
				HTMLSourceEditor htmleditor = (HTMLSourceEditor) editor.getSelectedPage();
				ToggleCommentHandler.toggleComment(htmleditor);
			} else if (editor.getSelectedPage() instanceof JavaScriptEditor) {
				JavaScriptEditor jseditor = (JavaScriptEditor) editor.getSelectedPage();
				JSComment(jseditor);
			} else if (editor.getSelectedPage() instanceof CSSEditor) {
				CSSEditor jseditor = (CSSEditor) editor.getSelectedPage();
				CSSComment(jseditor);
			}
		} else if (page.getActiveEditor() instanceof XMLEditor) {
			XMLEditor editor = (XMLEditor) page.getActiveEditor();
			ToggleCommentHandler.toggleComment(editor);
		} else if (page.getActiveEditor() instanceof HTMLEditor) {
			HTMLSourceEditor editor = ((HTMLEditor) page.getActiveEditor()).getPaletteTarget();
			ToggleCommentHandler.toggleComment(editor);
		} else if (page.getActiveEditor() instanceof JavaScriptEditor) {
			JavaScriptEditor editor = (JavaScriptEditor) page.getActiveEditor();
			JSComment(editor);
		} else if (page.getActiveEditor() instanceof CSSEditor) {
			CSSEditor editor = (CSSEditor) page.getActiveEditor();
			CSSComment(editor);
		}
		return null;
	}

	public static void JSComment(JavaScriptEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = sel.getText();
		if ("".equals(text)) {
			return;
		}
		try {
			if (text.startsWith("/*") && text.indexOf("*/") > 3) {
			} else {
				doc.replace(sel.getOffset(), sel.getLength(), "/*" + sel.getText() + "*/");
			}
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

	public static void CSSComment(CSSEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = sel.getText();
		if ("".equals(text)) {
			return;
		}
		try {
			if (text.startsWith("/*") && text.indexOf("*/") > 3) {
			} else {
				doc.replace(sel.getOffset(), sel.getLength(), "/*" + sel.getText() + "*/");
			}
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

}
