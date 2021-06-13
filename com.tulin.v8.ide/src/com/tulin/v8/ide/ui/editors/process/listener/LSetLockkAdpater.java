package com.tulin.v8.ide.ui.editors.process.listener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;

import com.tulin.v8.ide.ui.editors.process.FlowDesignEditor;

public class LSetLockkAdpater extends SelectionAdapter {
	FlowDesignEditor processeditor;
	ToolItem toolitem;

	public LSetLockkAdpater(FlowDesignEditor processeditor, ToolItem toolitem) {
		this.processeditor = processeditor;
		this.toolitem = toolitem;
	}

	public void widgetSelected(SelectionEvent paramSelectionEvent) {
		boolean select = toolitem.getSelection();
		this.processeditor
				.callJsFunction("MenuAction.setLock(" + select + ");");
	}
}
