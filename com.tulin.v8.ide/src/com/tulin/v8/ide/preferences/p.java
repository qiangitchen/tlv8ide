package com.tulin.v8.ide.preferences;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class p implements Listener {
	private JSLibraryPreferencePage paramJSLibraryPreferencePage;

	p(JSLibraryPreferencePage paramJSLibraryPreferencePage) {
		this.paramJSLibraryPreferencePage = paramJSLibraryPreferencePage;
	}

	public void handleEvent(Event paramEvent) {
		paramJSLibraryPreferencePage.e();
	}
}
