package com.tulin.v8.echarts.ui.editors.echt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.browser.Browser;

import com.tulin.v8.echarts.ui.Activator;
import com.tulin.v8.echarts.ui.editors.echt.Messages;

public class RefreshAction extends Action {
	Browser browser;

	public RefreshAction(Browser browser) {
		this.browser = browser;
		this.setText(Messages.getString("design.action.refresh"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(Activator.getIcon("refresh.gif")));
	}

	public void run() {
		browser.refresh();
	}
}
