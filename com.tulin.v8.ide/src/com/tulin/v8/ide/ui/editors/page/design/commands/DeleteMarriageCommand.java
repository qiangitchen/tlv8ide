package com.tulin.v8.ide.ui.editors.page.design.commands;

import org.eclipse.gef.commands.Command;

import com.tulin.v8.ide.ui.editors.page.design.model.GenealogyGraph;
import com.tulin.v8.ide.ui.editors.page.design.model.Marriage;

/**
 * Command to delete a person from the genealogy graph
 */
@SuppressWarnings("unused")
public class DeleteMarriageCommand extends Command {
	private final GenealogyGraph graph;
	private final Marriage marriage;

	public DeleteMarriageCommand(GenealogyGraph graph, Marriage marriage) {
		super("Delete Marriage");
		this.graph = graph;
		this.marriage = marriage;
	}

	/**
	 * Delete the marriage and any connection to and from the marriage from the
	 * genealogy graph
	 */
	public void execute() {
	}

	/**
	 * Restore the deleted marriage and any connections to and from the marriage
	 * to the genealogy graph
	 */
	public void undo() {
	}
}
