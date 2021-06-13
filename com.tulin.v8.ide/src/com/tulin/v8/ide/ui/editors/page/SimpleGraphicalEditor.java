package com.tulin.v8.ide.ui.editors.page;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.pagedesigner.IJMTConstants;
import org.eclipse.jst.pagedesigner.PDPlugin;
import org.eclipse.jst.pagedesigner.actions.container.ContainerActionGroup;
import org.eclipse.jst.pagedesigner.actions.menuextension.CustomedContextMenuActionGroup;
import org.eclipse.jst.pagedesigner.actions.range.RangeActionGroup;
import org.eclipse.jst.pagedesigner.actions.single.SingleElementActionGroup;
import org.eclipse.jst.pagedesigner.commands.CopyAction;
import org.eclipse.jst.pagedesigner.commands.CutAction;
import org.eclipse.jst.pagedesigner.commands.DeleteAction;
import org.eclipse.jst.pagedesigner.commands.PasteAction;
import org.eclipse.jst.pagedesigner.dnd.internal.LocalSelectionDropTargetListener;
import org.eclipse.jst.pagedesigner.dnd.internal.PDTemplateTransferDropTargetListener;
import org.eclipse.jst.pagedesigner.dnd.internal.ResouceDropTargetListener;
import org.eclipse.jst.pagedesigner.editors.IDesignViewer;
import org.eclipse.jst.pagedesigner.editors.PageDesignerActionConstants;
import org.eclipse.jst.pagedesigner.editors.actions.ActionsMessages;
import org.eclipse.jst.pagedesigner.editors.actions.RelatedViewActionGroup;
import org.eclipse.jst.pagedesigner.editors.actions.SkinsMenuItemBuilder;
import org.eclipse.jst.pagedesigner.editors.palette.DesignerPaletteCustomizer;
import org.eclipse.jst.pagedesigner.editors.palette.DesignerPaletteRootFactory;
import org.eclipse.jst.pagedesigner.editors.palette.DesignerPaletteViewerProvider;
import org.eclipse.jst.pagedesigner.editors.palette.IPaletteFactory;
import org.eclipse.jst.pagedesigner.jsp.core.internal.pagevar.DocumentPageVariableAdapter;
import org.eclipse.jst.pagedesigner.jsp.core.pagevar.adapter.PageVariableAdapterFactory;
import org.eclipse.jst.pagedesigner.parts.CSSStyleAdapterFactory;
import org.eclipse.jst.pagedesigner.parts.DocumentEditPart;
import org.eclipse.jst.pagedesigner.parts.HTMLEditPartsFactory;
import org.eclipse.jst.pagedesigner.parts.RefresherFactory;
import org.eclipse.jst.pagedesigner.utils.SelectionHelper;
import org.eclipse.jst.pagedesigner.viewer.IHTMLGraphicalViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.wst.sse.core.internal.PropagatingAdapter;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.undo.IDocumentSelectionMediator;
import org.eclipse.wst.sse.core.internal.undo.UndoDocumentEvent;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.page.design.action.Messages;

/**
 * @author mengbo
 */
