package com.tulin.v8.ide.ui.editors.page.design.commands;

import org.eclipse.gef.commands.Command;

import com.tulin.v8.ide.ui.editors.page.design.model.connection.GenealogyConnection;

/**
 * Command to delete a connection from the genealogy graph
 */
@SuppressWarnings("unused")
public class DeleteGenealogyConnectionCommand extends Command
{
	private final GenealogyConnection conn;
	private int connType;

	public DeleteGenealogyConnectionCommand(GenealogyConnection conn) {
		super("Delete Connection");
		this.conn = conn;
	}

	/**
	 * Delete the connection from the genealogy graph
	 */
	public void execute() {
		connType = 0;
	}
	
	/**
	 * Restore the connection in the genealogy graph
	 */
	public void undo() {
		switch (connType) {
			case 1 :
				break;
			case 2 :
				break;
			default :
				break;
		}
	}
}
