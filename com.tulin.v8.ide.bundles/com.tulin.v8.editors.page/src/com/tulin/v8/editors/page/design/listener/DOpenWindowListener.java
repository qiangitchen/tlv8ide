package com.tulin.v8.editors.page.design.listener;

import com.tulin.v8.editors.page.PageEditorInterface;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.swt.chromium.OpenWindowListener;
import com.tulin.v8.swt.chromium.WindowEvent;

public class DOpenWindowListener implements OpenWindowListener {
	PageEditorInterface pageeditor;
	WEBDesignEditorInterface designeditor;

	public DOpenWindowListener(PageEditorInterface pageeditor, WEBDesignEditorInterface designeditor) {
		this.pageeditor = pageeditor;
		this.designeditor = designeditor;
	}

	@Override
	public void open(WindowEvent event) {
		System.out.println("OpenWindowListener");
	}

}
