package com.tulin.v8.editors.page.design.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jsoup.nodes.Element;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.editors.page.PageEditorInterface;

public class CatAction extends Action {
	Tree tree;
	PageEditorInterface editorpart;
	Clipboard clipbd;
	StructuredTextEditor editor;

	public CatAction(Tree tree, PageEditorInterface editorpart, Clipboard clipbd, StructuredTextEditor editor) {
		super();
		this.tree = tree;
		this.editorpart = editorpart;
		this.clipbd = clipbd;
		this.editor = editor;
		this.setText(Messages.getString("design.action.cat"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("edit/CutHS.png")));
	}

	public void run() {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0) {
			TreeItem selectItem = selection[0];
			Element element = (Element) selectItem.getData();
			String select = element.outerHtml();
			StringSelection clipString = new StringSelection(select);
			clipbd.setContents(clipString, null);
			// 将内容加入剪切板后删除节点
			element.remove();// 删除Element
			editorpart.setSourcePageText(editorpart.getDesignEditor().getPageDom().html());
			selectItem.dispose();// 销毁节点
		}
	}
}
