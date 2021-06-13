package com.tulin.v8.ide.ui.editors.page.design.model;

import java.util.*;

import com.tulin.v8.ide.ui.editors.page.design.model.listener.MarriageListener;

/**
 * A marriage between a husband and wife that has zero or more offspring.
 */
public final class Marriage extends GenealogyElement {
	private int yearMarried = -1;
	private final Collection<MarriageListener> listeners = new HashSet<MarriageListener>();

	public Marriage() {
	}

	public Marriage(int year) {
		setYearMarried(year);
	}

	public int getYearMarried() {
		return yearMarried;
	}

	public boolean setYearMarried(int newYearMarried) {
		if (yearMarried == newYearMarried)
			return false;
		yearMarried = newYearMarried;
		for (MarriageListener l : listeners)
			l.yearMarriedChanged(yearMarried);
		return true;
	}

	// Listeners

	public void addMarriageListener(MarriageListener l) {
		listeners.add(l);
	}

	public void removeMarriageListener(MarriageListener l) {
		listeners.remove(l);
	}

	// ============================================================
	// GenealogyElement

	protected void fireLocationChanged(int newX, int newY) {
		for (MarriageListener l : listeners)
			l.locationChanged(newX, newY);
	}

	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (MarriageListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
