package com.tulin.v8.ide.ui.editors.page.sash;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;

/**
 * @author mengbo
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SashEditorSelectionProvider implements IPostSelectionProvider {

	/**
	 * Registered selection changed listeners (element type:
	 * <code>ISelectionChangedListener</code>).
	 */
	private ListenerList _listeners = new ListenerList(ListenerList.IDENTITY);

	private ListenerList _postSelectionChangedListeners = new ListenerList(ListenerList.IDENTITY);

	/**
	 * The multi-page editor.
	 */
	private SashEditorPart _sashEditor;

	/**
	 * Creates a selection provider for the given multi-page editor.
	 * 
	 * @param sashEditor
	 *            the multi-page editor
	 */
	public SashEditorSelectionProvider(SashEditorPart sashEditor) {
		Assert.isNotNull(sashEditor);
		this._sashEditor = sashEditor;
	}

	/*
	 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		_listeners.add(listener);
	}

	/**
	 * Notifies all registered selection changed listeners that the editor's
	 * selection has changed. Only listeners registered at the time this method
	 * is called are notified.
	 * 
	 * @param event
	 *            the selection changed event
	 */
	public void fireSelectionChanged(final SelectionChangedEvent event) {
		Object[] listeners = this._listeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}

	/**
	 * Returns the sash editor.
	 * 
	 * @return the sash editor part
	 */
	public SashEditorPart getSashEditor() {
		return _sashEditor;
	}

	/*
	 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
	 */
	public ISelection getSelection() {
		IEditorPart activeEditor = _sashEditor.getActiveEditor();
		if (activeEditor != null) {
			ISelectionProvider selectionProvider = activeEditor.getSite().getSelectionProvider();
			if (selectionProvider != null)
				return selectionProvider.getSelection();
		}
		return null;
	}

	/*
	 * (non-JavaDoc) Method declaed on <code>ISelectionProvider</code>.
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		_listeners.remove(listener);
	}

	/*
	 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
	 */
	public void setSelection(ISelection selection) {
		IEditorPart activeEditor = _sashEditor.getActiveEditor();
		if (activeEditor != null) {
			ISelectionProvider selectionProvider = activeEditor.getSite().getSelectionProvider();
			if (selectionProvider != null)
				selectionProvider.setSelection(selection);
		}
	}

	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		_postSelectionChangedListeners.add(listener);
	}

	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		_postSelectionChangedListeners.remove(listener);
	}

	/**
	 * Notifies any post selection listeners that a post selection event has
	 * been received. Only listeners registered at the time this method is
	 * called are notified.
	 * 
	 * @param event
	 *            a selection changed event
	 * 
	 * @see #addPostSelectionChangedListener(ISelectionChangedListener)
	 */
	public void firePostSelectionChanged(final SelectionChangedEvent event) {
		Object[] listeners = _postSelectionChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
			SafeRunnable.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}
}
