package com.tulin.v8.flowdesigner.ui.editors.process.listener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class LLineAdpater extends SelectionAdapter {
	FlowDesignEditor processeditor;

	public LLineAdpater(FlowDesignEditor processeditor) {
		this.processeditor = processeditor;
	}

	public void widgetSelected(SelectionEvent paramSelectionEvent) {
		this.processeditor.callJsFunction("MenuAction.line(true);");
	}
}
