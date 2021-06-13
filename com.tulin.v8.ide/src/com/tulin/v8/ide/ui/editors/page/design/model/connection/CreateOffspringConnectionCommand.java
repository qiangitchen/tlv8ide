package com.tulin.v8.ide.ui.editors.page.design.model.connection;

import com.tulin.v8.ide.ui.editors.page.design.commands.CreateConnectionCommand;
import com.tulin.v8.ide.ui.editors.page.design.model.Marriage;

/**
 * Add a {@link Person} to a {@link Marriage} as an offspring
 */
@SuppressWarnings("unused")
public class CreateOffspringConnectionCommand extends CreateConnectionCommand {
	private Marriage marriage;
	private Marriage oldMarriage;

	public CreateOffspringConnectionCommand(Marriage marriage) {
		this.marriage = marriage;
	}

	public String getConnectionName() {
		return "Offspring Connection";
	}

	/**
	 * Determine if the specified model object is an appropriate source for the
	 * connection. This method is appropriate after {@link #setTarget(Object)}
	 * has been called with a non-<code>null</code> value.
	 * 
	 * @see CreateConnectionCommand#isValidSource(Object)
	 */
	public boolean isValidSource(Object source) {
		if (!(source instanceof Marriage))
			return false;
		return true;
	}

	public void setSource(Object source) {
		marriage = (Marriage) source;
	}

	/**
	 * Determine if the specified object is an appropriate target for the
	 * receiver and that the specified object is not already connected to the
	 * receiver.
	 * 
	 * @see CreateConnectionCommand#isValidTarget(Object)
	 */
	public boolean isValidTarget(Object target) {
		return true;
	}

	/**
	 * Set the target for the connection to be created.
	 * 
	 * @param target
	 *            the target
	 * @throws IllegalArgumentException
	 *             if the target is not valid for the receiver
	 */
	public void setTarget(Object target) {
	}

	/**
	 * Add the person as an offspring of the marriage while caching the original
	 * marriage that had the person as an offspring
	 */
	public void execute() {
	}

	/**
	 * Restore the original marriage having the person as an offspring
	 */
	public void undo() {
	}
}
