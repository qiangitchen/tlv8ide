package com.tulin.v8.ide.ui.editors.page.design.policies;

import static org.eclipse.draw2d.PositionConstants.*;
import static org.eclipse.gef.SharedCursors.ARROW;

import java.util.*;

import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.*;
import org.eclipse.gef.tools.*;

/**
 * A specialized {@link EditPolicy} for selecting and positioning {@link MarriageEditPart}
 * s. Selection is indicated via four square handles at each corner of the editpart's
 * figure (North, East, South, and West). All of these handles return
 * org.eclipse.gef.tools.DragEditPartsTrackers, which allows the current selection to be
 * dragged. During feedback, a rectangle filled using XOR and outlined with dashes is
 * drawn.
 */
public class NonResizableMarriageEditPolicy extends NonResizableEditPolicy
{
	/**
	 * Create and return a collection of four square handles at each corner of the
	 * editpart's figure (North, East, South, and West)
	 */
	protected List<Handle> createSelectionHandles() {
		List<Handle> list = new ArrayList<Handle>();
		GraphicalEditPart part = (GraphicalEditPart) getHost();
		DragTracker tracker = new DragEditPartsTracker(getHost());
		list.add(moveHandle(part, tracker));
		list.add(createHandle(part, NORTH, tracker));
		list.add(createHandle(part, EAST, tracker));
		list.add(createHandle(part, SOUTH, new ConnectionDragCreationTool()));
		list.add(createHandle(part, WEST, tracker));
		return list;
	}

	/**
	 * Create and return a diamond shaped handle that outlines the editpart with a 1-pixel
	 * black line
	 */
	Handle moveHandle(GraphicalEditPart owner, DragTracker tracker) {
		MoveHandle moveHandle = new MoveHandle(owner);
		moveHandle.setBorder(null);
		moveHandle.setDragTracker(tracker);
		moveHandle.setCursor(ARROW);
		return moveHandle;
	}

	/**
	 * Create and return a small square handle.
	 */
	Handle createHandle(GraphicalEditPart owner, int direction, DragTracker tracker) {
		ResizeHandle handle = new ResizeHandle(owner, direction);
		handle.setCursor(ARROW);
		handle.setDragTracker(tracker);
		return handle;
	}
}