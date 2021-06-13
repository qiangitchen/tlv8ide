package com.tulin.v8.ide.ui.editors.page.design.parts;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.*;
import org.eclipse.gef.requests.*;

import com.tulin.v8.ide.ui.editors.page.design.commands.CreateNoteCommand;
import com.tulin.v8.ide.ui.editors.page.design.commands.MoveAndResizeGenealogyElementCommand;
import com.tulin.v8.ide.ui.editors.page.design.commands.ReparentNoteCommand;
import com.tulin.v8.ide.ui.editors.page.design.model.GenealogyElement;
import com.tulin.v8.ide.ui.editors.page.design.model.GenealogyGraph;
import com.tulin.v8.ide.ui.editors.page.design.model.Marriage;
import com.tulin.v8.ide.ui.editors.page.design.model.Note;
import com.tulin.v8.ide.ui.editors.page.design.model.NoteContainer;
import com.tulin.v8.ide.ui.editors.page.design.model.listener.GenealogyGraphListener;
import com.tulin.v8.ide.ui.editors.page.design.policies.NonResizableMarriageEditPolicy;

/**
 * The {@link EditPart} for the {@link GenealogyGraph} model object. This EditPart is
 * responsible for creating the layer in which all other figures are placed and for
 * returning the collection of top level model objects to be displayed in that layer.
 */
public class GenealogyGraphEditPart extends AbstractGraphicalEditPart
	implements GenealogyGraphListener
{
	public GenealogyGraphEditPart(GenealogyGraph genealogyGraph) {
		setModel(genealogyGraph);
		genealogyGraph.addGenealogyGraphListener(this);
	}

	public GenealogyGraph getModel() {
		return (GenealogyGraph) super.getModel();
	}

	/**
	 * Construct and return the layer in which all other genealogy figures are displayed
	 */
	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}

	/**
	 * Return a collection of top level genealogy model objects to be displayed
	 */
	protected List<GenealogyElement> getModelChildren() {
		List<GenealogyElement> allObjects = new ArrayList<GenealogyElement>();
//		allObjects.addAll(getModel().getMarriages());
//		allObjects.addAll(getModel().getPeople());
		allObjects.addAll(getModel().getNotes());
		return allObjects;
	}

	protected void createEditPolicies() {
		
		// Disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		
		// Handles constraint changes (e.g. moving and/or resizing) of model elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {
			protected Command getCreateCommand(CreateRequest request) {
				Object type = request.getNewObjectType();
				Rectangle box = (Rectangle) getConstraintFor(request);
				GenealogyGraph graph = getModel();
				if (type == Note.class) {
					Note note = (Note) request.getNewObject();
					return new CreateNoteCommand(graph, note, box);
				}
				return null;
			}

			/**
			 * IGNORE: This method is a holdover from earlier GEF frameworks
			 * and will be removed in future versions of GEF.
			 */
			protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
				return null;
			}
			
			/**
			 * Return a command for moving elements around the canvas
			 */
			protected Command createChangeConstraintCommand(
				ChangeBoundsRequest request, EditPart child, Object constraint
			) {
				GenealogyElement elem = (GenealogyElement) child.getModel();
				Rectangle box = (Rectangle) constraint;
				return new MoveAndResizeGenealogyElementCommand(elem, box);
			}
			
			/**
			 * Exclude MarriageEditParts from being resized
			 */
			protected EditPolicy createChildEditPolicy(EditPart child) {
				if (child instanceof MarriageEditPart)
					return new NonResizableMarriageEditPolicy();
				return super.createChildEditPolicy(child);
			}
			
			/**
			 * Called when a Note is dragged from a Person on the canvas.
			 * Return a command for reparenting the note
			 */
			protected Command createAddCommand(EditPart child, Object constraint) {
				NoteContainer oldContainer = (NoteContainer) child.getParent().getModel();
				if (getModel() == oldContainer)
					return null;
				Note note = (Note) child.getModel();
				ReparentNoteCommand cmd = new ReparentNoteCommand(getModel(), note);
				cmd.setBounds((Rectangle) constraint);
				cmd.setOldContainer(oldContainer);
				return cmd;
			}
		});
	}

	// ===============================================================
	// GenealogyGraphListener

	/**
	 * When a marriage is added to the model,
	 * add a new EditPart to manage the corresponding figure.
	 */
	public void marriageAdded(Marriage m) {
		addChild(createChild(m), 0);
	}

	/**
	 * When a marriage is removed from the model,
	 * find and remove the corresponding edit part
	 */
	public void marriageRemoved(Marriage m) {
		genealogyElementRemoved(m);
	}

	/**
	 * When a note is added to the model,
	 * add a new EditPart to manage the corresponding figure.
	 */
	public void noteAdded(int index, Note n) {
		addChild(createChild(n), index);
	}

	/**
	 * When a note is removed from the model,
	 * find and remove the corresponding edit part
	 */
	public void noteRemoved(Note n) {
		genealogyElementRemoved(n);
	}

	/**
	 * When an element is removed from the model,
	 * find and remove the corresponding edit part
	 */
	private void genealogyElementRemoved(GenealogyElement elem) {
		Object part = getViewer().getEditPartRegistry().get(elem);
		if (part instanceof EditPart)
			removeChild((EditPart) part);
	}

	public void graphCleared() {
	}

}