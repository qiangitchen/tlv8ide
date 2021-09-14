package com.tulin.v8.flowdesigner.ui.editors.process.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONException;
import org.json.JSONObject;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.flowdesigner.ui.editors.Messages;
import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class CopyAction extends Action {
	Clipboard clipbd;
	FlowDesignEditor editor;

	public CopyAction(Clipboard clipbd, FlowDesignEditor editor) {
		super();
		this.clipbd = clipbd;
		this.editor = editor;
		this.setText(Messages.getString("design.action.copy"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("edit/CopyHS.png")));
	}

	public void run() {
		TreeItem[] selection = editor.tree.getSelection();
		if (selection.length > 0) {
			TreeItem selectItem = selection[0];
			JSONObject json = (JSONObject) selectItem.getData();
			try {
				json.put("id", "CopyOf" + json.getString("id"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String select = json.toString();
			StringSelection clipString = new StringSelection(select);
			clipbd.setContents(clipString, null);
		}
		editor.changePasteHandler();
	}
}
