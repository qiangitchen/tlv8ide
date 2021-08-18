package com.tulin.v8.ureport.ui.editors.designer.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.equo.swt.chromium.Browser;
import com.tulin.v8.ureport.Activator;
import com.tulin.v8.ureport.ui.Messages;

public class ForwardAction extends Action {
	Browser browser;

	public ForwardAction(Browser browser) {
		super();
		this.browser = browser;
		this.setText(Messages.getString("design.action.forward"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(Activator.getIcon("browser/forward.gif")));
		this.setEnabled(false);
	}

	public void run() {
		browser.forward();
	}
}
