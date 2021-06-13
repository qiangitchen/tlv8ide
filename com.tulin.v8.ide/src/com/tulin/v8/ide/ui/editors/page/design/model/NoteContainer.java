package com.tulin.v8.ide.ui.editors.page.design.model;

import java.util.List;

/**
 * A model object that can contain notes.
 */
public interface NoteContainer
{
	List<Note> getNotes();
	boolean addNote(Note n);
	boolean addNote(int index, Note n);
	boolean removeNote(Note n);
}
