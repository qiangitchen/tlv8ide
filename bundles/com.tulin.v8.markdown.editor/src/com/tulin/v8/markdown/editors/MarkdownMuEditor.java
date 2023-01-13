package com.tulin.v8.markdown.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

public class MarkdownMuEditor extends MultiPageEditorPart {
	MarkdownEditor editor;

	@Override
	protected void createPages() {
		try {
			editor = new MarkdownEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, "源码");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "MarkdownMuEditor", null, e.getStatus());
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void doSaveAs() {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO 自动生成的方法存根
		return false;
	}

}
