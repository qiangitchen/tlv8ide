package com.tulin.v8.ide.ui.editors.page.sash;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jst.pagedesigner.PDPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.INestableKeyBindingService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * @author mengbo
 */
@SuppressWarnings({ "restriction", "deprecation", "unchecked", "rawtypes" })
public class SashEditorSite implements IEditorSite {

	/**
	 * The nested editor.
	 */
	private IEditorPart _editor;

	/**
	 * The multi-page editor.
	 */
	private SashEditorPart _sashEditor;

	/**
	 * The selection provider; <code>null</code> if none.
	 * 
	 * @see SashEditorSite#setSelectionProvider(ISelectionProvider)
	 */
	private ISelectionProvider _selectionProvider = null;

	/**
	 * The selection change listener, initialized lazily; <code>null</code> if
	 * not yet created.
	 */
	private ISelectionChangedListener _selectionChangedListener = null;

	/**
	 * The cached copy of the key binding service specific to this sash editor
	 * site. This value is <code>null</code> if it is not yet initialized.
	 */
	private IKeyBindingService _service = null;

	/**
	 * The list of popup menu extenders; <code>null</code> if none registered.
	 */
	// TODO: dead? private ArrayList _menuExtenders;

	/**
	 * Creates a site for the given editor nested within the given multi-page
	 * editor.
	 * 
	 * @param sashEditor
	 *            the multi-page editor
	 * @param editor
	 *            the nested editor
	 */
	public SashEditorSite(SashEditorPart sashEditor, IEditorPart editor) {
		Assert.isNotNull(sashEditor);
		Assert.isNotNull(editor);
		this._sashEditor = sashEditor;
		this._editor = editor;
	}

