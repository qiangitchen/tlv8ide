package com.tulin.v8.ide.ui.editors.page;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jst.pagedesigner.utils.SelectionHelper;
import org.eclipse.swt.custom.StyledText;

/**
 * This class handles selection synchronization between the designer and other
 * parts. It listens event from both ViewerSelectionManager and the
 * IDesignerView, and convert the events to each other.
 * <p>
 * SelectionSynchronizer will be registered on the ViewerSelectionManager,
 * basically listens to selection change of other parts, and make the designer
 * sync with them.
 * <p>
 * As ViewerSelectionManager is firing out both textSelectionChange and
 * nodeSelectionChange, we only need to listen to one of them. As
 * textSelectionChange provide more information than nodeSelectionChange, so
 * we'll listen only to textSelectionChange.
 * 
 * @author mengbo
 * @version 1.5
 */
@SuppressWarnings("restriction")
public class SelectionSynchronizer implements ISelectionChangedListener {
	private boolean _firingChange = false;

	private SimpleGraphicalEditor _editor;

	/**
	 * @param editor
	 */
	public SelectionSynchronizer(SimpleGraphicalEditor editor) {
		_editor = editor;
	}

	/**
	 * @return true if the status check is okay
	 */
	protected boolean statusCheckOk() {
		try {
			StyledText text = _editor.getHTMLEditor().getTextEditor().getTextViewer().getTextWidget();
			if (text == null || text.isDisposed()) {
				return false;
			}
			return true;
		} catch (NullPointerException ex) {
			return false;
		}
	}

	/**
	 * This is for event from the designer.
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();

		if (!_firingChange) {
			// check current status
			if (!statusCheckOk()) {
				return;
			}

			_firingChange = true;
			try {
				// convert the designer selection into SSE selection
				// (IStructureSelection of nodes
				// or textSelection, and let the ViewerSelectionManager to
				// handle it.
				// if (selection instanceof IStructuredSelection)
				// {
				// IStructuredSelection nodeSel =
				// SelectionHelper.convertFromDesignSelection((IStructuredSelection)selection);
				// can't use DoubleClickEvent, since it requre a Viewer.
				// _viewerSelectionManager.doubleClick(new
				// DoubleClickEvent(null, nodeSel));
				// }
				// else if (selection instanceof DesignRange)
				// {
				// ITextSelection srcselection =
				// SelectionHelper.convertFromDesignSelection((DesignRange)selection);
				// event = new SelectionChangedEvent(_editor.getGraphicViewer(),
				// srcselection);
				// _viewerSelectionManager.selectionChanged(event);
				// }
				ITextSelection srcselection = SelectionHelper.convertFromDesignSelectionToTextSelection(selection);

				// ideally, we should let the text editor display the selection
				// through calls to _viewerSelectionManager,
				// but seemed _viewerSelectionManager don't support that, so we
				// do workaround by calling the text editor (lium)
				_editor.getHTMLEditor().getTextEditor().selectAndReveal(srcselection.getOffset(),
						srcselection.getLength());
			} finally {
				_firingChange = false;
			}
		}
	}

	/**
	 * We are listening to the selection change in ViewerSelectionManager. The
	 * original source of the event could be source view or the outline view or
	 * other party that participate in the ViewerSelectionManager.
	 * 
	 * @param start
	 * @param end
	 */
	public void textSelectionChanged(int start, int end) {
		// Bug 332479 - [WPE] Design page processing still takes place even when
		// design page is hidden
		if (!htmlEditorIsInSourceOnlyMode()) {
			if (!_firingChange) {
				try {
					_firingChange = true;

					// XXX: workaround a SSE problem. In SSE, when user select a
					// range, it will fire two textSelectionChange event
					// the first one indicate the correct range, the second one
					// is
					// zero size for caret position.
					// @see ViewerSelectionManagerImpl.caretMoved
					// We try to ignore the second event by checking whether the
					// current real selection is empty
					if (start == end) {
						ITextSelection sel = (ITextSelection) _editor.getHTMLEditor().getTextEditor()
								.getSelectionProvider().getSelection();
						if (sel.getLength() != 0) {
							return;
						}
					}

					if (start > end) {
						int temp = start;
						start = end;
						end = temp;
					}
					int offset = start;
					int length = end - start;

					ITextSelection oldSelection = SelectionHelper
							.convertFromDesignSelectionToTextSelection(_editor.getGraphicViewer().getSelection());
					if (oldSelection != null && oldSelection.getOffset() == offset
							&& oldSelection.getLength() == length) {
						return;
					}

					ISelection selection = SelectionHelper.convertToDesignerSelection(this._editor.getGraphicViewer(),
							offset, length);
					_editor.getGraphicViewer().setSelection(selection);
				} finally {
					_firingChange = false;
				}
			}
		}
	}

	/**
	 * Returns true if HTMLEditor is in source-only mode.
	 * 
	 * @return <code>true</code> if HTMLEditor is in source-only mode, else
	 *         <code>false</code>.
	 */
	protected boolean htmlEditorIsInSourceOnlyMode() {
		boolean ret = false;
		if (_editor != null) {
			ret = (_editor.getHTMLEditor().getDesignerMode() == WebPageEditor1.MODE_SOURCE);
		}
		return ret;
	}

}
