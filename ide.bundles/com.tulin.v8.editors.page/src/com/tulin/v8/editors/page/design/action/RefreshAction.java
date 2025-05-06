package com.tulin.v8.editors.page.design.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.swt.chromium.Browser;

/**
 * 刷新
 * 
 * @author chenqian
 *
 */
public class RefreshAction extends Action {
	Browser browser;

	public RefreshAction(Browser browser) {
		this.browser = browser;
		this.setText(Messages.getString("design.action.refresh"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("refresh.gif")));
	}

	public void run() {
		// browser.execute("realodFrame();");
		browser.refresh();
	}
}