	/**
	 * Dispose the contributions.
	 */
	public void dispose() {
		// if (_menuExtenders != null) {
		// for (int i = 0, size = _menuExtenders.size(); i < size; i++) {
		// ((PopupMenuExtender) _menuExtenders.get(i)).dispose();
		// }
		// _menuExtenders = null;
		// }

		// Remove myself from the list of nested key binding services.
		if (_service != null) {
			IKeyBindingService parentService = getEditor().getSite().getKeyBindingService();
			if (parentService instanceof INestableKeyBindingService) {
				INestableKeyBindingService nestableParent = (INestableKeyBindingService) parentService;
				nestableParent.removeKeyBindingService(this);
			}
			_service = null;
		}
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IEditorSite</code> method returns <code>null</code>, since nested
	 * editors do not have their own action bar contributor.
	 * 
	 * @return <code>null</code>
	 */
	public IEditorActionBarContributor getActionBarContributor() {
		return null;
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IEditorSite</code> method forwards to the multi-page editor to
	 * return the action bars.
	 * 
	 * @return The action bars from the parent multi-page editor.
	 */
	public IActionBars getActionBars() {
		return _sashEditor.getEditorSite().getActionBars();
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method forwards to the multi-page editor
	 * to return the decorator manager.
	 * 
	 * @return The decorator from the workbench window.
	 * @deprecated use IWorkbench.getDecoratorManager()
	 */
	public ILabelDecorator getDecoratorManager() {
		return getWorkbenchWindow().getWorkbench().getDecoratorManager().getLabelDecorator();
	}

	/**
	 * Returns the nested editor.
	 * 
	 * @return the nested editor
	 */
	public IEditorPart getEditor() {
		return _editor;
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method returns an empty string since the
	 * nested editor is not created from the registry.
	 * 
	 * @return An empty string.
	 */
	public String getId() {
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc) Method declared on IEditorSite.
	 */
	public IKeyBindingService getKeyBindingService() {
		if (_service == null) {
			_service = getSashEditor().getEditorSite().getKeyBindingService();
			if (_service instanceof INestableKeyBindingService) {
				INestableKeyBindingService nestableService = (INestableKeyBindingService) _service;
				_service = nestableService.getKeyBindingService(this);

			} else {
				/*
				 * This is an internal reference, and should not be copied by
				 * client code. If you are thinking of copying this, DON'T DO
				 * IT.
				 */
				PDPlugin.getLogger(SashEditorSite.class).info(
						"MultiPageEditorSite.getKeyBindingService()   Parent key binding service was not an instance of INestableKeyBindingService.  It was an instance of " //$NON-NLS-1$
								+ _service.getClass().getName() + " instead."); //$NON-NLS-1$
			}
		}

		return _service;
	}

	/**
	 * Returns the sash editor.
	 * 
	 * @return the sash editor
	 */
	public SashEditorPart getSashEditor() {
		return _sashEditor;
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method forwards to the multi-page editor
	 * to return the workbench page.
	 * 
	 * @return The workbench page in which this editor site resides.
	 */
	public IWorkbenchPage getPage() {
		return getSashEditor().getSite().getPage();
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method returns an empty string since the
	 * nested editor is not created from the registry.
	 * 
	 * @return An empty string.
	 */
	public String getPluginId() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method returns an empty string since the
	 * nested editor is not created from the registry.
	 * 
	 * @return An empty string.
	 */
	public String getRegisteredName() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the selection changed listener which listens to the nested
	 * editor's selection changes, and calls
	 * <code>handleSelectionChanged</code>.
	 * 
	 * @return the selection changed listener
	 */
	private ISelectionChangedListener getSelectionChangedListener() {
		if (_selectionChangedListener == null) {
			_selectionChangedListener = new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					SashEditorSite.this.handleSelectionChanged(event);
				}
			};
		}
		return _selectionChangedListener;
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method returns the selection provider set
	 * by <code>setSelectionProvider</code>.
	 * 
	 * @return The current selection provider.
	 */
	public ISelectionProvider getSelectionProvider() {
		return _selectionProvider;
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method forwards to the multi-page editor
	 * to return the shell.
	 * 
	 * @return The shell in which this editor site resides.
	 */
	public Shell getShell() {
		return getSashEditor().getSite().getShell();
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method forwards to the multi-page editor
	 * to return the workbench window.
	 * 
	 * @return The workbench window in which this editor site resides.
	 */
	public IWorkbenchWindow getWorkbenchWindow() {
		return getSashEditor().getSite().getWorkbenchWindow();
	}

	/**
	 * Handles a selection changed event from the nested editor. The default
	 * implementation gets the selection provider from the multi-page editor's
	 * site, and calls <code>fireSelectionChanged</code> on it (only if it is an
	 * instance of <code>SashSelectionProvider</code>), passing a new event
	 * object.
	 * <p>
	 * Subclasses may extend or reimplement this method.
	 * </p>
	 * 
	 * @param event
	 *            the event
	 */
	public void handleSelectionChanged(SelectionChangedEvent event) {
		// we'll only make the parent editor site fire the selection change
		// event
		// when we (the sasheditorsite) is the active editor in the parent site.
		if (getSashEditor().getActiveEditor() == this.getPart()) {
			ISelectionProvider parentProvider = getSashEditor().getSite().getSelectionProvider();
			if (parentProvider instanceof SashEditorSelectionProvider) {
				SelectionChangedEvent newEvent = new SelectionChangedEvent(parentProvider, event.getSelection());
				((SashEditorSelectionProvider) parentProvider).fireSelectionChanged(newEvent);
			}
		}
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method forwards to the multi-page editor
	 * for registration.
	 * 
	 * @param menuID
	 *            The identifier for the menu.
	 * @param menuMgr
	 *            The menu manager
	 * @param selProvider
	 *            The selection provider.
	 */
	public void registerContextMenu(String menuID, MenuManager menuMgr, ISelectionProvider selProvider) {
		// if (_menuExtenders == null) {
		// _menuExtenders = new ArrayList(1);
		// }
		// cancel the registration of PopupMenuExtender since the
		// PopupMenuExtender's behavior
		// is different between eclipse 3.0 and eclipse 3.1,and we always have
		// one context
		// menu listener,no need add PopupMenuExtender as the second
		// listener(workaroud for bug 408295-1)
		// _menuExtenders.add(new PopupMenuExtender(menuID, menuMgr,
		// selProvider,
		// _editor));
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method forwards to the multi-page editor
	 * for registration.
	 * 
	 * @param menuManager
	 *            The menu manager
	 * @param selProvider
	 *            The selection provider.
	 */
	public void registerContextMenu(MenuManager menuManager, ISelectionProvider selProvider) {
		getSashEditor().getSite().registerContextMenu(menuManager, selProvider);
	}

	/**
	 * The <code>SashEditorSite</code> implementation of this
	 * <code>IWorkbenchPartSite</code> method remembers the selection provider,
	 * and also hooks a listener on it, which calls
	 * <code>handleSelectionChanged</code> when a selection changed event
	 * occurs.
	 * 
	 * @param provider
	 *            The selection provider.
	 * @see SashEditorSite#handleSelectionChanged(SelectionChangedEvent)
	 */
	public void setSelectionProvider(ISelectionProvider provider) {
		ISelectionProvider oldSelectionProvider = _selectionProvider;
		_selectionProvider = provider;
		if (oldSelectionProvider != null) {
			oldSelectionProvider.removeSelectionChangedListener(getSelectionChangedListener());
		}
		if (_selectionProvider != null) {
			_selectionProvider.addSelectionChangedListener(getSelectionChangedListener());
		}
	}

	/**
	 * @param job
	 */
	public void progressEnd(Job job) {
		// Do nothing
	}

	/**
	 * @param job
	 */
	public void progressStart(Job job) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPartSite#getPart()
	 */
	public IWorkbenchPart getPart() {
		return _editor;
	}

	public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider,
			boolean includeEditorInput) {
		// do nothing
	}

	public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider,
			boolean includeEditorInput) {
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceLocator#getService(java.lang.Class)
	 */
	public Object getService(Class api) {
		Object service = null;
		if (api != null && _sashEditor != null) {
			final IWorkbenchPartSite site = _sashEditor.getSite();
			if (site != null) {
				service = site.getService(api);
			}
		}
		return service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceLocator#hasService(java.lang.Class)
	 */
	public boolean hasService(Class api) {
		boolean has = false;
		if (api != null && _sashEditor != null) {
			final IWorkbenchPartSite site = _sashEditor.getSite();
			if (site != null) {
				has = site.hasService(api);
			}
		}
		return has;
	}

}
