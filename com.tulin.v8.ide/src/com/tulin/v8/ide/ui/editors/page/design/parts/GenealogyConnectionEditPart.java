package com.tulin.v8.ide.ui.editors.page.design.parts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.*;
import org.eclipse.gef.requests.*;

import com.tulin.v8.ide.ui.editors.page.design.commands.CreateConnectionCommand;
import com.tulin.v8.ide.ui.editors.page.design.commands.DeleteGenealogyConnectionCommand;
import com.tulin.v8.ide.ui.editors.page.design.model.connection.GenealogyConnection;

/**
 * The {@link EditPart} for the {@link GenealogyConnection} model object. This EditPart is
 * responsible for creating the connection between the person and marriage figures.
 */
public class GenealogyConnectionEditPart extends AbstractConnectionEditPart
{
	private static final PointList ARROWHEAD = new PointList(new int[]{
		0, 0, -2, 2, -2, 0, -2, -2, 0, 0
	});

	public GenealogyConnectionEditPart(GenealogyConnection marriageConnection) {
		setModel(marriageConnection);
	}

	public GenealogyConnection getModel() {
		return (GenealogyConnection) super.getModel();
	}

	/**
	 * Create a new connection between the person and marriage referenced by the
	 * reciever's model object. The arrow head fill color depends upon whether the person
	 * is a parent or an offspring in the referenced marriage.
	 */
	protected IFigure createFigure() {
		return createFigure(getModel().isOffspringConnection());
	}

	/**
	 * Create a new connection figure. The arrow head fill color depends upon whether the
	 * person is a parent or an offspring in the referenced marriage.
	 */
	public static Connection createFigure(boolean isOffspringConnection) {
		PolylineConnection connection = new PolylineConnection();

		// Add an arrowhead decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(isOffspringConnection
			? ColorConstants.white
			: ColorConstants.darkGray);
		connection.setTargetDecoration(decoration);

		return connection;
	}

	/**
	 * Answer a new command to recreate the receiver
	 */
	public CreateConnectionCommand recreateCommand() {
		CreateConnectionCommand cmd = null;
		if (getModel().isOffspringConnection()) {
//			cmd = new CreateOffspringConnectionCommand(getModel().marriage);
//			cmd.setTarget(getModel().person);
		}
		else {
//			cmd = new CreateSpouseConnectionCommand(getModel().person);
//			cmd.setTarget(getModel().marriage);
		}
		return cmd;
	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getSourceConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getSource() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getSource();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
		return super.getSourceConnectionAnchor();
	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getTargetConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getTarget() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getTarget();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
		return super.getTargetConnectionAnchor();
	}

	/**
	 * Create and install {@link EditPolicy} instances used to define behavior
	 * associated with this EditPart's figure.
	 */
	protected void createEditPolicies() {
		ConnectionEndpointEditPolicy selectionPolicy = new ConnectionEndpointEditPolicy();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);

		// Handles deleting the receiver
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy() {
			protected Command getDeleteCommand(GroupRequest request) {
				return new DeleteGenealogyConnectionCommand(getModel());
			}
		});
	}
}
