package com.tulin.v8.flowdesigner.ui.editors.process.listener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class LEndAdpater extends SelectionAdapter {
	FlowDesignEditor processeditor;

	public LEndAdpater(FlowDesignEditor processeditor) {
		this.processeditor = processeditor;
	}

	public void widgetSelected(SelectionEvent paramSelectionEvent) {
		this.processeditor.callJsFunction("MenuAction.end();");
	}
}
