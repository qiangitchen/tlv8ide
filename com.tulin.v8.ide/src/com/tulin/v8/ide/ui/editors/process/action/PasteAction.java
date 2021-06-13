package com.tulin.v8.ide.ui.editors.process.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.json.JSONObject;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.Messages;
import com.tulin.v8.ide.ui.editors.process.FlowDesignEditor;

public class PasteAction extends Action {
	Clipboard clipbd;
	FlowDesignEditor editor;

	public PasteAction(Clipboard clipbd, FlowDesignEditor editor) {
		super();
		this.clipbd = clipbd;
		this.editor = editor;
		this.setText(Messages.getString("design.action.paste"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("PasteHS.png")));
		this.setEnabled(false);
	}

	public void run() {
		try {
			Transferable clipT = clipbd.getContents(null);
			if (clipT != null) {
				if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String context = (String) clipT.getTransferData(DataFlavor.stringFlavor);
					JSONObject json = new JSONObject(context);
					// TreeItem item = JSONToItem.parseItem(tree, json);
					// editor.setElementItem(json.getString("id"), item);
					editor.callJsFunction("JavaparseNode('" + json.toString() + "')");
					if (!json.getString("id").startsWith("CopyOf")) {
						clipbd.setContents(new StringSelection(""), null);
					}
				}
			}
		} catch (Exception e) {
			Status status = new Status(IStatus.ERROR, StudioPlugin.getPluginId(), e.toString());
			ErrorDialog.openError(StudioPlugin.getShell(), Messages.getString("design.action.pasteerrtitle"),
					Messages.getString("design.action.pasteerrmsg"), status);
		}
	}
}
