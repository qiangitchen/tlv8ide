package com.tulin.v8.editors.page;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.internal.genericeditor.ExtensionBasedTextEditor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;

@SuppressWarnings("restriction")
public interface PageEditorInterface {
	public WEBDesignEditorInterface getDesignEditor();

	public void setSourcePageText(String html);

	public void activhtmlEditor();

	public ExtensionBasedTextEditor getSourceEditor();

	public int getDesignerMode();

	public void changeJsSourse(String text, String text2);

	public void activJsEditor();

	public AbstractDecoratedTextEditor getJSEditor();

	public String getJSEditorText();

	public void setJSEditorPageText(String string);

	public void doSave(IProgressMonitor monitor);

	public void doSaveAs();

	public boolean isDirty();

	public boolean isSaveAsAllowed();

	public IEditorInput getEditorInput();

}
