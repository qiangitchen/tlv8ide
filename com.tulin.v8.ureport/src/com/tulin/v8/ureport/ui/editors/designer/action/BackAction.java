package com.tulin.v8.ureport.ui.editors.designer.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.browser.Browser;

import com.tulin.v8.ureport.Activator;
import com.tulin.v8.ureport.ui.Messages;

public class BackAction extends Action {
	Browser browser;

	public BackAction(Browser browser) {
		super();
		this.browser = browser;
		this.setText(Messages.getString("design.action.back"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(Activator.getIcon("browser/back.gif")));
		this.setEnabled(false);
	}

	public void run() {
		browser.back();
	}
}
