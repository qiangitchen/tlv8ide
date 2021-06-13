package com.tulin.v8.ide.ui.editors.page.design.model.listener;

/**
 * Interface used by {@link GenealogyElement} to notify others when changes occur.
 */
public interface GenealogyElementListener
{
	void locationChanged(int x, int y);
	void sizeChanged(int width, int height);
}
