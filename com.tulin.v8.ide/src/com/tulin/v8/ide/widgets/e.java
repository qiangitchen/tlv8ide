package com.tulin.v8.ide.widgets;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableItem;

class e extends SelectionAdapter {
	private JSLibraryConfigComposite a;

	e(JSLibraryConfigComposite paramJSLibraryConfigComposite) {
		this.a = paramJSLibraryConfigComposite;
	}

	public void widgetSelected(SelectionEvent paramSelectionEvent) {
		int i = this.a.tabFolder.getSelectionIndex();
		TableItem[] arrayOfTableItem;
		if (i == 0) {
			arrayOfTableItem = this.a.dsTable.getSelection();
			if (arrayOfTableItem.length > 0)
				this.a.deleteBtn.setEnabled(true);
			else
				this.a.deleteBtn.setEnabled(false);
		} else {
			arrayOfTableItem = this.a.mdsTable.getSelection();
			if (arrayOfTableItem.length > 0)
				this.a.deleteBtn.setEnabled(true);
			else
				this.a.deleteBtn.setEnabled(false);
		}
	}
}
