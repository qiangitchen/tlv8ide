package com.tulin.v8.flowdesigner.ui.editors.process.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONObject;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.flowdesigner.ui.editors.Messages;
import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class DeleteAction extends Action {
	FlowDesignEditor editor;

	public DeleteAction(FlowDesignEditor editor) {
		this.editor = editor;
		this.setText(Messages.getString("TLEditor.dataEditor.9"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("delete.gif")));
	}

	@Override
	public void run() {
		try {
			TreeItem[] selectItems = editor.tree.getSelection();
			if (selectItems.length > 0) {
				JSONObject jon = (JSONObject) selectItems[0].getData();
				editor.callJsFunction("removeSinglNodeById('" + jon.getString("id") + "')");
				selectItems[0].dispose();
			} else {
				editor.callJsFunction("MenuAction.remove()");
			}
		} catch (Exception es) {
			es.printStackTrace();
		}
	}
}
