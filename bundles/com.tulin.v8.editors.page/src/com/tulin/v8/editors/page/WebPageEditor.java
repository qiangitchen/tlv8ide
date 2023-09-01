package com.tulin.v8.editors.page;

import java.io.File;
import java.io.StringWriter;
import java.text.Collator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.core.Sys;
import com.tulin.v8.editors.css.CSSEditor;
import com.tulin.v8.editors.page.design.LinkHref;
import com.tulin.v8.editors.page.design.WEBDesignEditor;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;

import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.genericeditor.ExtensionBasedTextEditor;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 * 
 * @author chenqian
 */
@SuppressWarnings({ "unused", "restriction" })
public class WebPageEditor extends MultiPageEditorPart implements PageEditorInterface, IResourceChangeListener {
	public final static String ID = "com.tulin.v8.editors.page.WebPageEditor";
	private ExtensionBasedTextEditor editor;
	public WEBDesignEditorInterface _designViewer;
	private CompilationUnitEditor jseditor;
	public CSSEditor csseditor;
	public String editortext;

	private MutiPageContentOutlinePage mutiPageContentOutlinePage;

	public WebPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage0() {
		try {
			editor = new ExtensionBasedTextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.getString("TLEditor.pageEditor.1"));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("TLEditor.pageEditor.2"), null,
					e.getStatus());
		}
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the font
	 * used in page 2.
	 */
	void createPage1() {
		try {
			_designViewer = new WEBDesignEditor(editor, this);
			int index = addPage((IEditorPart) _designViewer, getEditorInput());
			setPageText(index, Messages.getString("TLEditor.pageEditor.3"));
		} catch (PartInitException e1) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("TLEditor.pageEditor.4"), null,
					e1.getStatus());
		}
	}

	/**
	 * Creates page 2 JS编辑.
	 */
	void createPage2() {
		try {
			jseditor = new CompilationUnitEditor();
			File nf = new File(getJSpathName());
			IFile JSIFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getJSpathName()));
			int index = 2;
			if (JSIFile == null || !JSIFile.exists()) {
				LocalFile localLocalFile = new LocalFile(nf);
				FileStoreEditorInput localFileStoreEditorInput = new FileStoreEditorInput(localLocalFile);
				index = addPage(jseditor, localFileStoreEditorInput);
			} else {
				try {
					JSIFile.refreshLocal(0, null);
				} catch (CoreException localCoreException1) {
					localCoreException1.printStackTrace();
				}
				FileEditorInput localFileEditorInput1 = new FileEditorInput(JSIFile);
				index = addPage(jseditor, localFileEditorInput1);
			}
			setPageText(index, Messages.getString("TLEditor.pageEditor.5"));
			// setJsWords();
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("TLEditor.pageEditor.6"), null,
					e.getStatus());
		}
	}

	/**
	 * CSS编辑
	 */
	void createPage3() {
		try {
			csseditor = new CSSEditor();
			File nf = new File(getCSSpathName());
			IFile CSSIFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getCSSpathName()));
			int index = 3;
			if (CSSIFile == null || !CSSIFile.exists()) {
				LocalFile localLocalFile = new LocalFile(nf);
				FileStoreEditorInput localFileStoreEditorInput = new FileStoreEditorInput(localLocalFile);
				index = addPage(csseditor, localFileStoreEditorInput);
			} else {
				try {
					CSSIFile.refreshLocal(0, null);
				} catch (CoreException localCoreException1) {
					localCoreException1.printStackTrace();
				}
				FileEditorInput localFileEditorInput2 = new FileEditorInput(CSSIFile);
				index = addPage(csseditor, localFileEditorInput2);
			}
			setPageText(index, Messages.getString("TLEditor.pageEditor.7"));
			// setCSSWords();
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("TLEditor.pageEditor.8"), null,
					e.getStatus());
		}
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
		createPage3();
		setPartName(editor.getPartName());
		setActivePage(1);// 默认展示‘设计’
	}

	private String getJSpathName() {
		IEditorInput curEditorInput = editor.getEditorInput();
		String editPath = editor.getTitleToolTip();
		if (curEditorInput instanceof FileEditorInput) {
			editPath = ((FileEditorInput) curEditorInput).getPath().makeAbsolute().toString();
		}
		String JSpathName = editPath.substring(0, editPath.lastIndexOf(".")) + ".js";
		return JSpathName;
	}

	private String getCSSpathName() {
		IEditorInput curEditorInput = editor.getEditorInput();
		String editPath = editor.getTitleToolTip();
		if (curEditorInput instanceof FileEditorInput) {
			editPath = ((FileEditorInput) curEditorInput).getPath().makeAbsolute().toString();
		}
		String CSSpathName = editPath.substring(0, editPath.lastIndexOf(".")) + ".css";
		return CSSpathName;
	}

	void setCSSWords() {
		File nf = new File(getCSSpathName());
		IFile CSSIFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getCSSpathName()));
		if (CSSIFile != null && !CSSIFile.exists()) {
			LocalFile localLocalFile = new LocalFile(nf);
			FileStoreEditorInput localFileStoreEditorInput = new FileStoreEditorInput(localLocalFile);
			csseditor.setInput(localFileStoreEditorInput);
		} else {
			try {
				CSSIFile.refreshLocal(0, null);
			} catch (CoreException localCoreException1) {
				localCoreException1.printStackTrace();
			}
			FileEditorInput localFileEditorInput2 = new FileEditorInput(CSSIFile);
			csseditor.setInput(localFileEditorInput2);
		}
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors. Subclasses
	 * may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		File jsfile = new File(getJSpathName());
		Document pageDom = _designViewer.getPageDom();
		if (jseditor.isDirty()) {
			try {
				String jseditorText = jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
				if (!jsfile.exists()) {
					jsfile.createNewFile();
					IFile JSIFile = ResourcesPlugin.getWorkspace().getRoot()
							.getFileForLocation(new Path(getJSpathName()));
					if (pageDom != null) {
						LinkHref.addScript(pageDom.getElementsByTag("head").first(), JSIFile.getName());
						setSourcePageText(pageDom);
					}
					try {
						JSIFile.refreshLocal(0, monitor);
					} catch (CoreException localCoreException1) {
						localCoreException1.printStackTrace();
					}
				}
				jseditor.doSave(monitor);
			} catch (Exception e) {
				Sys.packErrMsg("JS:" + e.toString());
				e.printStackTrace();
			}
		}
		File cssfile = new File(getCSSpathName());
		if (csseditor.isDirty()) {
			try {
				String cseditorText = csseditor.getDocumentProvider().getDocument(csseditor.getEditorInput()).get();
				if (!cssfile.exists()) {
					cssfile.createNewFile();
					IFile CSSIFile = ResourcesPlugin.getWorkspace().getRoot()
							.getFileForLocation(new Path(getCSSpathName()));
					if (pageDom != null) {
						LinkHref.addLink(pageDom.getElementsByTag("head").first(), CSSIFile.getName());
						setSourcePageText(pageDom);
					}
					try {
						CSSIFile.refreshLocal(0, monitor);
					} catch (CoreException localCoreException1) {
						localCoreException1.printStackTrace();
					}
				}
				csseditor.doSave(monitor);
			} catch (Exception e) {
				Sys.packErrMsg("CSS:" + e.toString());
				e.printStackTrace();
			}
		}
		getEditor(0).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the text
	 * for page 0's tab, and updates this multi-page editor's input to correspond to
	 * the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method checks
	 * that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 0) {
			act(this.editor);
			editortext = getEditorText();
		} else if (newPageIndex == 1) {
			act(this.editor);
			if (!getEditorText().equals(editortext)) {
				setModel();
			}
			loadBrowser();
		} else if (newPageIndex == 2) {
			act(this.jseditor);
		} else if (newPageIndex == 3) {
			act(this.csseditor);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class adapter) {
		if ((adapter.equals(IContentOutlinePage.class))) {
			fiend();
			return mutiPageContentOutlinePage;
		}
		return super.getAdapter(adapter);
	}

	private void act(IEditorPart paramIEditorPart) {
		IContentOutlinePage localIContentOutlinePage = (IContentOutlinePage) paramIEditorPart
				.getAdapter(IContentOutlinePage.class);
		if (paramIEditorPart != null) {
			fiend();
			Display.getDefault().asyncExec(new Z());
			if ((localIContentOutlinePage != null) && (this.mutiPageContentOutlinePage != null))
				this.mutiPageContentOutlinePage.setPageActive(localIContentOutlinePage);
		}
	}

	private void fiend() {
		if ((this.mutiPageContentOutlinePage == null) || (this.mutiPageContentOutlinePage.isDisposed()))
			this.mutiPageContentOutlinePage = new MutiPageContentOutlinePage();
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput()).getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	public void setModel() {
		_designViewer.setModel();
	}

	private void loadBrowser() {
		_designViewer.loadBrowser();
	}

	@Override
	public WEBDesignEditorInterface getDesignEditor() {
		return _designViewer;
	}

	public String getEditorText() {
		return editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
	}

	@Override
	public void setSourcePageText(String text) {
		editor.getDocumentProvider().getDocument(editor.getEditorInput())
				.set(text.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n", ""));
	}

	@Override
	public void activhtmlEditor() {
		setActivePage(0);
	}

	@Override
	public ExtensionBasedTextEditor getSourceEditor() {
		return editor;
	}

	@Override
	public int getDesignerMode() {
		return 2;
	}

	@Override
	public void changeJsSourse(String oldid, String elementid) {
		String text = jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
		text = text.replace("document.getElementById(\"" + oldid + "\")",
				"document.getElementById(\"" + elementid + "\")");
		text = text.replace("document.getElementById('" + oldid + "')",
				"document.getElementById(\"" + elementid + "\")");
		text = text.replace("$(\"#" + oldid + "\")", "$(\"#" + elementid + "\")");
		text = text.replace("$('#" + oldid + "')", "$(\"#" + elementid + "\")");
		// System.out.println(text);
		jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).set(text);
	}

	@Override
	public void activJsEditor() {
		setActivePage(2);
	}

	@Override
	public AbstractDecoratedTextEditor getJSEditor() {
		return jseditor;
	}

	@Override
	public String getJSEditorText() {
		return jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
	}

	public void setSourcePageText(Document pageDom) {
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(pageDom.html());
	}

	@Override
	public void setJSEditorPageText(String text) {
		jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).set(text);
	}

	@Override
	public Object getSelectedPage() {
		int i = getActivePage();
		if (i == 2)
			return jseditor;
		if (i == 3)
			return csseditor;
		return editor;
	}
}
