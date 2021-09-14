package com.tulin.v8.editors.page.design.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jsoup.nodes.Element;

import com.tulin.v8.core.TuLinPlugin;

public class CopyAction extends Action {
	Tree tree;
	Clipboard clipbd;

	public CopyAction(Tree tree, Clipboard clipbd) {
		super();
		this.tree = tree;
		this.clipbd = clipbd;
		this.setText(Messages.getString("design.action.copy"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("edit/CopyHS.png")));
	}

	public void run() {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0) {
			TreeItem selectItem = selection[0];
			Element element = (Element) selectItem.getData();
			String select = element.outerHtml();
			StringSelection clipString = new StringSelection(select);
			clipbd.setContents(clipString, null);
		}
	}
}
