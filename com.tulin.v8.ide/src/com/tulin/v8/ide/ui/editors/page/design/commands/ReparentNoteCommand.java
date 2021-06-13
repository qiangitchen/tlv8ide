package com.tulin.v8.ide.ui.editors.page.design.commands;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.commands.Command;

import com.tulin.v8.ide.ui.editors.page.design.model.Note;
import com.tulin.v8.ide.ui.editors.page.design.model.NoteContainer;

/**
 * Move a note from one {@link NoteContainer} to another.
 */
public class ReparentNoteCommand extends Command
{
	private final NoteContainer container;
	private final Note note;
	private Rectangle box;
	private int index;
	private NoteContainer oldContainer;
	private Rectangle oldBox;
	private int oldIndex;

	/**
	 * Instantiate a new command to move a note.
	 * 
	 * @param container the container to which the note will be moved
	 * @param note the note to be moved
	 */
	public ReparentNoteCommand(NoteContainer container, Note note) {
		super("Move Note");
		this.container = container;
		this.note = note;
	}
	
	/**
	 * Set the note's original container.
	 * Must be called before the command is executed.
	 * 
	 * @param container the original container (not <code>null</code>)
	 */
	public void setOldContainer(NoteContainer container) {
		oldContainer = container;
	}

	/**
	 * Set the location and size of the note when it is reparented.
	 */
	public void setBounds(Rectangle box) {
		this.box = box;
	}

	/**
	 * If the note is to be inserted after a particular note
	 * then call this method to specify the note before,
	 * otherwise the note will be added as the first note in the container.
	 * 
	 * @param afterNote the note after which the moved note will be inserted
	 */
	public void setAfterNote(Note afterNote) {
		index = container.getNotes().indexOf(afterNote) + 1;
	}

	/**
	 * Move the note to the new container,
	 * while caching the old location and size.
	 */
	public void execute() {
		oldBox = new Rectangle(
			note.getX(), note.getY(), note.getWidth(), note.getHeight());
		oldIndex = oldContainer.getNotes().indexOf(note);
		oldContainer.removeNote(note);
		if (box != null) {
			note.setLocation(box.x, box.y);
			note.setSize(box.width, box.height);
		}
		container.addNote(index, note);
	}
	
	/**
	 * Move the note back to the original container
	 * and restore the original location and size.
	 */
	public void undo() {
		container.removeNote(note);
		oldContainer.addNote(oldIndex, note);
		note.setSize(oldBox.width, oldBox.height);
		note.setLocation(oldBox.x, oldBox.y);
	}
}
