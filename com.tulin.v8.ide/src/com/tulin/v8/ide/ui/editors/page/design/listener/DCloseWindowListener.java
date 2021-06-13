package com.tulin.v8.ide.ui.editors.page.design.listener;

import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.WindowEvent;

import com.tulin.v8.ide.ui.editors.page.PageEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;

public class DCloseWindowListener implements CloseWindowListener {
	PageEditorInterface pageeditor;
	WEBDesignEditorInterface designeditor;

	public DCloseWindowListener(PageEditorInterface pageeditor,
			WEBDesignEditorInterface designeditor) {
		this.pageeditor = pageeditor;
		this.designeditor = designeditor;
	}

	@Override
	public void close(WindowEvent event) {
		//System.out.println("CloseWindowListener");
	}

}
