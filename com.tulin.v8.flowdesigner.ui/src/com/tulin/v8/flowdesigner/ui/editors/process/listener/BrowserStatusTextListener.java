package com.tulin.v8.flowdesigner.ui.editors.process.listener;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class BrowserStatusTextListener implements StatusTextListener {
	FlowDesignEditor editor;
	Browser browser;

	public BrowserStatusTextListener(FlowDesignEditor editor, Browser browser) {
		this.editor = editor;
		this.browser = browser;
	}

	@Override
	public void changed(StatusTextEvent event) {
		System.out.println(event);
	}

}
