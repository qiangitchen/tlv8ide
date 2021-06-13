package com.tulin.v8.ide.ui.editors.process.listener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.tulin.v8.ide.ui.editors.process.FlowDesignEditor;

public class LSWLeftAdpater extends SelectionAdapter {
	FlowDesignEditor processeditor;

	public LSWLeftAdpater(FlowDesignEditor processeditor) {
		this.processeditor = processeditor;
	}

	public void widgetSelected(SelectionEvent paramSelectionEvent) {
		this.processeditor.callJsFunction("MenuAction.setAdposation('left');");
	}
}
