package com.tulin.v8.ide.ui.editors.page.design;

import java.util.*;

import org.eclipse.gef.*;
import org.eclipse.jface.viewers.*;

/**
 * A selection manager that prevents nested parts from being selected
 * when their parent or grandparent part is selected.
 */
class ModifiedSelectionManager extends SelectionManager
{
	private final GraphicalViewer viewer;

	public ModifiedSelectionManager(GraphicalViewer viewer) {
		this.viewer = viewer;
	}
	
	/**
	 * Override the superclass behavior
	 * to cycle through the each of the selected EditParts 
	 * and removes any which have a selected ancestor.
	 */
	public void setSelection(ISelection selection) {
		
		// Build a collection of originally selected parts
		// and a collection from which nested parts are removed
		
		List<?> oldSelection = ((IStructuredSelection) selection).toList();
		final List<Object> newSelection = new ArrayList<Object>(oldSelection.size());
		newSelection.addAll(oldSelection);

		// Cycle through all selected parts and remove nested parts
		// which have a parent or grandparent part that is selected
		
		Iterator<Object> iter = newSelection.iterator();
		while (iter.hasNext())
			if (containsAncestor(newSelection, (EditPart) iter.next()))
				iter.remove();
		
		// Pass the revised selection to the superclass implementation
		// to perform the actual selection
		
		super.setSelection(new StructuredSelection(newSelection));
	}
	
	/**
	 * Override the superclass behavior
	 * to adjust the selection based upon whether the editpart
	 * is a nested part of an already selected ancestor
	 * or is an ancestor of already selected parts.
	 */
	public void appendSelection(EditPart part) {
		List<?> selection = ((IStructuredSelection) getSelection()).toList();
		
		// If "nothing" is selected then getSelection() returns
		// the viewer's primary edit part in which case the 
		// specified part should be selected.
		
		if (selection.size() == 1 && selection.get(0) == viewer.getContents()) {
			super.appendSelection(part);
			return;
		}
		
		// If the selection already contains an ancestor 
		// of the specified part then don't select the part
		
		if (containsAncestor(selection, part))
			return;

		// Deselect any currently selected parts
		// which have the new part as an ancestor 
		
		Iterator<?> iter = new ArrayList<Object>(selection).iterator();
		while (iter.hasNext()) {
			EditPart each = (EditPart) iter.next();
			if (isAncestor(part, each))
				deselect(each);
		}
		
		// Call the superclass implemention to select the part
		
		super.appendSelection(part);
	}

	/**
	 * Determine if the specified ancestor is indeed an ancestor
	 * of the specified part.
	 * 
	 * @param ancestor the ancestor (not <code>null</code>)
	 * @param part the part (not <code>null</code>)
	 * @return <code>true</code> if the ancestor is indeed an ancestor
	 * 		of the specified part, else false
	 */
	private static boolean isAncestor(EditPart ancestor, EditPart part) {
		while (part != null) {
			part = part.getParent();
			if (part == ancestor)
				return true;
		}
		return false;
	}

	/**
	 * Determine if the specified collection contains an ancestor
	 * of the specified edit part
	 * 
	 * @param list the collection of edit parts (not <code>null</code>)
	 * @param part the EditPart (not <code>null</code>)
	 * @return <code>true</code> if the collection contains an ancestor
	 * 		of the specified edit part, else <code>false</code>
	 */
	private static boolean containsAncestor(final List<?> list, EditPart part) {
		while (part != null) {
			part = part.getParent();
			if (list.contains(part))
				return true;
		}
		return false;
	}
}