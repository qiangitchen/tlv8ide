package com.tulin.v8.editors.page.design.listener;

import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.tulin.v8.editors.page.PageEditorInterface;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;

public class DLocationListener implements LocationListener {

	PageEditorInterface pageeditor;
	WEBDesignEditorInterface designeditor;

	public DLocationListener(PageEditorInterface pageeditor, WEBDesignEditorInterface designeditor) {
		this.pageeditor = pageeditor;
		this.designeditor = designeditor;
	}

	@Override
	public void changing(LocationEvent event) {

	}

	@Override
	public void changed(LocationEvent event) {
		// System.out.println(event.toString());
	}

}
