package com.tulin.v8.ide.ui.editors.struts.action;

import org.dom4j.Element;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.struts.Messages;
import com.tulin.v8.ide.ui.editors.struts.StrutsConfigEditor;

public class RemoveIncludeAction extends Action {
	StrutsConfigEditor editor;
	Tree tree;

	public RemoveIncludeAction(StrutsConfigEditor editor, Tree tree) {
		this.editor = editor;
		this.tree = tree;
		this.setText(Messages.getString("editors.StrutsConfigEditor.delref"));
		this.setImageDescriptor(ImageDescriptor
				.createFromImage(StudioPlugin.getIcon("delbtn.gif")));
	}

	public void run() {
		TreeItem[] selects = tree.getSelection();
		for (int i = 0; i < selects.length; i++) {
			TreeItem item = selects[i];
			Element element = (Element) item.getData();
			element.getParent().remove(element);
			item.dispose();
		}
		editor.setDataText();
	}
}
