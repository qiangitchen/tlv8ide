package com.tulin.v8.ide.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableItem;

class o extends SelectionAdapter {
	private JSLibraryPreferencePage paramJSLibraryPreferencePage;

	o(JSLibraryPreferencePage paramJSLibraryPreferencePage) {
		this.paramJSLibraryPreferencePage = paramJSLibraryPreferencePage;
	}

	public void widgetSelected(SelectionEvent paramSelectionEvent) {
		TableItem[] arrayOfTableItem = paramJSLibraryPreferencePage.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTable
				.getSelection();
		if (arrayOfTableItem.length > 0)
			paramJSLibraryPreferencePage.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.deleteBtn
					.setEnabled(true);
		else
			paramJSLibraryPreferencePage.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.deleteBtn
					.setEnabled(false);
	}
}
