package com.tulin.v8.ide.ui.editors.page.design.model.listener;

/**
 * Used by {@link Marriage} to notify others when changes occur.
 */
public interface MarriageListener
	extends GenealogyElementListener
{
	void yearMarriedChanged(int yearMarried);
//	void husbandChanged(Person husband, Person oldHusband);
//	void wifeChanged(Person wife, Person oldWife);
//	void offspringAdded(Person p);
//	void offspringRemoved(Person p);
}
