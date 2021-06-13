package com.tulin.v8.ide.ui.editors.page.design.model.listener;

/**
 * Used by {@link Note} to notify others when changes occur.
 */
public interface NoteListener
	extends GenealogyElementListener
{
	void textChanged(String text);
}
