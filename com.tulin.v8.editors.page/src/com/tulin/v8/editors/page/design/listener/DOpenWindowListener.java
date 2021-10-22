package com.tulin.v8.editors.page.design.listener;

import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.WindowEvent;

import com.tulin.v8.editors.page.PageEditorInterface;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;

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
