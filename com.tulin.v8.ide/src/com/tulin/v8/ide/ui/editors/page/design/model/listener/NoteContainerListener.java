package com.tulin.v8.ide.ui.editors.page.design.model.listener;

import com.tulin.v8.ide.ui.editors.page.design.model.Note;

/**
 * Used by {@link NoteContainer} to notify others when changes occur.
 */
public interface NoteContainerListener
{
	void noteAdded(int index, Note n);
	void noteRemoved(Note n);
}
