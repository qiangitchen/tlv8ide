package com.tulin.v8.ureport.ui.editors.designer.action;

import java.awt.datatransfer.Clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.tulin.v8.ureport.Activator;
import com.tulin.v8.ureport.ui.Messages;

public class PasteAction extends Action {
	Clipboard clipbd;

	public PasteAction(Clipboard clipbd) {
		super();
		this.clipbd = clipbd;
		this.setText(Messages.getString("design.action.paste"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(Activator.getIcon("PasteHS.png")));
		this.setEnabled(false);
	}

	public void run() {

	}

}