@SuppressWarnings("restriction")
public class SimpleGraphicalEditor extends GraphicalEditorWithFlyoutPalette
		implements IDesignViewer, IDocumentSelectionMediator {

	private PageEditorInterface _delegate;

	private IHTMLGraphicalViewer _viewer;

	private IStructuredModel _model;

	/** Palette component, holding the tools and shapes. */
	private PaletteRoot _palette;

	private PaletteViewerPage _paletteViewerPage;

	private SelectionSynchronizer _synchronizer = new SelectionSynchronizer(this);

	private IModelStateListener _internalModelListener = new IModelStateListener() {
		public void modelAboutToBeChanged(IStructuredModel model) {
			// do nothing
		}

		public void modelChanged(IStructuredModel model) {
			updateActionsWhenModelChange();
		}

		public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
			// do nothing
		}

		public void modelResourceDeleted(IStructuredModel model) {
			// do nothing
		}

		public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
			// do nothing
		}

		public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
			// do nothing
		}

		public void modelReinitialized(IStructuredModel structuredModel) {
			// do nothing
		}
	};

	private PaletteViewerProvider _paletteViewerProvider;

	/**
	 * TODO: why isn't this private?
	 */
	protected IPaletteFactory _paletteViewerPageFactory;

	/**
	 * @param delegate
	 * @param editdomain
	 */
	public SimpleGraphicalEditor(PageEditorInterface delegate, DefaultEditDomain editdomain) {
		_delegate = delegate;
		initPaletteFactory();
		this.setEditDomain(editdomain);
	}

	protected void createGraphicalViewer(Composite parent) {
		_viewer = IHTMLGraphicalViewer.Factory.createGraphicalViewer(this);
		Control control = _viewer.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control,
				PDPlugin.getResourceString("SimpleGraphicalEditor.help.id")); //$NON-NLS-1$
		setGraphicalViewer(_viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
		initializeContextMenu();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#dispose()
	 */
	public void dispose() {
		if (_model != null) {
			_model.getUndoManager().disconnect(this);
		}

		_paletteViewerPage = null;
		_palette = null;
		_model = null;
		_viewer = null;

		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		_viewer.setRootEditPart(rootEditPart);

		_viewer.getViewport().setContentsTracksWidth(true);

		_viewer.setKeyHandler(new GraphicalViewerKeyHandler(_viewer));

		// initialize the viewer with input
		// IStructuredModel sModel =
		// StructuredModelManager.getModelManager().createUnManagedStructuredModelFor(ContentTypeIdForHTML.ContentTypeID_HTML);
		// IDOMDocument designDoc = ((IDOMModel)sModel).getDocument();
		// HTMLEditPartsFactory factory = new HTMLEditPartsFactory(designDoc);
		HTMLEditPartsFactory factory = new HTMLEditPartsFactory(null);

		_viewer.setEditPartFactory(factory);

		// for sync with source view.

		_viewer.addDropTargetListener(new LocalSelectionDropTargetListener(_viewer));
		_viewer.addDropTargetListener(new PDTemplateTransferDropTargetListener(_viewer));
		_viewer.addDropTargetListener(new ResouceDropTargetListener(_viewer));

		// add double click support.
		_viewer.getControl().addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				try {
					getSite().getPage().showView(IPageLayout.ID_PROP_SHEET);
				} catch (PartInitException e1) {
					// ignore
				}
			}
		});

		super.initializeGraphicalViewer();
	}

	/**
	 * 
	 */
	protected void initializeContextMenu() {
		Control gviewer = _viewer.getControl();
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		Menu menu = menuMgr.createContextMenu(gviewer);
		gviewer.setMenu(menu);
		menuMgr.addMenuListener(new ContextMenuListener());
		getSite().registerContextMenu("HTMLVisualEditor.contextMenu", menuMgr, _viewer); //$NON-NLS-1$
	}

	private void updateActionsWhenModelChange() {
		// update undo/redo action
		IAction action = this.getAction(IWorkbenchCommandConstants.EDIT_UNDO);
		((UpdateAction) action).update();

		action = this.getAction(IWorkbenchCommandConstants.EDIT_REDO);
		((UpdateAction) action).update();
	}

	private void setGlobalActionHandler(final IAction action) {
		if (action != null) {
			final String actionId = action.getId();
			if (actionId != null && actionId.length() > 0) {
				final IEditorSite site = getEditorSite();
				if (site != null) {
					final IActionBars bars = site.getActionBars();
					if (bars != null) {
						bars.setGlobalActionHandler(actionId, action);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
	 */
	@SuppressWarnings("unchecked")
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

		IAction action;

		action = new DesignerUndoRedoAction(true, this);
		action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		action.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
		action.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_UNDO);
		action.setId(IWorkbenchCommandConstants.EDIT_UNDO);
		setGlobalActionHandler(action);
		registry.registerAction(action);

		action = new DesignerUndoRedoAction(false, this);
		action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		action.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO_DISABLED));
		action.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_REDO);
		action.setId(IWorkbenchCommandConstants.EDIT_REDO);
		setGlobalActionHandler(action);
		registry.registerAction(action);

		action = new DeleteAction(this);
		action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		action.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		action.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_DELETE);
		action.setId(IWorkbenchCommandConstants.EDIT_DELETE);
		setGlobalActionHandler(action);
		this.getSelectionActions().add(action.getId());
		registry.registerAction(action);

		action = new CopyAction(this);
		action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		action.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		action.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);
		action.setId(IWorkbenchCommandConstants.EDIT_COPY);
		setGlobalActionHandler(action);
		this.getSelectionActions().add(action.getId());
		registry.registerAction(action);

		action = new CutAction(this);
		action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		action.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		action.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_CUT);
		action.setId(IWorkbenchCommandConstants.EDIT_CUT);
		setGlobalActionHandler(action);
		this.getSelectionActions().add(action.getId());
		registry.registerAction(action);

		action = new PasteAction(this);
		action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		action.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		action.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_PASTE);
		action.setId(IWorkbenchCommandConstants.EDIT_PASTE);
		setGlobalActionHandler(action);
		this.getSelectionActions().add(action.getId());
		registry.registerAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		if (_delegate != null) {
			_delegate.doSave(monitor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		if (_delegate != null) {
			_delegate.doSaveAs();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() {
		if (_delegate != null) {
			return _delegate.isDirty();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		if (_delegate != null) {
			return _delegate.isSaveAsAllowed();
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if the current model is valid.
	 * 
	 * @return <code>true</code> if the current model is valid, else
	 *         <code>false</code>.
	 */
	public boolean isModelValid() {
		return _model != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.html.editor.IDesignViewer#setModel(com.ibm.sse.model.
	 * IStructuredModel)
	 */
	public void setModel(IStructuredModel model) {
		if (_model != null) {
			if (_model.getUndoManager() != null)
				_model.getUndoManager().disconnect(this);
			_model.removeModelStateListener(_internalModelListener);
		}

		this._model = model;

		if (_model != null) {
			_model.addModelStateListener(_internalModelListener);
			if (_model.getUndoManager() != null) {
				_model.getUndoManager().connect(this);
				updateActionsWhenModelChange();
			}
		}

		if (model instanceof IDOMModel) {
			IDOMDocument doc = ((IDOMModel) model).getDocument();
			PropagatingAdapter adapter = (PropagatingAdapter) doc.getAdapterFor(PropagatingAdapter.class);
			if (adapter != null) {
				INodeAdapterFactory factory = RefresherFactory.getInstance();
				adapter.addAdaptOnCreateFactory(factory);
				adapter.initializeForFactory(factory, doc);
				// CSSStyleAdapterFactory fac2 =
				// CSSStyleAdapterFactory.getInstance();
				// adapter.addAdaptOnCreateFactory(fac2);
				// adapter.initializeForFactory(fac2, doc);
			}
			((IDOMModel) model).getFactoryRegistry().addFactory(CSSStyleAdapterFactory.getInstance());

			// _viewer.getDestDocumentForDesign().getModel().getFactoryRegistry().addFactory(CSSStyleAdapterFactory.getInstance());
			((IDOMModel) model).getFactoryRegistry().addFactory(new PageVariableAdapterFactory());
			doc.addAdapter(new DocumentPageVariableAdapter(doc));
			_viewer.setContents(((IDOMModel) model).getDocument());
		} else {
			_viewer.setContents((EditPart) null);
		}
	}

	/**
	 * @return the selection synchronizer
	 */
	protected SelectionSynchronizer getSynchronizer() {
		return _synchronizer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditor#updateActions(java.util.List)
	 */
	@SuppressWarnings("rawtypes")
	protected void updateActions(List actionIds) {
		super.updateActions(actionIds);
	}

	/**
	 * @param id
	 * @return the action for the id
	 */
	public IAction getAction(Object id) {
		// lium: following lines commented out, see comments in
		// DesignerUndoRedoAction
		// if (ITextEditorActionConstants.UNDO.equals(id) ||
		// ITextEditorActionConstants.REDO.equals(id))
		// {
		// return _delegate.getTextEditor().getAction((String) id);
		// }
		return getActionRegistry().getAction(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#
	 * getPalettePreferences()
	 */
	protected FlyoutPreferences getPalettePreferences() {
		return DesignerPaletteRootFactory.createPalettePreferences();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot(
	 * )
	 */
	protected PaletteRoot getPaletteRoot() {
		if (_palette == null) {
			if (_paletteViewerPageFactory != null) {
				_palette = _paletteViewerPageFactory.createPaletteRoot(_delegate.getEditorInput());
			}
			if (_palette == null) {
				_palette = DesignerPaletteRootFactory.createPaletteRoot(getCurrentFile(_delegate.getEditorInput()));
			}
		}
		return _palette;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#
	 * createPaletteViewerProvider()
	 */
	protected PaletteViewerProvider createPaletteViewerProvider() {
		if (_paletteViewerProvider == null) {
			if (_paletteViewerPageFactory != null) {
				_paletteViewerProvider = _paletteViewerPageFactory.createPaletteViewerProvider(getEditDomain());
			}
			if (_paletteViewerProvider == null) {// if still null
				return new DesignerPaletteViewerProvider(getEditDomain()) {
					protected void configurePaletteViewer(PaletteViewer viewer) {
						super.configurePaletteViewer(viewer);
						viewer.setCustomizer(new DesignerPaletteCustomizer());

						// create a drag source listener for this palette viewer
						// together with an appropriate transfer drop target
						// listener,
						// this will enable
						// model element creation by dragging a
						// CombinatedTemplateCreationEntries
						// from the palette into the editor
						// @see ShapesEditor#createTransferDropTargetListener()
						viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
					}
				};
			}
		}
		return _paletteViewerProvider;
	}

	/**
	 * @return the palette viewer page
	 */
	protected PaletteViewerPage createPaletteViewerPage() {
		if (_paletteViewerPageFactory != null) {
			_paletteViewerPage = _paletteViewerPageFactory.createPaletteViewerPage(createPaletteViewerProvider());
		}
		if (_paletteViewerPage == null) {
			DefaultEditDomain editDomain = getEditDomain();
			// PaletteItemManager manager = PaletteItemManager
			// .getInstance(getCurrentFile(getEditorInput()));
			// manager.reset();
			PaletteRoot paletteRoot = getPaletteRoot();
			editDomain.setPaletteRoot(paletteRoot);

			// _paletteViewerPage = (PaletteViewerPage)
			// super.getAdapter(PalettePage.class);
			// if possible, try to use the
			if (_paletteViewerPage == null) {
				PaletteViewerProvider provider = getPaletteViewerProvider2();
				_paletteViewerPage = new PaletteViewerPage(provider);
			}
		}
		return _paletteViewerPage;
	}

	PaletteViewerProvider getPaletteViewerProvider2() {
		return getPaletteViewerProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jst.pagedesigner.editors.IDesignViewer#getGraphicViewer()
	 */
	public IHTMLGraphicalViewer getGraphicViewer() {
		return _viewer;
	}

	/**
	 * @return the html editor delegate
	 */
	public PageEditorInterface getHTMLEditor() {
		return _delegate;
	}

	// private IProject getCurrentProject(IEditorInput input) {
	// IProject curProject = null;
	// IFile inputFile = null;
	// if (input instanceof IFileEditorInput) {
	// inputFile = ((IFileEditorInput) input).getFile();
	// curProject = inputFile.getProject();
	// }
	// return curProject;
	// }

	private IFile getCurrentFile(IEditorInput input) {
		IFile inputFile = null;
		if (input instanceof IFileEditorInput) {
			inputFile = ((IFileEditorInput) input).getFile();
		}
		return inputFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (_viewer != null) {
			GraphicalViewer viewerViewer = getGraphicalViewer();
			if (viewerViewer != null && viewerViewer.getControl() != null
					&& viewerViewer.getControl().isFocusControl()) {
				updateActions(getSelectionActions());
				if (selection instanceof IStructuredSelection && //
						!(((IStructuredSelection) selection).getFirstElement() instanceof DocumentEditPart)) {
					((IHTMLGraphicalViewer) viewerViewer).updateRangeSelection(selection);
				}
			}
		} else {
			super.selectionChanged(part, selection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.core.internal.undo.IDocumentSelectionMediator#
	 * getDocument()
	 */
	public IDocument getDocument() {
		if (_model != null) {
			return _model.getStructuredDocument();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.core.internal.undo.IDocumentSelectionMediator#
	 * undoOperationSelectionChanged(org.eclipse.wst.sse.core.internal.undo.
	 * UndoDocumentEvent)
	 */
	public void undoOperationSelectionChanged(UndoDocumentEvent event) {
		IDocumentSelectionMediator requester = event.getRequester();
		if (this == requester) {
			// ok, the undo/redo operation is initialized by designer page.
			// we should set selection in designer.
			// However, when this method is called, the modelChanged event is
			// not fired yet, so the
			// editpart hasn't refreshed yet. So we register a
			// modelStateListener, and do the selection
			// in modelChangedEvent. (lium)
			final int offset = event.getOffset();
			final int length = event.getLength();

			_model.addModelStateListener(new IModelStateListener() {
				public void modelAboutToBeChanged(IStructuredModel model) {
					// nothing to do
				}

				public void modelChanged(IStructuredModel model) {
					_model.removeModelStateListener(this);
					ISelection sel = SelectionHelper.convertToDesignerSelection(getGraphicViewer(), offset, length);
					if (sel != null) {
						getGraphicViewer().setSelection(sel);
					}
				}

				public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
					// do nothing
				}

				public void modelResourceDeleted(IStructuredModel model) {
					// do nothign
				}

				public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
					// do nothing
				}

				public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
					// do nothing
				}

				public void modelReinitialized(IStructuredModel structuredModel) {
					// do nothing
				}
			});
		}
	}

	private final class ContextMenuListener implements IMenuListener {
		public void menuAboutToShow(IMenuManager menuMgr1) {
			// add standarized sub-menus
			addEditSubMenu(menuMgr1);
			addSelectSubMenu(menuMgr1);
			addInsertSubMenu(menuMgr1);
			/*
			 * Bug 172959 - [WPE] WPE context menu Navigate->Java->Value doesn't
			 * work..
			 * 
			 * addNavigateSubMenu(menuMgr1);
			 */
			addStyleSubMenu(menuMgr1);

			// add separators that mark standard append locations in the main
			// context menu
			PageDesignerActionConstants.addStandardActionGroups(menuMgr1);

			// TODO: Run/Debug?

			// insert ElementEdit contributed menu items
			final ContainerActionGroup containerActionGroup = new ContainerActionGroup();
			ActionContext context = new ActionContext(_viewer.getSelection());
			context.setInput(_viewer);
			containerActionGroup.setContext(context);
			containerActionGroup.fillContextMenu(menuMgr1);
			containerActionGroup.setContext(null);

			// TODO: TableActionGroup

			// if on a text context, (instead of a ElementEditPart),
			// add text styling actions
			final RangeActionGroup rangeActionGroup = new RangeActionGroup();
			context = new ActionContext(_viewer.getSelection());
			context.setInput(_viewer);
			rangeActionGroup.setContext(context);
			rangeActionGroup.fillContextMenu(menuMgr1);
			rangeActionGroup.setContext(null);

			// Add actions for single ElementEditPart's that are common
			// to all
			final SingleElementActionGroup singleActionGroup = new SingleElementActionGroup();
			singleActionGroup.setContext(new ActionContext(_viewer.getSelection()));
			singleActionGroup.fillContextMenu(menuMgr1);
			singleActionGroup.setContext(null);

			// add "Show In" actions...
			final RelatedViewActionGroup viewMenu = new RelatedViewActionGroup(getEditDomain());
			context = new ActionContext(_viewer.getSelection());
			viewMenu.setContext(context);
			viewMenu.fillContextMenu(menuMgr1);

			// add extension point contributed menu actions
			CustomedContextMenuActionGroup customedMenu = new CustomedContextMenuActionGroup();
			customedMenu.setContext(new ActionContext(_viewer.getSelection()));
			customedMenu.setModel(_model);
			customedMenu.setParentControl(_viewer.getControl());
			customedMenu.fillContextMenu(menuMgr1);
			customedMenu.setContext(null);
			customedMenu.setParentControl(null);
			customedMenu.setModel(null);

			// add skins menu
			IEditorInput editorInput = _delegate.getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) editorInput).getFile();
				IProject project = file.getProject();
				if (project != null) {
					SkinsMenuItemBuilder builder = new SkinsMenuItemBuilder(project);
					builder.buildMenuManagers(menuMgr1);
				}
			}
			
			menuMgr1.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			Action viewSourseAction = new Action(){
				public void run() {
					_delegate.activhtmlEditor();
				}
			};
			viewSourseAction.setText(Messages.getString("design.action.viewsource"));
			viewSourseAction.setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("tag.gif")));
			menuMgr1.add(viewSourseAction);
		}

		/*
		 * Bug 172959 - [WPE] WPE context menu Navigate->Java->Value doesn't
		 * work..
		 * 
		 * private void addNavigateSubMenu(IMenuManager menu) { final
		 * IMenuManager navigateSubmenu = new
		 * MenuManager(ActionsMessages.getString("Navigate.Menu") //$NON-NLS-1$
		 * , PageDesignerActionConstants.NAVIGATE_SUBMENU_ID);
		 * menu.add(navigateSubmenu);
		 * PageDesignerActionConstants.addStandardNavigateActionGroups(
		 * navigateSubmenu); }
		 */

		private void addEditSubMenu(IMenuManager menu) {
			final IMenuManager editSubmenu = new MenuManager(ActionsMessages.getString("Edit.Menu") //$NON-NLS-1$
					, PageDesignerActionConstants.EDIT_SUBMENU_ID);
			menu.add(editSubmenu);
			PageDesignerActionConstants.addStandardEditActionGroups(editSubmenu);

			// FIXME: for UNDO/REDO, maybe need also wrap them in
			// DesignerCommand.
			// otherwise don't have validate() called after the source
			// change.
			editSubmenu.appendToGroup(PageDesignerActionConstants.GROUP_UNDO,
					getAction(IWorkbenchCommandConstants.EDIT_UNDO));
			editSubmenu.appendToGroup(PageDesignerActionConstants.GROUP_UNDO,
					getAction(IWorkbenchCommandConstants.EDIT_REDO));

			editSubmenu.appendToGroup(PageDesignerActionConstants.GROUP_EDIT,
					getAction(IWorkbenchCommandConstants.EDIT_CUT));
			editSubmenu.appendToGroup(PageDesignerActionConstants.GROUP_EDIT,
					getAction(IWorkbenchCommandConstants.EDIT_COPY));
			editSubmenu.appendToGroup(PageDesignerActionConstants.GROUP_EDIT,
					getAction(IWorkbenchCommandConstants.EDIT_PASTE));
			editSubmenu.appendToGroup(PageDesignerActionConstants.GROUP_EDIT,
					getAction(IWorkbenchCommandConstants.EDIT_DELETE));

		}

		private void addStyleSubMenu(IMenuManager menu) {
			final IMenuManager styleSubmenu = new MenuManager(ActionsMessages.getString("Style.Menu") //$NON-NLS-1$
					, PageDesignerActionConstants.STYLE_SUBMENU_ID);
			menu.add(styleSubmenu);
			PageDesignerActionConstants.addStandardStyleActionGroups(styleSubmenu);
		}

		private void addInsertSubMenu(IMenuManager menu) {
			final IMenuManager insertSubmenu = new MenuManager(ActionsMessages.getString("Insert.Menu") //$NON-NLS-1$
					, PageDesignerActionConstants.INSERT_SUBMENU_ID);
			menu.add(insertSubmenu);
			PageDesignerActionConstants.addStandardInsertActionGroups(insertSubmenu);
		}

		private void addSelectSubMenu(IMenuManager menu) {
			final IMenuManager selectSubMenu = new MenuManager(ActionsMessages.getString("Select.Menu") //$NON-NLS-1$
					, PageDesignerActionConstants.SELECT_SUBMENU_ID);
			menu.add(selectSubMenu);
			PageDesignerActionConstants.addStandardSelectActionGroups(selectSubMenu);
		}
	}

	/**
	 * @return the palette viewer page
	 */
	public PaletteViewerPage getPaletteViewerPage() {
		if (_paletteViewerPage == null) {
			_paletteViewerPage = createPaletteViewerPage();
		}
		return _paletteViewerPage;
	}

	/**
	 * @return the palette factory
	 */
	protected IPaletteFactory initPaletteFactory() {
		if (_paletteViewerPageFactory == null) {
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
					if (element.getName().equals(IJMTConstants.PALETTE_FACTORY)) {
						elementEditElement[j].getAttribute("class"); //$NON-NLS-1$
						Object obj;
						try {
							obj = elementEditElement[j].createExecutableExtension("class"); //$NON-NLS-1$

							// TODO: we need a policy based solution here,
							// but this will do for now
							if (obj instanceof IPaletteFactory) {
								_paletteViewerPageFactory = (IPaletteFactory) obj;
							}
						} catch (CoreException e) {
							PDPlugin.log("Problem loading element edit extension for " + element.toString(), e); //$NON-NLS-1$
						}
					}
				}
			}
		}
		return _paletteViewerPageFactory;
	}
	//
	// @Override
	// public Object getAdapter(Class type) {
	// if (type == PalettePage.class) {
	// return getPaletteViewerPage();
	// }
	// return super.getAdapter(type);
	// }

}