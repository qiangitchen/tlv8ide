package com.tulin.v8.ide.ui.editors.page;

import java.io.File;

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
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.ui.editors.Messages;
import com.tulin.v8.ide.ui.editors.css.CSSEditor;
import com.tulin.v8.ide.ui.editors.page.design.LinkHref;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditor;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.ide.ui.editors.javascript.JavaScriptEditor;

@SuppressWarnings("restriction")
public class WebPageEditor extends MultiPageEditorPart implements PageEditorInterface, IResourceChangeListener {
	public static String ID = "com.tulin.v8.ide.ui.editors.page.WebPageEditor";

	public StructuredTextEditor editor;
	public WEBDesignEditorInterface _designViewer;
	public JavaScriptEditor jseditor;
	public CSSEditor csseditor;
	public Document pageDom;
	public String editortext;

	private MutiPageContentOutlinePage mutiPageContentOutlinePage;

	public WebPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public StructuredTextEditor getSourceEditor() {
		return editor;
	}

	private void createPage0() {
		try {
			editor = new StructuredTextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.getString("TLEditor.pageEditor.1")); //$NON-NLS-1$
			setPartName(getEditorInput().getName());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("TLEditor.pageEditor.2"), null, //$NON-NLS-1$
					e.getStatus());
		}
	}

	/**
	 * Creates page 1 设计视图.
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
			jseditor = new JavaScriptEditor();
			File nf = new File(getJSpathName());
			IFile JSIFile = StudioPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getJSpathName()));
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

	/*
	 * CSS编辑
	 */
	void createPage3() {
		try {
			csseditor = new CSSEditor();
			File nf = new File(getCSSpathName());
			IFile CSSIFile = StudioPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getCSSpathName()));
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

	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
		createPage3();

		setActivePage(1);// 默认展示‘设计’
		editortext = getEditorText();
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		editor.dispose();
		jseditor.dispose();
		csseditor.dispose();
		editor = null;
		jseditor = null;
		csseditor = null;
		super.dispose();
	}

	/*
	 * 保存
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		File jsfile = new File(getJSpathName());
		if (jseditor.isDirty()) {
			try {
				String jseditorText = jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
				if (!jsfile.exists()) {
					jsfile.createNewFile();
					IFile JSIFile = StudioPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getJSpathName()));
					if (pageDom != null) {
						LinkHref.addScript(pageDom.getElementsByTag("head").first(), JSIFile.getName());
						setSourcePageText();
					}
					try {
						JSIFile.refreshLocal(0, null);
					} catch (CoreException localCoreException1) {
						localCoreException1.printStackTrace();
					}
					FileEditorInput localFileEditorInput1 = new FileEditorInput(JSIFile);
					jseditor.setInput(localFileEditorInput1);
					jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).set(jseditorText);
				}
			} catch (Exception e) {
				Sys.packErrMsg("JS:" + e.toString());
				e.printStackTrace();
			}
			jseditor.doSave(monitor);
		}
		File cssfile = new File(getCSSpathName());
		final IProgressMonitor monitors = monitor;
		if (csseditor.isDirty()) {
			boolean havefile = true;
			try {
				final String cseditorText = csseditor.getDocumentProvider().getDocument(csseditor.getEditorInput())
						.get();
				if (!cssfile.exists()) {
					cssfile.createNewFile();
					final IFile CSSIFile = StudioPlugin.getWorkspace().getRoot()
							.getFileForLocation(new Path(getCSSpathName()));
					if (pageDom != null) {
						LinkHref.addLink(pageDom.getElementsByTag("head").first(), CSSIFile.getName());
						setSourcePageText();
					}
					try {
						CSSIFile.refreshLocal(0, null);
					} catch (CoreException localCoreException1) {
						localCoreException1.printStackTrace();
					}
					try {
						FileEditorInput localFileEditorInput2 = new FileEditorInput(CSSIFile);
						csseditor.setInput(localFileEditorInput2);
						csseditor.getDocumentProvider().getDocument(csseditor.getEditorInput()).set(cseditorText);
						csseditor.doSave(monitors);
					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				Sys.packErrMsg("CSS:" + e.toString());
				e.printStackTrace();
			}
			if (havefile) {
				csseditor.doSave(monitor);
			}
		}
		getEditor(0).doSave(monitor);
	}

	/*
	 * (另存为)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		getEditor(0).doSaveAs();
		setInput(getEditor(0).getEditorInput());
		setPartName(getEditorInput().getName());
	}

	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	public void activhtmlEditor() {
		setActivePage(0);
	}

	public void activJsEditor() {
		setActivePage(2);
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		// super.init(site, editorInput);
		setSite(site);
		setInput(editorInput);
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	public void setOffset(int offset) {
		setActivePage(0);
		editor.selectAndReveal(offset, 0);
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(WebPageEditor.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
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

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 0) {
			act(this.editor);
			editortext = getEditorText();
		} else if (newPageIndex == 1) {
			if (!getEditorText().equals(editortext)) {
				setModel();
			}
			loadBrowser();
		} else if (newPageIndex == 2) {
			act(this.jseditor);
		} else if (newPageIndex == 3) {
			setCSSWords();
			act(this.csseditor);
		}
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

	private String getJSpathName() {
		IEditorInput curEditorInput = editor.getEditorInput();
		String editPath = editor.getTitleToolTip();
		if (curEditorInput instanceof FileEditorInput) {
			editPath = ((FileEditorInput) curEditorInput).getPath().makeAbsolute().toString();
		}
		String JSpathName = editPath.substring(0, editPath.lastIndexOf(".")) + ".js";
		return JSpathName;
	}

	/*
	 * JS editor
	 */
	void setJsWords() {
		try {
			File nf = new File(getJSpathName());
			IFile JSIFile = StudioPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getJSpathName()));
			if (JSIFile != null && !JSIFile.exists()) {
				LocalFile localLocalFile = new LocalFile(nf);
				FileStoreEditorInput localFileStoreEditorInput = new FileStoreEditorInput(localLocalFile);
				jseditor.setInput(localFileStoreEditorInput);
			} else {
				try {
					JSIFile.refreshLocal(0, null);
				} catch (CoreException localCoreException1) {
					localCoreException1.printStackTrace();
				}
				FileEditorInput localFileEditorInput1 = new FileEditorInput(JSIFile);
				jseditor.setInput(localFileEditorInput1);
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
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

	/*
	 * CSS editor
	 */
	void setCSSWords() {
		File nf = new File(getCSSpathName());
		IFile CSSIFile = StudioPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getCSSpathName()));
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

	// 加載页面模型
	public void setModel() {
		pageDom = _designViewer.setModel();
	}

	private void loadBrowser() {
		_designViewer.loadBrowser();
	}

	public WEBDesignEditorInterface getDesignEditor() {
		return _designViewer;
	}

	public String getEditorText() {
		return editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
	}

	public JavaScriptEditor getJSEditor() {
		return jseditor;
	}

	public String getJSEditorText() {
		return jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
	}

	public void setSourcePageText() {
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(pageDom.html());
	}

	public void setSourcePageText(String text) {
		editor.getDocumentProvider().getDocument(editor.getEditorInput())
				.set(text.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n", ""));
	}

	public void setJSEditorPageText(String text) {
		jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).set(text);
	}

	public void changeJsSourse(String oldid, String elementid) {
		String text = jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
		text = text.replace("document.getElementById(\"" + oldid + "\")",
				"document.getElementById(\"" + elementid + "\")");
		text = text.replace("document.getElementById('" + oldid + "')",
				"document.getElementById(\"" + elementid + "\")");
		text = text.replace("$(\"#" + oldid + "\")", "$(\"#" + elementid + "\")");
		text = text.replace("$('#" + oldid + "')", "$(\"#" + elementid + "\")");
		System.out.println(text);
		jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).set(text);
	}

	@Override
	public Element getPageDom() {
		return pageDom;
	}

	@Override
	public int getDesignerMode() {
		return 2;
	}

	@Override
	public StructuredTextEditor getTextEditor() {
		return editor;
	}

	private IStructuredModel _model;

	@Override
	public IStructuredModel getModel() {
		if (_model == null) {
			if (editor != null) {
				IDocumentProvider documentProvider = editor.getDocumentProvider();
				if (documentProvider != null) {
					IDocument document = documentProvider.getDocument(editor.getEditorInput());
					if (document instanceof IStructuredDocument) {
						IModelManager modelManager = StructuredModelManager.getModelManager();
						if (modelManager != null) {
							_model = modelManager.getExistingModelForEdit(document);
							if (_model == null) {
								_model = modelManager.getModelForEdit((IStructuredDocument) document);
							}
						}
					}
				}
			}
		}
		return _model;
	}

}
