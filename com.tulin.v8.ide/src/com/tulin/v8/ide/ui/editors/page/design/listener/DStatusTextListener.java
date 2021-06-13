package com.tulin.v8.ide.ui.editors.page.design.listener;

import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;

import com.tulin.v8.ide.ui.editors.page.PageEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;

public class DStatusTextListener implements StatusTextListener {
	PageEditorInterface pageeditor;
	WEBDesignEditorInterface designeditor;

	public DStatusTextListener(PageEditorInterface pageeditor,
			WEBDesignEditorInterface designeditor) {
		this.pageeditor = pageeditor;
		this.designeditor = designeditor;
	}

	@Override
	public void changed(StatusTextEvent event) {

	}

}
