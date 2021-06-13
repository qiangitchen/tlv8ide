package com.tulin.v8.ide.preferences;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class r implements Listener {
	private JSLibraryPreferencePage paramJSLibraryPreferencePage;

	r(JSLibraryPreferencePage paramJSLibraryPreferencePage) {
		this.paramJSLibraryPreferencePage = paramJSLibraryPreferencePage;
	}

	public void handleEvent(Event paramEvent) {
		paramJSLibraryPreferencePage.c();
		paramJSLibraryPreferencePage.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.deleteBtn
				.setEnabled(false);
	}
}
