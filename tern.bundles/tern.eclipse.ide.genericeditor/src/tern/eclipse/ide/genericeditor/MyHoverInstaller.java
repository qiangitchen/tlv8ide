package tern.eclipse.ide.genericeditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.ITextEditor;

import tern.eclipse.ide.genericeditor.hover.DelegatingFallbackHover;

public class MyHoverInstaller implements IPartListener2 {

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		if (part instanceof ITextEditor editor) {
			Object adapter = editor.getAdapter(ITextViewer.class);
			if (adapter instanceof ISourceViewer) {
				ISourceViewer viewer = (ISourceViewer) adapter;
				viewer.setTextHover(new DelegatingFallbackHover(editor, viewer), IDocument.DEFAULT_CONTENT_TYPE);
			}
		}
	}

}
