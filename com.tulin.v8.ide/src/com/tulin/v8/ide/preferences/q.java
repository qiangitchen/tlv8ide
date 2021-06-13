package com.tulin.v8.ide.preferences;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class q implements Listener {
	private JSLibraryPreferencePage paramJSLibraryPreferencePage;

	q(JSLibraryPreferencePage paramJSLibraryPreferencePage) {
		this.paramJSLibraryPreferencePage = paramJSLibraryPreferencePage;
	}

	public void handleEvent(Event paramEvent) {
		paramJSLibraryPreferencePage.d();
	}
}
