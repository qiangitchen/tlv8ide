package com.tulin.v8.editors.page.design.listener;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;

import com.tulin.v8.editors.page.PageEditorInterface;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;

public class DProgressListener implements ProgressListener {

	PageEditorInterface pageeditor;
	WEBDesignEditorInterface designeditor;

	public DProgressListener(PageEditorInterface pageeditor, WEBDesignEditorInterface designeditor) {
		this.pageeditor = pageeditor;
		this.designeditor = designeditor;
	}

	@Override
	public void changed(ProgressEvent event) {
		// System.out.println("ProgressListener");

	}

	@Override
	public void completed(ProgressEvent event) {
		try {
			Browser browser = designeditor.getBrowser();
			if (browser != null) {
				String text = "window.document.onclick = function(event){var obj = event.target||event.srcElement; window.document.title = \"{'boj':'\"+obj+\"','id':'\"+obj.id+\"'}\";};";
				browser.execute(text);
				browser.execute("$(document.body).attr('contenteditable',true);");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
