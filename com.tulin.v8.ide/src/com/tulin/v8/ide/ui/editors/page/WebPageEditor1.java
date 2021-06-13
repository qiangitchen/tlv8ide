package com.tulin.v8.ide.ui.editors.page;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jst.jsf.common.ui.internal.logging.Logger;
import org.eclipse.jst.jsf.common.ui.internal.utils.ResourceUtils;
import org.eclipse.jst.pagedesigner.IJMTConstants;
import org.eclipse.jst.pagedesigner.PDPlugin;
import org.eclipse.jst.pagedesigner.dnd.internal.DesignerSourceMouseTrackAdapter;
import org.eclipse.jst.pagedesigner.editors.IDesignViewer;
import org.eclipse.jst.pagedesigner.editors.IPropertySheetPageFactory;
import org.eclipse.jst.pagedesigner.editors.actions.DesignPageActionContributor;
import org.eclipse.jst.pagedesigner.editors.pagedesigner.PageDesignerResources;
import org.eclipse.jst.pagedesigner.jsp.core.pagevar.IPageVariablesProvider;
import org.eclipse.jst.pagedesigner.jsp.core.pagevar.adapter.IDocumentPageVariableAdapter;
import org.eclipse.jst.pagedesigner.parts.DocumentEditPart;
import org.eclipse.jst.pagedesigner.preview.PreviewHandlerNew;
import org.eclipse.jst.pagedesigner.preview.WindowsIEBrowser;
import org.eclipse.jst.pagedesigner.tools.RangeSelectionTool;
import org.eclipse.jst.pagedesigner.ui.common.PartActivationHandler;
import org.eclipse.jst.pagedesigner.ui.preferences.PDPreferences;
import org.eclipse.jst.pagedesigner.utils.EditorUtil;
import org.eclipse.jst.pagedesigner.utils.PreviewUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.html.core.internal.document.DOMStyleModelImpl;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.ISourceEditingTextTools;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.provisional.IDOMSourceEditingTextTools;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.ui.editors.Messages;
import com.tulin.v8.ide.ui.editors.css.CSSEditor;
import com.tulin.v8.ide.ui.editors.javascript.JavaScriptEditor;
import com.tulin.v8.ide.ui.editors.page.IWPEPersistenceListener.IPersistenceEvent;
import com.tulin.v8.ide.ui.editors.page.IWPEPersistenceListener.PersistenceEventType;
import com.tulin.v8.ide.ui.editors.page.design.ElementLinkHref;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.WEBPageDesignEditor;
import com.tulin.v8.ide.ui.editors.page.properties.WPETabbedPropertySheetPage;
import com.tulin.v8.ide.ui.editors.page.sash.SashEditorPart;

