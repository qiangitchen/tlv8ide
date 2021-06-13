package com.tulin.v8.ide.ui.editors.page.design.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.*;

import com.tulin.v8.ide.ui.editors.page.design.commands.CreateConnectionCommand;
import com.tulin.v8.ide.ui.editors.page.design.commands.DeleteGenealogyConnectionCommand;
import com.tulin.v8.ide.ui.editors.page.design.parts.GenealogyConnectionEditPart;

/**
 * A superclass for {@link PersonGraphicalNodeEditPolicy} and
 * {@link MarriageGraphicalNodeEditPolicy} to share behavior.
 */
public abstract class GenealogyElementGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy
{
	/**
	 * Answer the model element associated with the receiver
	 */
	protected abstract Object getModel();

	/**
	 * Answer a new connection command for the receiver.
	 */
	public abstract CreateConnectionCommand createConnectionCommand();

	/**
	 * Return a new connection command with the receiver's model as the "source".
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		request.setStartCommand(createConnectionCommand());
		return request.getStartCommand();
	}

	/**
	 * If the connection is valid with the receiver's model as its target then set the
	 * recevier's model as its target and return the command otherwise return
	 * <code>null</code> indicating that the connection is invalid and should not be
	 * created.
	 */
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Command startCmd = request.getStartCommand();
		if (!(startCmd instanceof CreateConnectionCommand))
			return null;
		CreateConnectionCommand connCmd = (CreateConnectionCommand) startCmd;
		if (!connCmd.isValidTarget(getModel()))
			return null;
		connCmd.setTarget(getModel());
		return connCmd;
	}

	/**
	 * If the connection modification request is valid with the recevier's model
	 * as the source, then return a command to modify the specified connection
	 * otherwise return <code>null</code> indicating that the connection modification
	 * request is invalid.
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		EditPart part = request.getConnectionEditPart();
		if (!(part instanceof GenealogyConnectionEditPart))
			return null;
		GenealogyConnectionEditPart connPart = (GenealogyConnectionEditPart) part;
		CreateConnectionCommand connCmd = connPart.recreateCommand();
		if (!connCmd.isValidSource(getModel()))
			return null;
		connCmd.setSource(getModel());
		Command deleteCmd = new DeleteGenealogyConnectionCommand(connPart.getModel());
		Command modifyCmd = deleteCmd.chain(connCmd);
		modifyCmd.setLabel("Modify " + connCmd.getConnectionName());
		return modifyCmd;
	}

	/**
	 * If the connection modification request is valid with the recevier's model
	 * as the target, then return a command to modify the specified connection
	 * otherwise return <code>null</code> indicating that the connection modification
	 * request is invalid.
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		EditPart part = request.getConnectionEditPart();
		if (!(part instanceof GenealogyConnectionEditPart))
			return null;
		GenealogyConnectionEditPart connPart = (GenealogyConnectionEditPart) part;
		CreateConnectionCommand connCmd = connPart.recreateCommand();
		if (!connCmd.isValidTarget(getModel()))
			return null;
		connCmd.setTarget(getModel());
		Command deleteCmd = new DeleteGenealogyConnectionCommand(connPart.getModel());
		Command modifyCmd = deleteCmd.chain(connCmd);
		modifyCmd.setLabel("Modify " + connCmd.getConnectionName());
		return modifyCmd;
	}
}