package com.tulin.v8.ide.ui.editors.process.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONObject;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.Messages;
import com.tulin.v8.ide.ui.editors.process.FlowDesignEditor;

public class CatAction extends Action {
	Clipboard clipbd;
	FlowDesignEditor editor;

	public CatAction(Clipboard clipbd, FlowDesignEditor editor) {
		super();
		this.clipbd = clipbd;
		this.editor = editor;
		this.setText(Messages.getString("design.action.cat"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("CutHS.png")));
	}

	public void run() {
		TreeItem[] selection = editor.tree.getSelection();
		if (selection.length > 0) {
			TreeItem selectItem = selection[0];
			JSONObject json = (JSONObject) selectItem.getData();
			String select = json.toString();
			StringSelection clipString = new StringSelection(select);
			clipbd.setContents(clipString, null);

			// 将内容加入剪切板后删除节点
			editor.deleteAction.run();
		}
		editor.changePasteHandler();
	}
}
