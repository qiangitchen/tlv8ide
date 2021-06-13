package com.tulin.v8.ide.ui.editors.page.design.parts;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.*;
import org.eclipse.gef.requests.*;

import com.tulin.v8.ide.ui.editors.page.design.anchors.MarriageAnchor;
import com.tulin.v8.ide.ui.editors.page.design.commands.CreateConnectionCommand;
import com.tulin.v8.ide.ui.editors.page.design.commands.DeleteMarriageCommand;
import com.tulin.v8.ide.ui.editors.page.design.figures.MarriageFigure;
import com.tulin.v8.ide.ui.editors.page.design.model.GenealogyGraph;
import com.tulin.v8.ide.ui.editors.page.design.model.Marriage;
import com.tulin.v8.ide.ui.editors.page.design.model.connection.GenealogyConnection;
import com.tulin.v8.ide.ui.editors.page.design.model.listener.MarriageListener;
import com.tulin.v8.ide.ui.editors.page.design.policies.MarriageGraphicalNodeEditPolicy;

/**
 * The {@link EditPart} for the {@link Marriage} model object. This EditPart is
 * responsible for creating a visual representation for the model object and for
 * updating that visual representation as the model changes.
 */
public class MarriageEditPart extends GenealogyElementEditPart implements
		MarriageListener, NodeEditPart {
	public MarriageEditPart(Marriage marriage) {
		setModel(marriage);
	}

	public Marriage getModel() {
		return (Marriage) super.getModel();
	}

	/**
	 * Create and return the figure representing this model object
	 */
	protected IFigure createFigure() {
		return new MarriageFigure(getModel().getYearMarried());
	}

	/**
	 * Answer a collection of connection model objects that originate from the
	 * receiver.
	 */
	protected List<GenealogyConnection> getModelSourceConnections() {
		//Marriage model = getModel();
		ArrayList<GenealogyConnection> offspringList = new ArrayList<GenealogyConnection>();
		return offspringList;
	}

	/**
	 * Return an instance of {@link MarriageAnchor} so that the connection
	 * originates along the edge of the {@link MarriageFigure} rather than along
	 * the edge of the figure's bounding box. This is called once a connection
	 * has been established.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new MarriageAnchor(getFigure());
	}

	/**
	 * Return an instance of {@link MarriageAnchor} so that the connection
	 * creation originates along the edge of the {@link MarriageFigure} rather
	 * than along the edge of the figure's bounding box. This is called when the
	 * user is interactively creating a connection.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request)
					.getConnectionEditPart();
			if (!(part instanceof GenealogyConnectionEditPart))
				return null;
			GenealogyConnectionEditPart connPart = (GenealogyConnectionEditPart) part;
			CreateConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidSource(getModel()))
				return null;
			return new MarriageAnchor(getFigure());
		}
		return new MarriageAnchor(getFigure());
	}

	/**
	 * Answer a collection of connection model objects that terminate at the
	 * receiver.
	 */
	protected List<GenealogyConnection> getModelTargetConnections() {
		//Marriage marriage = getModel();
		ArrayList<GenealogyConnection> marriageList = new ArrayList<GenealogyConnection>(
				1);
		return marriageList;
	}

	/**
	 * Return an instance of {@link MarriageAnchor} so that the connection
	 * terminates along the edge of the {@link MarriageFigure} rather than along
	 * the edge of the figure's bounding box. This is called once a connection
	 * has been established.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new MarriageAnchor(getFigure());
	}

	/**
	 * If this request is creating a connection from a {@link PersonEditPart} to
	 * the receiver, then return an instance of {@link MarriageAnchor} so that
	 * the connection creation snaps to the figure and terminates along the edge
	 * of the {@link MarriageFigure} rather than along the edge of the figure's
	 * bounding box. If the connection source is NOT from a
	 * {@link PersonEditPart} then return <code>null</code> so that the
	 * connection does not appear to connect to the receiver. This is called
	 * when the user is interactively creating a connection.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof CreateConnectionRequest) {
			Command cmd = ((CreateConnectionRequest) request).getStartCommand();
			if (!(cmd instanceof CreateConnectionCommand))
				return null;
			if (!((CreateConnectionCommand) cmd).isValidTarget(getModel()))
				return null;
			return new MarriageAnchor(getFigure());
		}
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request)
					.getConnectionEditPart();
			if (!(part instanceof GenealogyConnectionEditPart))
				return null;
			GenealogyConnectionEditPart connPart = (GenealogyConnectionEditPart) part;
			CreateConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidTarget(getModel()))
				return null;
			return new MarriageAnchor(getFigure());
		}
		return null;
	}

	/**
	 * Extend the superclass behavior to modify the associated figure's
	 * appearance to show that the element is selected.
	 */
	protected void fireSelectionChanged() {
		((MarriageFigure) getFigure()).setSelected(getSelected() != 0);
		super.fireSelectionChanged();
	}

	/**
	 * Create and install {@link EditPolicy} instances used to define behavior
	 * associated with this EditPart's figure.
	 */
	protected void createEditPolicies() {
		// Resize feedback provided by container
		// NonResizableEditPolicy selectionPolicy = new
		// NonResizableMarriageEditPolicy();
		// installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
		// selectionPolicy);

		// Handles deleting the receiver
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			protected Command createDeleteCommand(GroupRequest request) {
				GenealogyGraph graph = (GenealogyGraph) getParent().getModel();
				return new DeleteMarriageCommand(graph, getModel());
			}
		});

		// Handles connection creation and retargeting
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new MarriageGraphicalNodeEditPolicy(getModel()));
	}

	/**
	 * Override the superclass implementation so that the receiver can add
	 * itself as a listener to the underlying model object
	 */
	public void addNotify() {
		super.addNotify();
		getModel().addMarriageListener(this);
	}

	/**
	 * Override the superclass implementation so that the receiver can stop
	 * listening to events from the underlying model object
	 */
	public void removeNotify() {
		getModel().removeMarriageListener(this);
		super.removeNotify();
	}

	// =================================================================
	// MarriageListener

	public void yearMarriedChanged(int yearMarried) {
		((MarriageFigure) getFigure()).setYearMarried(yearMarried);
	}
}
