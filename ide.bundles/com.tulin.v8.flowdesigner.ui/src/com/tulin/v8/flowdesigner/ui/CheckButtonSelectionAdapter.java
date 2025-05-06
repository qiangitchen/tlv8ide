package com.tulin.v8.flowdesigner.ui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

public abstract class CheckButtonSelectionAdapter extends SelectionAdapter {
	private Button button;

	public CheckButtonSelectionAdapter(Button button) {
		this.button = button;
	}

	public abstract void widgetSelected(SelectionEvent event, Button button);

	public void widgetSelected(SelectionEvent e) {
		widgetSelected(e, button);
	}
}
