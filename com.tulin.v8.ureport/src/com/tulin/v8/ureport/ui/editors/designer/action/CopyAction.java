package com.tulin.v8.ureport.ui.editors.designer.action;

import java.awt.datatransfer.Clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.tulin.v8.ureport.Activator;
import com.tulin.v8.ureport.ui.Messages;

public class CopyAction extends Action {
	Clipboard clipbd;

	public CopyAction(Clipboard clipbd) {
		super();
		this.clipbd = clipbd;
		this.setText(Messages.getString("design.action.copy"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(Activator.getIcon("CopyHS.png")));
		this.setEnabled(false);
	}

	public void run() {

	}
}
