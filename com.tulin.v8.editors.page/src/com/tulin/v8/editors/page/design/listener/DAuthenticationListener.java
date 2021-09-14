package com.tulin.v8.editors.page.design.listener;

import org.eclipse.swt.browser.AuthenticationEvent;
import org.eclipse.swt.browser.AuthenticationListener;

import com.tulin.v8.core.Sys;
import com.tulin.v8.editors.page.PageEditorInterface;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;

public class DAuthenticationListener implements AuthenticationListener {
	PageEditorInterface pageeditor;
	WEBDesignEditorInterface designeditor;

	public DAuthenticationListener(PageEditorInterface pageeditor, WEBDesignEditorInterface designeditor) {
		this.pageeditor = pageeditor;
		this.designeditor = designeditor;
	}

	@Override
	public void authenticate(AuthenticationEvent event) {
		Sys.printMsg("AuthenticationEvent");
	}

}