@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class WebPageEditor1 extends MultiPageEditorPart implements PageEditorInterface, IPropertyListener,
		ITabbedPropertySheetPageContributor, IResourceChangeListener {
	// private static final String PAGE_NAME_DESIGN = "Design"; //$NON-NLS-1$
	// private static final String PAGE_NAME_SOURCE = "Source"; //$NON-NLS-1$

	public JavaScriptEditor jseditor;
	public CSSEditor csseditor;
	public String editortext;

	private MutiPageContentOutlinePage mutiPageContentOutlinePage;

	/**
	 * Tabbed property contributor id for WPE
	 */
	public final static String TABBED_PROPERTIES_CONTRIBUTOR_ID = "org.eclipse.jst.pagedesigner.tabPropertyContributor"; //$NON-NLS-1$

	// four different modes for the designer when displayed in a sash editor.
	/**
	 * editor split is vertical
	 */
	public static final int MODE_SASH_VERTICAL = 0;

	/**
	 * editor split is horizontal
	 */
	public static final int MODE_SASH_HORIZONTAL = 1;

	/**
	 * no split, only designer canvas
	 */
	public static final int MODE_DESIGNER = 2;

	/**
	 * no split, only SSE source
	 */
	public static final int MODE_SOURCE = 3;

	private Logger _log = PDPlugin.getLogger(WebPageEditor1.class);

	private boolean _sash = false;

	private int _mode = 2;

	private SashEditorPart _sashEditorPart = null;

	private int _previewPageIndex = -1;

	/** The design viewer */
	private SimpleGraphicalEditor _designViewer;

	/** The text editor. */
	private DesignerStructuredTextEditorJSP _textEditor;

	private PartActivationHandler _partListener;

	private PaletteViewerPage _paletteViewerPage;

	private DefaultEditDomain _editDomain;

	private WindowsIEBrowser _browser;

	private Composite _previewComposite;

	private List PREVIEW_FILES_LIST = new ArrayList();

	private IPropertySheetPage _tabbedPropSheet;

	private ISelectionChangedListener _selChangedListener;

	private DesignPageActionContributor _designPageActionContributor;

	private IStructuredModel _model;

	// TODO:This class is never used locally
	// private class TextInputListener implements ITextInputListener {
	// public void inputDocumentAboutToBeChanged(IDocument oldInput,
	// IDocument newInput) {
	// // do nothing
	// }
	//
	// public void inputDocumentChanged(IDocument oldInput, IDocument newInput)
	// {
	// if (_designViewer != null && newInput != null)
	// _designViewer.setModel(getModel());
	// }
	// }

	private List<IWPEPersistenceListener> persistenceListeners;
	private WEBDesignEditorInterface designViewer;

	/**
	 * Default constructor
	 */
	public WebPageEditor1() {
		super();
	}

	/*
	 * This method is just to make firePropertyChanged accessbible from some
	 * (anonomous) inner classes.
	 */
	private void _firePropertyChange(int property) {
		super.firePropertyChange(property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.xtools.common.ui.properties.ITabbedPropertySheetPageContributor#
	 * getContributorId()
	 */
	public String getContributorId() {
		return TABBED_PROPERTIES_CONTRIBUTOR_ID;
	}

	private void connectSashPage() {
		ISelectionProvider selectionProvider = _sashEditorPart.getSite().getSelectionProvider();
		if (selectionProvider instanceof IPostSelectionProvider) {
			((IPostSelectionProvider) selectionProvider)
					.addPostSelectionChangedListener(getSelectionChangedListener(selectionProvider));
		} else {
			selectionProvider.addSelectionChangedListener(getSelectionChangedListener(selectionProvider));
		}
	}

	private void disconnectSashPage() {
		// attempted fix for bug 283569... was not able to repro, but should
		// protect against NPE
		if (_sashEditorPart != null && _sashEditorPart.getSite() != null
				&& _sashEditorPart.getSite().getSelectionProvider() != null && _selChangedListener != null) {

			final ISelectionProvider selectionProvider = _sashEditorPart.getSite().getSelectionProvider();
			if (selectionProvider != null) {
				if (selectionProvider instanceof IPostSelectionProvider) {
					((IPostSelectionProvider) selectionProvider)
							.removePostSelectionChangedListener(getSelectionChangedListener(selectionProvider));
				} else {
					selectionProvider.removeSelectionChangedListener(getSelectionChangedListener(selectionProvider));
				}
			}
		}
	}

	private ISelectionChangedListener getSelectionChangedListener(ISelectionProvider selectionProvider) {
		if (_selChangedListener == null) {
			if (selectionProvider instanceof IPostSelectionProvider) {
				_selChangedListener = new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						((MultiPageSelectionProvider) getSite().getSelectionProvider()).firePostSelectionChanged(event);
					}
				};
			} else {
				_selChangedListener = new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						((MultiPageSelectionProvider) getSite().getSelectionProvider()).firePostSelectionChanged(event);
					}
				};
			}
		}
		return _selChangedListener;
	}

	/**
	 * Creates the source page of the multi-page editor.
	 * 
	 * @throws PartInitException
	 */
	protected void sash_createAndAddDesignSourcePage() throws PartInitException {
		// create source page
		_textEditor = createTextEditor();
		_textEditor.setEditorPart(this);
		_textEditor.addPropertyListener(this);
		// create design page
		_designViewer = new SimpleGraphicalEditor(this, getEditDomain());

		// create SashEditor
		_sashEditorPart = new SashEditorPart() {
			protected void createPages() throws PartInitException {
				addPage(_designViewer, getEditorInput());
				addPage(_textEditor, getEditorInput());
			}
		};
		int sashIndex = addPage(_sashEditorPart, getEditorInput());

		// Set the sash editor mode from the stored file property
		// or the default preference
		initDesignerMode();

		setPageText(sashIndex, Messages.getString("TLEditor.pageEditor.3")); //$NON-NLS-1$

		// the update's critical, to get viewer selection manager and
		// highlighting to work
		_textEditor.update();

		firePropertyChange(PROP_TITLE);

		// Changes to the Text Viewer's document instance should also force an
		// input refresh
		// _textEditor.getTextViewer().addTextInputListener(new
		// TextInputListener());
		connectSashPage();
	}

	/**
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createSite(org.eclipse.ui.IEditorPart)
	 */
	protected IEditorSite createSite(IEditorPart editor) {
		return new MultiPageEditorSite(this, editor);
	}

	private void tabbed_createAndAddDesignSourcePage() throws PartInitException {
		// create source page
		_textEditor = createTextEditor();
		_textEditor.setEditorPart(this);
		_textEditor.addPropertyListener(this);

		// add source page
		int sourcePageIndex = addPage(_textEditor, getEditorInput());
		setPageText(sourcePageIndex, Messages.getString("TLEditor.pageEditor.1")); //$NON-NLS-1$
		// the update's critical, to get viewer selection manager and
		// highlighting to work
		_textEditor.update();

		// create design page
		_designViewer = new SimpleGraphicalEditor(this, getEditDomain());
		designViewer = new WEBPageDesignEditor(_textEditor, WebPageEditor1.this);
		_sashEditorPart = new SashEditorPart() {
			protected void createPages() throws PartInitException {
				addPage((IEditorPart) designViewer, getEditorInput());
				addPage(_designViewer, getEditorInput());
			}
		};
		_sashEditorPart.setOrientation(SWT.HORIZONTAL);
		_sashEditorPart.setMaximizedEditor(_designViewer);
		// add design page
		int designPageIndex = addPage(_sashEditorPart, getEditorInput());

		// // note: By adding the design page as a Control instead of an
		// // IEditorPart, page switches will indicate
		// // a "null" active editor when the design page is made active
		setPageText(designPageIndex, Messages.getString("TLEditor.pageEditor.3")); //$NON-NLS-1$

		firePropertyChange(PROP_TITLE);

		// Changes to the Text Viewer's document instance should also force an
		// input refresh
		// _textEditor.getTextViewer().addTextInputListener(new
		// TextInputListener());
	}

	protected void createAndAddPreviewPage() {
		_previewComposite = new Composite(getContainer(), 0);
		FillLayout filllayout = new FillLayout();
		_previewComposite.setLayout(filllayout);

		_previewPageIndex = addPage(_previewComposite);
		// JSPSourceEditor.Page.Preview.PageText=Preview
		setPageText(_previewPageIndex,
				PageDesignerResources.getInstance().getString("JSPSourceEditor.Page.Preview.PageText")); //$NON-NLS-1$

	}

	private WindowsIEBrowser getPreviewBrowser() {
		if (_browser == null) {
			_browser = new WindowsIEBrowser();
			if (_browser != null) {
				_browser.create(_previewComposite, SWT.NONE);
				_previewComposite.layout();
			}
		}
		return _browser;
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
								if (getActiveEditor() != _textEditor) {
									_designViewer.getSynchronizer().selectionChanged(event);
								}
							}
						});
			} else {
				designSelectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						if (getActiveEditor() != _textEditor) {
							_designViewer.getSynchronizer().selectionChanged(event);
						}
					}
				});
			}
			ISelectionProvider textSelectionProvider = _textEditor.getSite().getSelectionProvider();
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

	/**
	 * Creates the pages of this multi-page editor.
	 * <p>
	 * Subclasses of <code>MultiPageEditor</code> must implement this method.
	 * </p>
	 */
	protected void createPages() {
		try {
			// source page MUST be created before design page, now
			if (_sash) {
				sash_createAndAddDesignSourcePage();
			} else {
				tabbed_createAndAddDesignSourcePage();
			}
			connectDesignPage();

			// show preview page unless preference is hiding for content type
			// boolean showPreviewPage = true;
			// final IEditorInput input = getEditorInput();
			// if (input instanceof FileEditorInput) {
			// final IFile file = ((FileEditorInput) input).getFile();
			// if (file != null) {
			// try {
			// final IContentDescription description =
			// file.getContentDescription();
			// if (description != null) {
			// final IContentType type = description.getContentType();
			// if (type != null) {
			// final String id = type.getId();
			// if (id != null && id.length() > 0) {
			// if
			// (Arrays.binarySearch(PDPreferences.getHiddenPreviewPageContentTypes(),
			// id) > -1) {
			// showPreviewPage = false;
			// }
			// }
			// }
			// }
			// } catch (CoreException cEx) {
			// // do nothing
			// }
			// }
			// }
			// if (showPreviewPage) {
			// createAndAddPreviewPage();
			// }

			DesignerSourceMouseTrackAdapter adapter = new DesignerSourceMouseTrackAdapter(_textEditor, getEditDomain());
			_textEditor.getTextViewer().getTextWidget().addMouseListener(adapter);
			_textEditor.getTextViewer().getTextWidget().addMouseMoveListener(adapter);
		} catch (PartInitException exception) {
			// $NON-NLS-1$ = "An error has occurred when initializing the input
			// for the the editor's source page."
			if (_log != null) {
				// throw new SourceEditingRuntimeException(
				// "An error has occurred when initializing the input for the
				// the editor's source page.");
			}
		}
		createPage2();
		createPage3();
		// TODO: add a catch block here for any exception the design
		// page throws and convert it into a more informative message.

		setActivePage(1);// 默认展示‘设计’

		editortext = getEditorText();
	}

	/**
	 * Method createTextEditor.
	 * 
	 * @return StructuredTextEditor
	 */
	protected DesignerStructuredTextEditorJSP createTextEditor() {
		return new DesignerStructuredTextEditorJSP() {
			@Override
			protected void performRevert() {
				if (firePersistenceEvent(PersistenceEventType.BEFORE_REVERT)) {
					super.performRevert();
					firePersistenceEvent(PersistenceEventType.REVERTED);
				}
			}
		};
	}

	private void disconnectDesignPage() {
		if (_designViewer != null) {
			_designViewer.setModel(null);
			_designViewer.dispose();
		}
	}

	public void dispose() {
		// System.out.println("dispose of HTML Editor");
		deletePreviewFiles();

		disconnectSashPage();
		disconnectDesignPage();

		IWorkbenchWindow window = getSite().getWorkbenchWindow();
		window.getPartService().removePartListener(_partListener);
		window.getShell().removeShellListener(_partListener);
		getSite().getPage().removePartListener(_partListener);

		if (_textEditor != null) {
			_textEditor.removePropertyListener(this);
			_textEditor.setEditorPart(null);
			_textEditor.dispose();
		}

		// moved to last when added window ... seems like
		// we'd be in danger of losing some data, like site,
		// or something.
		_sashEditorPart = null;
		_tabbedPropSheet = null;
		_partListener = null;
		_editDomain = null;
		_designViewer = null;
		_browser = null;
		_previewComposite = null;
		_paletteViewerPage = null;
		_log = null;
		_selChangedListener = null;
		_textEditor = null;

		if (_model != null) {
			_model.releaseFromEdit();
			_model = null;
		}

		if (persistenceListeners != null) {
			persistenceListeners.clear();
			persistenceListeners = null;
		}

		jseditor.dispose();
		csseditor.dispose();

		jseditor = null;
		csseditor = null;

		super.dispose();

	}

	public void doSave(IProgressMonitor monitor) {
		File jsfile = new File(getJSpathName());
		if (jseditor.isDirty()) {
			try {
				String jseditorText = jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
				if (!jsfile.exists()) {
					jsfile.createNewFile();
					IFile JSIFile = StudioPlugin.getWorkspace().getRoot().getFileForLocation(new Path(getJSpathName()));
					IStructuredModel mode = _textEditor.getStructuredModel();
					if (mode instanceof DOMStyleModelImpl) {
						DOMStyleModelImpl dom = (DOMStyleModelImpl)mode;
						NodeList nodeli = dom.getDocument().getElementsByTagName("head");
						if(nodeli.getLength()>0){
							ElementLinkHref.addScript(nodeli.item(0), JSIFile.getName());
						}
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
					IStructuredModel mode = _textEditor.getStructuredModel();
					if (mode instanceof DOMStyleModelImpl) {
						DOMStyleModelImpl dom = (DOMStyleModelImpl)mode;
						NodeList nodeli = dom.getDocument().getElementsByTagName("head");
						if(nodeli.getLength()>0){
							ElementLinkHref.addLink(nodeli.item(0), CSSIFile.getName());
						}
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
		// momo
		if (firePersistenceEvent(PersistenceEventType.BEFORE_SAVE)) {
			_textEditor.doSave(monitor);
			firePersistenceEvent(PersistenceEventType.SAVED);
		} else {
			monitor.setCanceled(true);
		}
	}

	/*
	 * (non-Javadoc) Saves the contents of this editor to another object. <p>
	 * Subclasses must override this method to implement the open-save-close
	 * lifecycle for an editor. For greater details, see <code> IEditorPart
	 * </code></p>
	 * 
	 * @see IEditorPart
	 */
	public void doSaveAs() {
		if (firePersistenceEvent(PersistenceEventType.BEFORE_SAVE_AS)) {
			_textEditor.doSaveAs();
			firePersistenceEvent(PersistenceEventType.SAVED_AS);
		}
	}

	private void editorInputIsAcceptable(IEditorInput input) throws PartInitException {
		if (input instanceof IFileEditorInput) {
			// verify that it can be opened
			CoreException[] coreExceptionArray = new CoreException[1];
			if (fileDoesNotExist((IFileEditorInput) input, coreExceptionArray)) {
				// todo use message formatter for {0}
				Throwable coreException = coreExceptionArray[0];

				// C.B: this is a strange piece of logic. It was referenceing
				// the internal sub-class of CoreException, ResourceException.
				// need to review fileDoesNotExist.
				if (coreException instanceof CoreException) {
					// I'm assuming this is always 'does not exist'
					// we'll refresh local go mimic behavior of default
					// editor, where the
					// troublesome file is refreshed (and will cause it to
					// 'disappear' from Navigator.
					try {
						((IFileEditorInput) input).getFile().refreshLocal(IResource.DEPTH_ZERO,
								new NullProgressMonitor());
					} catch (CoreException ce) {
						if (_log != null) {
							_log.error("Error.HTMLPageEditor.0", ce); //$NON-NLS-1$
						}
					}
					throw new PartInitException("Resource " + input.getName() //$NON-NLS-1$
							+ " does not exist."); //$NON-NLS-1$
				}
				throw new PartInitException("Editor could not be open on " //$NON-NLS-1$
						+ input.getName());
			}
		} else if (input instanceof IStorageEditorInput) {
			InputStream contents = null;
			try {
				contents = ((IStorageEditorInput) input).getStorage().getContents();
				if (contents == null) {
					throw new PartInitException("Editor could not be open on " //$NON-NLS-1$
							+ input.getName());
				}
			} catch (CoreException noStorageExc) {
				// Error in geting storage contents
				_log.error("Error.HTMLPageEditor.1", noStorageExc); //$NON-NLS-1$
			} finally {
				ResourceUtils.ensureClosed(contents);
			}
		}
	}

	/**
	 * Initializes the editor part with a site and input.
	 * <p>
	 * Subclasses of <code> EditorPart </code> must implement this method.
	 * Within the implementation subclasses should verify that the input type is
	 * acceptable and then save the site and input. Here is sample code:
	 * </p>
	 * 
	 * <pre>
	 * if (!(input instanceof IFileEditorInput))
	 * 	throw new PartInitException("Invalid Input: Must be IFileEditorInput");
	 * setSite(site);
	 * setInput(editorInput);
	 * </pre>
	 * 
	 * @param input
	 * @param coreException
	 * @return true if the input doesn't exist
	 */
	protected boolean fileDoesNotExist(IFileEditorInput input, Throwable[] coreException) {
		boolean result = false;
		InputStream inStream = null;
		if ((!(input.exists())) || (!(input.getFile().exists()))) {
			result = true;
		} else {
			try {
				inStream = input.getFile().getContents(true);
			} catch (CoreException e) {
				// very likely to be file not found
				result = true;
				coreException[0] = e;
				// The core has exception
				_log.error("Error.HTMLPageEditor.3", e); //$NON-NLS-1$
			} finally {
				ResourceUtils.ensureClosed(inStream);
			}
		}
		return result;
	}

	public Object getAdapter(Class key) {
		if ((key.equals(IContentOutlinePage.class))) {
			fiend();
			return mutiPageContentOutlinePage;
		}
		Object result = null;
		if (key == IDesignViewer.class) {
			result = _designViewer;
		} else if (key == PalettePage.class) {
			return getPaletteViewerPage();
		} else if (key == IPropertySheetPage.class) {
			// XXX: we can delegate this to the fTextEditor, but that use some
			// more
			// complicate mechanism, and don't work with page designer well, so
			// do it simple now, fix later.
			// return _textEditor.getAdapter(key);
			return getPropertySheetPage();
		} else if (key == IContentOutlinePage.class) {
			if (_textEditor != null) {
				result = _textEditor.getAdapter(key);
			}
		} else if (key == IPageVariablesProvider.class) {
			Object obj = ((IDOMModel) getModel()).getDocument().getAdapterFor(IDocumentPageVariableAdapter.class);
			if (obj instanceof IPageVariablesProvider) {
				return obj;
			}
			return null;
		} else {
			// DMW: I'm bullet-proofing this because
			// its been reported (on 4.03 version) a null pointer sometimes
			// happens here on startup, when an editor has been left
			// open when workbench shutdown.
			if (_textEditor != null) {
				result = _textEditor.getAdapter(key);
			}
		}
		return result;
	}

	/**
	 * IExtendedSimpleEditor method
	 * 
	 * @return IDocument
	 */
	public IDocument getDocument() {
		if (getTextEditor() == null) {
			return null;
		}

		Object apapter = _textEditor.getAdapter(ISourceEditingTextTools.class);
		if (apapter != null) {
			return ((ISourceEditingTextTools) apapter).getDocument();
		}

		return null;
	}

	/**
	 * IExtendedMarkupEditor method
	 * 
	 * @return the dom document
	 */
	public Document getDOMDocument() {
		if (getTextEditor() == null) {
			return null;
		}

		Object adapter = _textEditor.getAdapter(ISourceEditingTextTools.class);
		if (adapter instanceof IDOMSourceEditingTextTools) {
			return ((IDOMSourceEditingTextTools) adapter).getDOMDocument();
		}
		return null;
	}

	/**
	 * IExtendedSimpleEditor method
	 * 
	 * @return the editor part
	 */
	public IEditorPart getEditorPart() {
		return this;
	}

	/**
	 * Caller MUST NOT release this model, it will be released in
	 * {@link #dispose()}.
	 * 
	 * @return the structured model
	 */
	public IStructuredModel getModel() {
		if (_model == null) {
			if (_textEditor != null) {
				IDocumentProvider documentProvider = _textEditor.getDocumentProvider();
				if (documentProvider != null) {
					IDocument document = documentProvider.getDocument(_textEditor.getEditorInput());
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

	/**
	 * @return the SSE editor delegate
	 */
	public StructuredTextEditor getTextEditor() {
		return _textEditor;
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchPart.
	 */
	public String getTitle() {
		String title = null;
		if (getTextEditor() == null) {
			if (getEditorInput() != null) {
				title = getEditorInput().getName();
			}
		} else {
			title = getTextEditor().getTitle();
		}
		if (title == null) {
			title = getPartName();
		}
		return title;
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		editorInputIsAcceptable(input);
		try {
			// super.init(site, input);
			// setSite(site);
			setInput(input);
			if (_partListener == null) {
				_partListener = new PartActivationHandler(this) {
					public void handleActivation() {
						safelySanityCheckState();
					}
				};
			}
			// we want to listen for our own activation
			IWorkbenchWindow window = getSite().getWorkbenchWindow();
			window.getPartService().addPartListener(_partListener);
			window.getShell().addShellListener(_partListener);

			// TODO: is this the right place to do this?
			// enable our editor context
			IContextService contextService = (IContextService) getSite().getService(IContextService.class);
			contextService.activateContext("org.eclipse.jst.pagedesigner.editorContext"); //$NON-NLS-1$

		} catch (Exception e) {
			// Error in editor initialization
			_log.error("Error.HTMLPageEditor.5", e); //$NON-NLS-1$
		}
		setPartName(input.getName());
	}

	/*
	 * (non-Javadoc) Returns whether the "save as" operation is supported by
	 * this editor. <p> Subclasses must override this method to implement the
	 * open-save-close lifecycle for an editor. For greater details, see <code>
	 * IEditorPart </code></p>
	 * 
	 * @see IEditorPart
	 */
	public boolean isSaveAsAllowed() {
		return _textEditor != null && _textEditor.isSaveAsAllowed();
	}

	public void setOffset(int offset) {
		setActivePage(0);
		_textEditor.selectAndReveal(offset, 0);
	}

	/*
	 * (non-Javadoc) Returns whether the contents of this editor should be saved
	 * when the editor is closed. <p> This method returns <code> true </code> if
	 * and only if the editor is dirty ( <code> isDirty </code> ). </p>
	 */
	public boolean isSaveOnCloseNeeded() {
		// overriding super class since it does a lowly isDirty!
		// if (_textEditor != null) {
		// return _textEditor.isSaveOnCloseNeeded();
		// }
		// if (jseditor != null) {
		// return jseditor.isSaveOnCloseNeeded();
		// }
		// if (csseditor != null) {
		// return csseditor.isSaveOnCloseNeeded();
		// }
		return isDirty();
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = _textEditor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(WebPageEditor1.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
		}
	}

	/**
	 * Posts the update code "behind" the running operation.
	 */
	private void postOnDisplayQue(Runnable runnable) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			Display display = windows[0].getShell().getDisplay();
			display.asyncExec(runnable);
		} else {
			runnable.run();
		}
	}

	/**
	 * Indicates that a property has changed.
	 * 
	 * @param source
	 *            the object whose property has changed
	 * @param propId
	 *            the id of the property which has changed; property ids are
	 *            generally defined as constants on the source class
	 */
	public void propertyChanged(Object source, int propId) {
		switch (propId) {
		// had to implement input changed "listener" so that
		// strucutedText could tell it containing editor that
		// the input has change, when a 'resource moved' event is
		// found.
		case IEditorPart.PROP_INPUT: {
			if (source == _textEditor) {
				if (_textEditor.getEditorInput() != getEditorInput()) {
					// Bug 392859 - [Regression] Incorrect WPE model returned
					// from HTMLPageEditor after page name change.
					// release the old model
					if (_model != null) {
						_model.releaseFromEdit();
						_model = null;
					}
					setInput(_textEditor.getEditorInput());
					// title should always change when input changes.
					// create runnable for following post call
					Runnable runnable = new Runnable() {
						public void run() {
							_firePropertyChange(IWorkbenchPart.PROP_TITLE);
						}
					};
					// Update is just to post things on the display queue
					// (thread). We have to do this to get the dirty
					// property to get updated after other things on the
					// queue are executed.
					postOnDisplayQue(runnable);
				}
			}
			break;
		}
		case IWorkbenchPart.PROP_TITLE: {
			// // update the input if the title is changed. why? It seems input
			// change event will be fired at last.
			// if (source == _textEditor)
			// {
			// if (_textEditor.getEditorInput() != getEditorInput())
			// {
			// setInput(_textEditor.getEditorInput());
			// }
			// }
			// break;
		}
		default: {
			// propagate changes. Is this needed? Answer: Yes.
			// PROP_PART_NAME, PROP_DIRTY etc.
			if (source == _textEditor) {
				firePropertyChange(propId);
			}
			break;
		}
		}

	}

	private void safelySanityCheckState() {
		// If we're called before editor is created, simply ignore since we
		// delegate this function to our embedded TextEditor
		if (getTextEditor() == null) {
			return;
		}

		getTextEditor().safelySanityCheckState(getEditorInput());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		// If driven from the Source page, it's "model" may not be up to date
		// with the input just yet. We'll rely on later notification from the
		// TextViewer to set us straight
		super.setInput(input);
		if (_designViewer != null) {
			_designViewer.setModel(getModel());
		}
		setPartName(input.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		try {
			return _textEditor.isDirty() || jseditor.isDirty() || csseditor.isDirty();
		} catch (Exception e) {
		}
		return false;
	}

	private IPropertySheetPage getPropertySheetPage() {
		if (_tabbedPropSheet == null || _tabbedPropSheet.getControl() == null
				|| _tabbedPropSheet.getControl().isDisposed()) {
			IPropertySheetPageFactory factory = getPageFactory();
			if (factory != null) {
				final IFile file = ((IFileEditorInput) getEditorInput()).getFile();
				_tabbedPropSheet = factory.createPage(file);
			} else {
				_tabbedPropSheet = new WPETabbedPropertySheetPage(this, this);
			}
		}
		return _tabbedPropSheet;
	}

	private IPropertySheetPageFactory getPageFactory() {
		// List<IElementEditFactory> result = new
		// ArrayList<IElementEditFactory>();
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(PDPlugin.getPluginId(),
				IJMTConstants.EXTENSION_POINT_PAGEDESIGNER);
		IExtension[] extensions = extensionPoint.getExtensions();

		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			IConfigurationElement[] elementEditElement = ext.getConfigurationElements();

			for (int j = 0; j < elementEditElement.length; j++) {
				final IConfigurationElement element = elementEditElement[j];
				if (element.getName().equals(IJMTConstants.PROPERTY_PAGE_FACTORY)) {
					elementEditElement[j].getAttribute("class"); //$NON-NLS-1$
					Object obj;
					try {
						obj = elementEditElement[j].createExecutableExtension("class"); //$NON-NLS-1$

						// TODO: we need a policy based solution here,
						// but this will do for now
						if (obj instanceof IPropertySheetPageFactory) {
							return (IPropertySheetPageFactory) obj;
						}
					} catch (CoreException e) {
						PDPlugin.log("Problem loading element edit extension for " + element.toString(), e); //$NON-NLS-1$
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return PaletteViewerPage
	 */
	private PaletteViewerPage getPaletteViewerPage() {
		if (_paletteViewerPage == null) {
			_paletteViewerPage = _designViewer.createPaletteViewerPage();
		}
		return _paletteViewerPage;
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
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#pageChange(int)
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);

		deletePreviewFiles();

		if (newPageIndex == _previewPageIndex) {
			// preview page activate, need to regenerate the preview text and
			// display it.
			StringBuffer result = new StringBuffer();
			try {
				// Bug 350990 - Web Page Editor intolerably slow
				if (_mode == MODE_SOURCE) {
					_designViewer.setModel(getModel());
				}
				DocumentEditPart part = (DocumentEditPart) this._designViewer.getGraphicViewer().getContents();
				PreviewHandlerNew.generatePreview(part, result);
				// Bug 350990 - Web Page Editor intolerably slow
				if (_mode == MODE_SOURCE) {
					_designViewer.setModel(null);
				}
			} catch (Exception ex) {
				result = new StringBuffer();
				result.append(getModel().getStructuredDocument().getText());
				// Error in page changing
				_log.info("Error.HTMLPageEditor.6", ex); //$NON-NLS-1$
			}
			File file = PreviewUtil.toFile(result, getEditorInput());
			if (file != null) {
				PREVIEW_FILES_LIST.add(file);
				getPreviewBrowser().loadFile(file);
			} else {
				getPreviewBrowser().getBrowser().setUrl("about:blank"); //$NON-NLS-1$
			}
		}
		if (newPageIndex == 0) {
			act(this._textEditor);
			editortext = getEditorText();
		} else if (newPageIndex == 1) {
			act(this._textEditor);
			if (!getEditorText().equals(editortext)) {
				setModel();
			}
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

	/**
	 * @return Returns the _designViewer.
	 */
	public IDesignViewer getDesignViewer() {
		return _designViewer;
	}

	/**
	 * @param mode
	 */
	public void setDesignerMode(int mode) {
		boolean modeWasSourceOnly = (_mode == MODE_SOURCE && _mode != mode);
		if (_sashEditorPart != null && _mode != mode) {
			switch (mode) {
			case MODE_SASH_HORIZONTAL:
				_sashEditorPart.setOrientation(SWT.HORIZONTAL);
				break;
			case MODE_DESIGNER:
				_sashEditorPart.setMaximizedEditor(this._designViewer);
				break;
			case MODE_SOURCE:
				_sashEditorPart.setMaximizedEditor(this._textEditor);
				// Bug 350990 - Web Page Editor intolerably slow
				_designViewer.setModel(null);
				if (_designPageActionContributor != null) {
					_designPageActionContributor.disableRangeModeActions();
				}
				break;
			case MODE_SASH_VERTICAL:
			default:
				_sashEditorPart.setOrientation(SWT.VERTICAL);
			}
			if (getEditorInput() != null) {
				EditorUtil.setEditorInputDesignModeProperty(getEditorInput(), String.valueOf(mode));
			}
		}
		this._mode = mode;
		if (modeWasSourceOnly) {
			// Bug 350990 - Web Page Editor intolerably slow
			_designViewer.setModel(getModel());
			resynch();
		}
	}

	/**
	 * Sets the current DesignPageActionContributor instance.
	 * 
	 * @param designPageActionContributor
	 *            Current DesignPageActionContributor instance.
	 */
	public void setDesignPageActionContributor(final DesignPageActionContributor designPageActionContributor) {
		_designPageActionContributor = designPageActionContributor;
	}

	/*
	 * Set the sash editor mode from the stored file property or the default
	 * preference.
	 */
	private void initDesignerMode() {
		int preferredMode = MODE_SASH_VERTICAL;

		// If the user has already selected a mode for the file, use it.
		String prop = null;
		if (getEditorInput() != null) {
			prop = EditorUtil.getEditorInputDesignModeProperty(getEditorInput());
		}
		if (prop != null) {
			try {
				preferredMode = Integer.parseInt(prop);
			} catch (NumberFormatException e) {
				// do nothing;
			}
		} else {
			// Otherwise, get the default mode from preferences.
			IPreferenceStore pStore = PDPlugin.getDefault().getPreferenceStore();
			preferredMode = pStore.getInt(PDPreferences.SASH_EDITOR_MODE_PREF);
		}

		setDesignerMode(preferredMode);
	}

	/**
	 * @return the current design mode
	 */
	public int getDesignerMode() {
		return this._mode;
	}

	private void resynch() {
		if (_textEditor != null && _designViewer != null) {
			ISelectionProvider provider = _textEditor.getSelectionProvider();
			if (provider != null) {
				ISelection selection = provider.getSelection();
				if (selection instanceof TextSelection) {
					TextSelection textSelection = (TextSelection) selection;
					SelectionSynchronizer synchronizer = _designViewer.getSynchronizer();
					if (synchronizer != null) {
						synchronizer.textSelectionChanged(textSelection.getOffset(),
								textSelection.getOffset() + textSelection.getLength());
					}
				}
			}
		}
	}

	public IEditorPart getActiveEditor() {
		IEditorPart result = null;
		try {
			if (_sash) {
				result = _sashEditorPart.getActiveEditor();
			} else {
				if (_designViewer.getGraphicViewer().getControl().isFocusControl()) {
					result = _designViewer;
				} else if (_textEditor.getTextViewer().getControl().isFocusControl()) {
					result = _textEditor;
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	public String getPartName() {
		if (_textEditor != null) {
			return _textEditor.getPartName();
		}
		return super.getPartName();
	}

	/**
	 * 清除预览文件
	 */
	private void deletePreviewFiles() {
		Iterator itPreviewFiles = PREVIEW_FILES_LIST.iterator();
		while (itPreviewFiles.hasNext()) {
			File file = (File) itPreviewFiles.next();
			if (file != null && file.exists()) {
				file.delete();
			}
		}
		PREVIEW_FILES_LIST.clear();
	}

	/**
	 * Refreshes the design page. Allows an external action to force a refresh
	 * after an external change, such as a DT skin change.
	 */
	public void refreshDesignViewer() {
		EditPart contentEditPart = _designViewer.getGraphicViewer().getRootEditPart().getContents();
		if (contentEditPart instanceof DocumentEditPart) {
			((DocumentEditPart) contentEditPart).styleChanged();
		}
	}

	/**
	 * Adds a {@link IWPEPersistenceListener persistence listener} to this
	 * editor.
	 * 
	 * <p>
	 * This type of listener is cleaned when the editor is disposed.
	 * </p>
	 * 
	 * @param listener
	 */
	public void addPersistenceListener(IWPEPersistenceListener listener) {
		if (persistenceListeners == null) {
			persistenceListeners = new ArrayList<IWPEPersistenceListener>(5);
		}
		persistenceListeners.add(listener);
	}

	/**
	 * Removes a {@link IWPEPersistenceListener persistence listener} added to
	 * this editor.
	 * 
	 * <p>
	 * This type of listener is cleaned when the editor is disposed.
	 * </p>
	 * 
	 * @param listener
	 */
	public void removePersistenceListener(IWPEPersistenceListener listener) {
		if (persistenceListeners != null) {
			if (persistenceListeners.remove(listener) && persistenceListeners.isEmpty()) {
				persistenceListeners = null;
			}
		}
	}

	/**
	 * @param type
	 * @return <code>true</code> if operation is to continue, otherwise
	 *         <code>false</code>
	 */
	private boolean firePersistenceEvent(final PersistenceEventType type) {
		if (persistenceListeners != null) {
			List<IWPEPersistenceListener> listeners = new ArrayList<IWPEPersistenceListener>(persistenceListeners);
			IPersistenceEvent event = new IPersistenceEvent() {
				private boolean cancelled;

				public WebPageEditor1 getWPEInstance() {
					return WebPageEditor1.this;
				}

				public PersistenceEventType getEventType() {
					return type;
				}

				public boolean isOperationCancelled() {
					return cancelled;
				}

				public void cancelOperation() {
					cancelled = true;
				}
			};

			for (IWPEPersistenceListener listener : listeners) {
				try {
					listener.notify(event);
				} catch (Exception e) {
					PDPlugin.log("Exception thrown while notifying a persistence listener", e); //$NON-NLS-1$
				}
			}
			return !event.isOperationCancelled();
		}
		return true;
	}

	/**
	 * Updates the editor's title image
	 * 
	 * @param titleImage
	 */
	public void updateTitleImage(Image titleImage) {
		setTitleImage(titleImage);
	}

	public StructuredTextEditor getSourceEditor() {
		return _textEditor;
	}

	private String getJSpathName() {
		IEditorInput curEditorInput = _textEditor.getEditorInput();
		String editPath = _textEditor.getTitleToolTip();
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
		IEditorInput curEditorInput = _textEditor.getEditorInput();
		String editPath = _textEditor.getTitleToolTip();
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
		if (designViewer != null) {
			designViewer.setModel();
		}
	}

	public WEBDesignEditorInterface getDesignEditor() {
		return designViewer;
	}

	public String getEditorText() {
		return _textEditor.getDocumentProvider().getDocument(_textEditor.getEditorInput()).get();
	}

	public String getJSEditorText() {
		return jseditor.getDocumentProvider().getDocument(jseditor.getEditorInput()).get();
	}

	public void setSourcePageText() {
		// _textEditor.getDocumentProvider().getDocument(_textEditor.getEditorInput()).set(pageDom.html());
	}

	public void setSourcePageText(String text) {
		_textEditor.getDocumentProvider().getDocument(_textEditor.getEditorInput())
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

	@Override
	public Element getPageDom() {
		return null;
	}

	@Override
	public AbstractDecoratedTextEditor getJSEditor() {
		return jseditor;
	}

}