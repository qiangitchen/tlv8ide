package com.tulin.v8.webtools.ide.css;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;

/**
 * This action opens {@link ColorDialog} and insert the selected color into the
 * caret position as &quot;#RRGGBB&quot; format.
 */
public class ChooseColorAction extends Action {

	private TextEditor editor;

	public ChooseColorAction(TextEditor editor) {
		super(WebToolsPlugin.getResourceString("CSSEditor.ChooseColor"));
		this.editor = editor;
	}

	@Override
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ColorDialog dialog = new ColorDialog(window.getShell());
		RGB color = dialog.open();
		if (color != null) {
			IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
			try {
				doc.replace(sel.getOffset(), 0, HTMLUtil.toHex(color));
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		}
	}
}
