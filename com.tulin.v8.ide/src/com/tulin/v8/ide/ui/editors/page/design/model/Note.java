package com.tulin.v8.ide.ui.editors.page.design.model;

import java.util.*;

import com.tulin.v8.ide.ui.editors.page.design.model.listener.NoteListener;

public final class Note extends GenealogyElement
{
	private String text = "";
	private final Collection<NoteListener> listeners = new HashSet<NoteListener>();
	
	public Note() {
	}

	public Note(String text) {
		setText(text);
	}

	public String getText() {
		return text;
	}
	
	public boolean setText(String newText) {
		if (newText == null)
			newText = "";
		if (text.equals(newText))
			return false;
		text = newText;
		for (NoteListener l : listeners)
			l.textChanged(text);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addNoteListener(NoteListener l) {
		listeners.add(l);
	}
	
	public void removeNoteListener(NoteListener l) {
		listeners.remove(l);
	}

	//============================================================
	// GenealogyElement
	
	protected void fireLocationChanged(int newX, int newY) {
		for (NoteListener l : listeners)
			l.locationChanged(newX, newY);
	}

	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (NoteListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
