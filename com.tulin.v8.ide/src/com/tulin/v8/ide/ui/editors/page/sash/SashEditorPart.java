package com.tulin.v8.ide.ui.editors.page.sash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jst.jsf.common.ui.internal.guiutils.SWTUtils;
import org.eclipse.jst.pagedesigner.PDPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.INestableKeyBindingService;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.part.MultiPageEditorSite;

/**
 * This class emulates the MultiPageEditorPart. But instead of using multipage,
 * it use SashForm to separate the editors.
 * 
 * @author mengbo
 */
@SuppressWarnings({ "restriction", "rawtypes", "deprecation" })
public abstract class SashEditorPart extends EditorPart {
	private int _orientation = SWT.VERTICAL;

	private SashForm _sashForm;

	/**
	 * List of nested editors. Element type: IEditorPart. Need to hang onto them
	 * here, in addition to using get/setData on the items, because dispose()
	 * needs to access them, but widgetry has already been disposed at that
	 * point.
	 */
	private ArrayList _nestedEditors = new ArrayList(3);

	private Map _editorToComposite = new HashMap();

	private IEditorPart _activeEditor = null;

	/**
	 * Creates and adds a new page containing the given editor to this
	 * multi-page editor. The page is added at the given index. This also hooks
	 * a property change listener on the nested editor.
	 * 
	 * @param editor
	 *            the nested editor
	 * @param input
	 *            the input for the nested editor
	 * @exception PartInitException
	 *                if a new page could not be created
	 * @see org.eclipse.ui.part.MultiPageEditorPart#handlePropertyChange(int)
	 *      the handler for property change events from the nested editor
	 */
	@SuppressWarnings("unchecked")
	public void addPage(final IEditorPart editor, IEditorInput input) throws PartInitException {
		IEditorSite site = createSite(editor);
		// call init first so that if an exception is thrown, we have created no
		// new widgets
		editor.init(site, input);
		final Composite parent1 = new Composite(getContainer(), SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = fillLayout.marginHeight = 1;
		parent1.setLayout(fillLayout);

		parent1.addListener(SWT.Activate, new Listener() {
			public void handleEvent(Event event) {
				if (event.type == SWT.Activate) {
					activeEditorChanged(editor);
					parent1.setBackground(ColorConstants.green);
				}
			}
		});
		parent1.addListener(SWT.Deactivate, new Listener() {
			public void handleEvent(Event event) {
				parent1.setBackground(ColorConstants.titleInactiveBackground);
			}
		});
		SWTUtils.workaroundResize(parent1);
		editor.createPartControl(parent1);
		editor.addPropertyListener(new IPropertyListener() {
			public void propertyChanged(Object source, int propertyId) {
				SashEditorPart.this.handlePropertyChange(propertyId);
			}
		});

		_nestedEditors.add(editor);
		_editorToComposite.put(editor, parent1);

		connectPage(editor);
	}

	/**
	 * @param editor
	 */
	protected void connectPage(IEditorPart editor) {
		ISelectionProvider editSelectionProvider = editor.getSite().getSelectionProvider();
		if (editSelectionProvider instanceof IPostSelectionProvider) {
			((IPostSelectionProvider) editSelectionProvider)
					.addPostSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							((SashEditorSelectionProvider) getSite().getSelectionProvider())
									.firePostSelectionChanged(event);
						}
					});
		} else if (editSelectionProvider != null) {
			editSelectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					((SashEditorSelectionProvider) getSite().getSelectionProvider()).fireSelectionChanged(event);
				}
			});
		}
	}

	/**
	 * Creates an empty container. Creates a CTabFolder with no style bits set,
	 * and hooks a selection listener which calls <code>pageChange()</code>
	 * whenever the selected tab changes.
	 * 
	 * @param parent
	 *            The composite in which the container tab folder should be
	 *            created; must not be <code>null</code>.
	 * @return a new container
	 */
	private SashForm createContainer(Composite parent) {
		// use SWT.FLAT style so that an extra 1 pixel border is not reserved
		// inside the folder
		SashForm newContainer = new SashForm(parent, SWT.NONE);
		SWTUtils.workaroundResize(newContainer);
		newContainer.setOrientation(_orientation);
		return newContainer;
	}

	/**
	 * @throws PartInitException
	 */
	abstract protected void createPages() throws PartInitException;

	/**
	 * The <code>MultiPageEditor</code> implementation of this
	 * <code>IWorkbenchPart</code> method creates the control for the multi-page
	 * editor by calling <code>createContainer</code>, then
	 * <code>createPages</code>. Subclasses should implement
	 * <code>createPages</code> rather than overriding this method.
	 * 
	 * @param parent
	 *            The parent in which the editor should be created; must not be
	 *            <code>null</code>.
	 */
	public final void createPartControl(Composite parent) {
		this._sashForm = createContainer(parent);

		try {
			createPages();
		} catch (PartInitException ex) {
			ex.printStackTrace();
		}
		// set the active page (page 0 by default), unless it has already been
		// done
		if (getActiveEditor() == null) {
			if (!_nestedEditors.isEmpty()) {
				setActiveEditor((IEditorPart) _nestedEditors.get(0));
			}
		}

		try{
			_sashForm.setWeights(new int[] { 1, 4 });
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Creates the site for the given nested editor. The
	 * <code>MultiPageEditorPart</code> implementation of this method creates an
	 * instance of <code>MultiPageEditorSite</code>. Subclasses may reimplement
	 * to create more specialized sites.
	 * 
	 * @param editor
	 *            the nested editor
	 * @return the editor site
	 */
	protected IEditorSite createSite(IEditorPart editor) {
		return new SashEditorSite(this, editor);
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		_activeEditor = null;
		for (int i = 0; i < _nestedEditors.size(); ++i) {
			IEditorPart editor = (IEditorPart) _nestedEditors.get(i);
			disposePart(editor);
		}
		_nestedEditors.clear();
		_editorToComposite.clear();
	}

	/**
	 * Returns the active nested editor if there is one.
	 * <p>
	 * Subclasses should not override this method
	 * </p>
	 * 
	 * @return the active nested editor, or <code>null</code> if none
	 */
	public IEditorPart getActiveEditor() {
		return _activeEditor;
	}

	/**
	 * Returns the composite control containing this multi-page editor's pages.
	 * This should be used as the parent when creating controls for the
	 * individual pages. That is, when calling <code>addPage(Control)</code>,
	 * the passed control should be a child of this container.
	 * <p>
	 * Warning: Clients should not assume that the container is any particular
	 * subclass of Composite. The actual class used may change in order to
	 * improve the look and feel of multi-page editors. Any code making
	 * assumptions on the particular subclass would thus be broken.
	 * </p>
	 * <p>
	 * Subclasses should not override this method
	 * </p>
	 * 
	 * @return the composite, or <code>null</code> if
	 *         <code>createPartControl</code> has not been called yet
	 */
	protected Composite getContainer() {
		return _sashForm;
	}

	/**
	 * Returns the editor for the given page index. The page index must be
	 * valid.
	 * 
	 * @param pageIndex
	 *            the index of the page
	 * @return the editor for the specified page, or <code>null</code> if the
	 *         specified page was not created with
	 *         <code>addPage(IEditorPart,IEditorInput)</code>
	 */
	protected IEditorPart getEditor(int pageIndex) {
		return (IEditorPart) _nestedEditors.get(pageIndex);
	}

	/**
	 * Handles a property change notification from a nested editor. The default
	 * implementation simply forwards the change to listeners on this multi-page
	 * editor by calling <code>firePropertyChange</code> with the same property
	 * id. For example, if the dirty state of a nested editor changes (property
	 * id <code>IEditorPart.PROP_DIRTY</code>), this method handles it by firing
	 * a property change event for <code>IEditorPart.PROP_DIRTY</code> to
	 * property listeners on this multi-page editor.
	 * <p>
	 * Subclasses may extend or reimplement this method.
	 * </p>
	 * 
	 * @param propertyId
	 *            the id of the property that changed
	 */
	protected void handlePropertyChange(int propertyId) {
		firePropertyChange(propertyId);
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IEditorPart</code> method sets its site to the given site, its
	 * input to the given input, and the site's selection provider to a
	 * <code>MultiPageSelectionProvider</code>. Subclasses may extend this
	 * method.
	 * 
	 * @param site
	 *            The site for which this part is being created; must not be
	 *            <code>null</code>.
	 * @param input
	 *            The input on which this editor should be created; must not be
	 *            <code>null</code>.
	 * @throws PartInitException
	 *             If the initialization of the part fails -- currently never.
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		site.setSelectionProvider(new SashEditorSelectionProvider(this));
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IEditorPart</code> method returns whether the contents of any of
	 * this multi-page editor's nested editors have changed since the last save.
	 * Pages created with <code>addPage(Control)</code> are ignored.
	 * <p>
	 * Subclasses may extend or reimplement this method.
	 * </p>
	 * 
	 * @return <code>true</code> if any of the nested editors are dirty;
	 *         <code>false</code> otherwise.
	 */
	public boolean isDirty() {
		// use nestedEditors to avoid SWT requests; see bug 12996
		for (Iterator i = _nestedEditors.iterator(); i.hasNext();) {
			IEditorPart editor = (IEditorPart) i.next();
			if (editor.isDirty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Notifies this multi-page editor that the page with the given id has been
	 * activated. This method is called when the user selects a different tab.
	 * <p>
	 * The <code>MultiPageEditorPart</code> implementation of this method sets
	 * focus to the new page, and notifies the action bar contributor (if there
	 * is one). This checks whether the action bar contributor is an instance of
	 * <code>MultiPageEditorActionBarContributor</code>, and, if so, calls
	 * <code>setActivePage</code> with the active nested editor. This also fires
	 * a selection change event if required.
	 * </p>
	 * <p>
	 * Subclasses may extend this method.
	 * </p>
	 * 
	 * @param activeEditor
	 * 
	 */
	protected void activeEditorChanged(IEditorPart activeEditor) {
		setActiveEditor(activeEditor);
		setFocus();

		IEditorSite site = getEditorSite();
		while (site != null) {
			IEditorActionBarContributor contributor = site.getActionBarContributor();
			if (contributor instanceof MultiPageEditorActionBarContributor) {
				((MultiPageEditorActionBarContributor) contributor).setActivePage(activeEditor);
			}
			if (site instanceof MultiPageEditorSite) {
				site = (IEditorSite) ((MultiPageEditorSite) site).getMultiPageEditor().getSite();
			} else if (site instanceof SashEditorSite) {
				site = (IEditorSite) ((SashEditorSite) site).getSashEditor().getSite();
			} else {
				site = null;
			}
		}

		if (activeEditor != null) {
			// Workaround for 1GAUS7C: ITPUI:ALL - Editor not activated when
			// restored from previous session
			// do not need second if once fixed
			ISelectionProvider selectionProvider = activeEditor.getSite().getSelectionProvider();
			if (selectionProvider != null) {
				if (selectionProvider.getSelection() != null) {
					SelectionChangedEvent event = new SelectionChangedEvent(selectionProvider,
							selectionProvider.getSelection());
					((SashEditorSelectionProvider) getSite().getSelectionProvider()).fireSelectionChanged(event);
				}
			}
		}
	}

	/**
	 * Disposes the given part and its site.
	 * 
	 * @param part
	 *            The part to dispose; must not be <code>null</code>.
	 */
	private void disposePart(final IWorkbenchPart part) {
		SafeRunner.run(new SafeRunnable() {
			public void run() {
				if (part.getSite() instanceof SashEditorSite) {
					SashEditorSite partSite = (SashEditorSite) part.getSite();
					partSite.dispose();
				}
				part.dispose();
			}

			public void handleException(Throwable e) {
				// Exception has already being logged by Core. Do nothing.
			}
		});
	}

	/**
	 * Sets the currently active page.
	 * 
	 * @param part
	 * 
	 */
	protected void setActiveEditor(IEditorPart part) {
		_activeEditor = part;
	}

	/**
	 * The <code>MultiPageEditor</code> implementation of this
	 * <code>IWorkbenchPart</code> method sets focus on the active nested
	 * editor, if there is one.
	 * <p>
	 * Subclasses may extend or reimplement.
	 * </p>
	 */
	public void setFocus() {
		setFocus(getActiveEditor());
	}

	/**
	 * Sets focus to the control for the given page. If the page has an editor,
	 * this calls its <code>setFocus()</code> method. Otherwise, this calls
	 * <code>setFocus</code> on the control for the page.
	 * 
	 * @param pageIndex
	 *            the index of the page
	 */
	@SuppressWarnings("unused")
	private void setFocus(IEditorPart editor) {
		final IKeyBindingService service = getSite().getKeyBindingService();

		if (editor == null) {
			// There is no selected page, so deactivate the active service.
			if (service instanceof INestableKeyBindingService) {
				final INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				nestableService.activateKeyBindingService(null);
			} else {
				// WorkbenchPlugin
				PDPlugin.getLogger(getClass()).error(
						"MultiPageEditorPart.setFocus()   Parent key binding service was not an instance of INestableKeyBindingService.  It was an instance of " //$NON-NLS-1$
								+ service.getClass().getName() + " instead."); //$NON-NLS-1$
			}
			return;
		}
		editor.setFocus();
		// There is no selected page, so deactivate the active service.
		if (service instanceof INestableKeyBindingService) {
			final INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
			if (editor != null) {
				nestableService.activateKeyBindingService(editor.getEditorSite());
			} else {
				nestableService.activateKeyBindingService(null);
			}
		} else {
			PDPlugin.getLogger(getClass()).error(
					"MultiPageEditorPart.setFocus()   Parent key binding service was not an instance of INestableKeyBindingService.  It was an instance of " //$NON-NLS-1$
							+ service.getClass().getName() + " instead."); //$NON-NLS-1$
		}
	}

	public void doSave(IProgressMonitor monitor) {
		if (_activeEditor != null) {
			_activeEditor.doSave(monitor);
		}
	}

	public void doSaveAs() {
		if (_activeEditor != null) {
			_activeEditor.doSaveAs();
		}

	}

	public boolean isSaveAsAllowed() {
		if (_activeEditor != null) {
			return _activeEditor.isSaveAsAllowed();
		}
		return false;
	}

	/**
	 * @param orientation
	 */
	public void setOrientation(int orientation) {
		this._orientation = orientation;
		if (_sashForm != null && !_sashForm.isDisposed()) {
			_sashForm.setMaximizedControl(null);
			_sashForm.setOrientation(_orientation);
		}
	}

	/**
	 * @param part
	 */
	public void setMaximizedEditor(IEditorPart part) {
		if (part != null) {
			Composite c = (Composite) _editorToComposite.get(part);
			if (c != null && _sashForm != null && !_sashForm.isDisposed()) {
				_sashForm.setMaximizedControl(c);
				part.setFocus();
			}
		} else {
			if (_sashForm != null && !_sashForm.isDisposed()) {
				_sashForm.setMaximizedControl(null);
			}
		}
	}
}
