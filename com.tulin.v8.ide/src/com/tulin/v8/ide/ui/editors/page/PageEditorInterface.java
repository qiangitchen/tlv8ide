package com.tulin.v8.ide.ui.editors.page;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jsoup.nodes.Element;

import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;

@SuppressWarnings("restriction")
public interface PageEditorInterface {
	public WEBDesignEditorInterface getDesignEditor();

	public void setSourcePageText(String html);

	public void activhtmlEditor();

	public StructuredTextEditor getSourceEditor();

	public Element getPageDom();

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

	public StructuredTextEditor getTextEditor();

	public IStructuredModel getModel();

}
