package com.tulin.v8.flowdesigner.ui.editors.process.listener;

import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class BrowserProgressListener implements ProgressListener {

	FlowDesignEditor peditor;

	public BrowserProgressListener(FlowDesignEditor peditor) {
		this.peditor = peditor;
	}

	@Override
	public void changed(ProgressEvent event) {

	}

	@Override
	public void completed(ProgressEvent event) {
		peditor.pageDataInited();
	}

}
