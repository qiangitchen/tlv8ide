package com.tulin.v8.ide.preferences;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class l implements Listener {
	private JSLibraryPreferencePage paramJSLibraryPreferencePage;

	l(JSLibraryPreferencePage paramJSLibraryPreferencePage) {
		this.paramJSLibraryPreferencePage = paramJSLibraryPreferencePage;
	}

	public void handleEvent(Event paramEvent) {
		paramEvent.width = paramJSLibraryPreferencePage.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTable
				.getGridLineWidth();
		paramEvent.height = (int) Math.floor(paramEvent.gc.getFontMetrics()
				.getHeight() * 1.5D);
	}
}