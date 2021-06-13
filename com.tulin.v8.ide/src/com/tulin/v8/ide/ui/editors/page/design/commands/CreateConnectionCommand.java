package com.tulin.v8.ide.ui.editors.page.design.commands;

import org.eclipse.gef.commands.Command;

/**
 * Interface for determining whether a connection command will accept a given target and
 * setting that target.
 */
public abstract class CreateConnectionCommand extends Command
{
	public CreateConnectionCommand() {
		setLabel("Create " + getConnectionName());
	}

	public abstract String getConnectionName();
	
	/**
	 * Determine if the specified model object is an appropriate source for the connection.
	 * This method is appropriate only when modifying existing connections.
	 * 
	 * @param source the source
	 * @return <code>true</code> if the source is valid for this command
	 */
	public abstract boolean isValidSource(Object source);

	/**
	 * Set the source for the connection to be created.
	 * 
	 * @param source the source
	 */
	public abstract void setSource(Object source);

	/**
	 * Determine if the specified model object is an appropriate target for the connection
	 * to be created
	 * 
	 * @param target the target
	 * @return <code>true</code> if the target is valid for this command
	 */
	public abstract boolean isValidTarget(Object target);

	/**
	 * Set the target for the connection to be created.
	 * 
	 * @param target the target
	 */
	public abstract void setTarget(Object target);
}
