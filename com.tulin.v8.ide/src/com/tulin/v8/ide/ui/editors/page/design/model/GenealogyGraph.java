package com.tulin.v8.ide.ui.editors.page.design.model;

import java.util.*;

import com.tulin.v8.ide.ui.editors.page.design.model.listener.GenealogyGraphListener;

/**
 * The root model object representing a genealogy graph and directly or indirectly
 * containing all other model objects.
 */
public class GenealogyGraph
	implements NoteContainer
{
	private final List<Note> notes = new ArrayList<Note>();
	private final Collection<GenealogyGraphListener> listeners = new HashSet<GenealogyGraphListener>();
	
	/**
	 * Discard all elements so that new information can be loaded
	 */
	public void clear() {
		notes.clear();
		for (GenealogyGraphListener l : listeners)
			l.graphCleared();
	}

	//============================================================
	// Notes
	
	public List<Note> getNotes() {
		return notes;
	}

	public boolean addNote(Note n) {
		return addNote(notes.size(), n);
	}

	public boolean addNote(int index, Note n) {
		if (n == null || notes.contains(n))
			return false;
		notes.add(index, n);
		for (GenealogyGraphListener l : listeners)
			l.noteAdded(index, n);
		return true;
	}

	public boolean removeNote(Note n) {
		if (n == null || !notes.remove(n))
			return false;
		for (GenealogyGraphListener l : listeners)
			l.noteRemoved(n);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addGenealogyGraphListener(GenealogyGraphListener l) {
		listeners.add(l);
	}
	
	public void removeGenealogyGraphListener(GenealogyGraphListener l) {
		listeners.remove(l);
	}
}
