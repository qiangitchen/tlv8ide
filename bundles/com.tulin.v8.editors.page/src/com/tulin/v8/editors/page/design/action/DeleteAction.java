package com.tulin.v8.editors.page.design.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jsoup.nodes.Element;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.editors.page.PageEditorInterface;

public class DeleteAction extends Action {
	Tree tree;
	PageEditorInterface editorpart;

	public DeleteAction(Tree tree, PageEditorInterface editorpart) {
		super();
		this.tree = tree;
		this.editorpart = editorpart;
		this.setText(Messages.getString("design.action.remove"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("delete.gif")));
	}

	public void run() {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0) {
			TreeItem selectItem = selection[0];
			Element element = (Element) selectItem.getData();
			element.remove();// 删除Element
			editorpart.setSourcePageText(editorpart.getDesignEditor().getPageDom().html());
			selectItem.dispose();// 销毁节点
		}
	}
}
