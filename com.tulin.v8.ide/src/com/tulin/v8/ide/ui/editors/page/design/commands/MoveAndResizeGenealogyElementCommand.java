package com.tulin.v8.ide.ui.editors.page.design.commands;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.commands.Command;

import com.tulin.v8.ide.ui.editors.page.design.model.GenealogyElement;
import com.tulin.v8.ide.ui.editors.page.design.model.Note;

/**
 * Move and resize a {@link GenealogyElement}
 */
public class MoveAndResizeGenealogyElementCommand extends Command
{
	private final GenealogyElement element;
	private final Rectangle box;
	private Rectangle oldBox;

	public MoveAndResizeGenealogyElementCommand(GenealogyElement element, Rectangle box) {
		this.element = element;
		this.box = box;
		setLabel("Modify " + getElementName());
	}
	
	private String getElementName() {
		if (element instanceof Note)
			return "Person";
		return "Element";
	}

	/**
	 * Move and resize the {@link GenealogyElement}
	 */
	public void execute() {
		oldBox = new Rectangle(element.getX(), element.getY(), element.getWidth(), element.getHeight());
		element.setLocation(box.x, box.y);
		element.setSize(box.width, box.height);
	}
	
	/**
	 * Restore the {@link GenealogyElement} to its original location and size
	 */
	public void undo() {
		element.setLocation(oldBox.x, oldBox.y);
		element.setSize(oldBox.width, oldBox.height);
	}
}
