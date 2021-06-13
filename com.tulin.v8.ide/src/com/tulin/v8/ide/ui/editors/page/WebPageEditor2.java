package com.tulin.v8.ide.ui.editors.page;

import java.io.File;

import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jst.pagedesigner.tools.RangeSelectionTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jsoup.nodes.Element;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.Messages;
import com.tulin.v8.ide.ui.editors.css.CSSEditor;
import com.tulin.v8.ide.ui.editors.javascript.JavaScriptEditor;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.WEBPageDesignEditor;
import com.tulin.v8.ide.ui.editors.page.sash.SashEditorPart;

@SuppressWarnings("restriction")
public class WebPageEditor2 extends MultiPageEditorPart implements PageEditorInterface, IResourceChangeListener {

	public DesignerStructuredTextEditorJSP sourceeditor;

	/** The design viewer */
	private SimpleGraphicalEditor _designViewer;
	private DefaultEditDomain _editDomain;
	private WEBDesignEditorInterface designViewer;
	private SashEditorPart _sashEditorPart = null;

	public JavaScriptEditor jseditor;
	public CSSEditor csseditor;
	
	private MutiPageContentOutlinePage mutiPageContentOutlinePage;

	public WebPageEditor2() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public DesignerStructuredTextEditorJSP getSourceEditor() {
		return sourceeditor;
	}

	private void createPage0() {
		try {
			sourceeditor = new DesignerStructuredTextEditorJSP();
			int index = addPage(sourceeditor, getEditorInput());
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
			_designViewer = new SimpleGraphicalEditor(this, getEditDomain());
			designViewer = new WEBPageDesignEditor(sourceeditor, WebPageEditor2.this);
			_sashEditorPart = new SashEditorPart() {
				protected void createPages() throws PartInitException {
					addPage((IEditorPart) designViewer, getEditorInput());
					addPage(_designViewer, getEditorInput());
				}
			};
			_sashEditorPart.setOrientation(SWT.HORIZONTAL);
			_sashEditorPart.setMaximizedEditor(_designViewer);
			int designPageIndex = addPage(_sashEditorPart, getEditorInput());
			setPageText(designPageIndex, Messages.getString("TLEditor.pageEditor.3")); //$NON-NLS-1$
			
			connectDesignPage();
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("TLEditor.pageEditor.3"), null,
					e.getStatus());
		}
	}
	
	/**
	 * Connects the design viewer with the viewer selection manager. Should be
	 * done after createSourcePage() is done because we need to get the
	 * ViewerSelectionManager from the TextEditor. setModel is also done here
	 * because getModel() needs to reference the TextEditor.
	 */
	protected void connectDesignPage() {
		if (_designViewer != null) {
			_designViewer.setModel(getModel());
			// _designViewer.getSynchronizer().listenToModel(getModel());
			ISelectionProvider designSelectionProvider = _designViewer.getSite().getSelectionProvider();
			if (designSelectionProvider instanceof IPostSelectionProvider) {
				((IPostSelectionProvider) designSelectionProvider)
						.addPostSelectionChangedListener(new ISelectionChangedListener() {
							public void selectionChanged(SelectionChangedEvent event) {
								if (getActiveEditor() != sourceeditor) {
									_designViewer.getSynchronizer().selectionChanged(event);
								}
							}
						});
			} else {
				designSelectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						if (getActiveEditor() != sourceeditor) {
							_designViewer.getSynchronizer().selectionChanged(event);
						}
					}
				});
			}
			ISelectionProvider textSelectionProvider = sourceeditor.getSite().getSelectionProvider();
			if (textSelectionProvider instanceof IPostSelectionProvider) {
				((IPostSelectionProvider) textSelectionProvider)
						.addPostSelectionChangedListener(new ISelectionChangedListener() {
							public void selectionChanged(SelectionChangedEvent event) {
								if (event.getSelection() instanceof TextSelection) {
									TextSelection textSelection = ((TextSelection) event.getSelection());
									_designViewer.getSynchronizer().textSelectionChanged(textSelection.getOffset(),
											textSelection.getOffset() + textSelection.getLength());
								}
							}
						});
			} else {
				textSelectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						TextSelection textSelection = ((TextSelection) event.getSelection());
						_designViewer.getSynchronizer().textSelectionChanged(textSelection.getOffset(),
								textSelection.getOffset() + textSelection.getLength());
					}
				});
			}
		}
	}

	/**
	 * @return the edit domain
	 */
	public DefaultEditDomain getEditDomain() {
		if (_editDomain == null) {
			_editDomain = new DefaultEditDomain(this);

			// XXX: if i don't do the following line, system will default use
			// SelectionTool. Don't know where else to set this. Since it is
			// kind of duplicate
			// to the DesignerPaletteRoot.
			_editDomain.setDefaultTool(new RangeSelectionTool());
			_editDomain.loadDefaultTool();

			// next config the _editDomain
			// _editDomain.setPaletteRoot(new JSFPaletteRoot());
		}
		return _editDomain;
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

	@Override
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
		createPage3();
	}
	
	private void disconnectDesignPage() {
		if (_designViewer != null) {
			_designViewer.setModel(null);
			_designViewer.dispose();
		}
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		
		disconnectDesignPage();
		
		sourceeditor.dispose();
		jseditor.dispose();
		csseditor.dispose();
		sourceeditor = null;
		jseditor = null;
		csseditor = null;
		super.dispose();
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		try {
			return sourceeditor.isDirty() || jseditor.isDirty() || csseditor.isDirty();
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = sourceeditor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(WebPageEditor2.this, false);
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
			act(this.sourceeditor);
			// editortext = getEditorText();
		} else if (newPageIndex == 1) {
			// if (!getEditorText().equals(editortext)) {
			// setModel();
			// }
			// loadBrowser();
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
		IEditorInput curEditorInput = sourceeditor.getEditorInput();
		String editPath = sourceeditor.getTitleToolTip();
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
		IEditorInput curEditorInput = sourceeditor.getEditorInput();
		String editPath = sourceeditor.getTitleToolTip();
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

	@Override
	public WEBDesignEditorInterface getDesignEditor() {
		return designViewer;
	}

	@Override
	public void setSourcePageText(String html) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void activhtmlEditor() {
		setActivePage(0);
	}

	@Override
	public Element getPageDom() {
		// TODO 自动生成的方法存根
		return null;
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
		System.out.println(text);
		jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).set(text);
	}

	@Override
	public void activJsEditor() {
		setActivePage(2);
	}

	@Override
	public AbstractDecoratedTextEditor getJSEditor() {
		return sourceeditor;
	}

	@Override
	public String getJSEditorText() {
		return jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
	}

	@Override
	public void setJSEditorPageText(String text) {
		jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).set(text);
	}

	@Override
	public StructuredTextEditor getTextEditor() {
		return sourceeditor;
	}

	@Override
	public IStructuredModel getModel() {
		return sourceeditor.getStructuredModel();
	}

}
