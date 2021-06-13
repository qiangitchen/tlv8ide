package com.tulin.v8.ide.ui.editors.page.design.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jsoup.nodes.Element;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.page.design.ReadHTML;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.dialog.PageResourse;
import com.tulin.v8.ide.ui.editors.page.design.dialog.ResourseDialog;

public class AddResourseAction extends Action {
	Tree tree;
	WEBDesignEditorInterface editor;
	private TreeItem selectItem;

	public AddResourseAction(Tree tree, WEBDesignEditorInterface editor) {
		super();
		this.tree = tree;
		this.editor = editor;
		this.setText(Messages.getString("design.action.addsource"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("value.gif")));
		this.setEnabled(false);
	}

	public void run() {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0) {
			selectItem = selection[0];
			Element element = (Element) selectItem.getData();
			if ("head".equals(element.tagName())) {
				addResourse();
			}
		}
	}

	private void addResourse() {
		ResourseDialog dialog = new ResourseDialog(StudioPlugin.getShell());
		int staue = dialog.open();
		if (staue == IDialogConstants.OK_ID) {
			List<String> result = dialog.selectlist;
			for (int i = 0; i < result.size(); i++) {
				addSourse(result.get(i));
			}
		}
	}

	private void addSourse(String filename) {
		String fielName = PageResourse.transeFile(editor.getHTMLEditor().getFile().toURI().toString(), filename.trim());
		Element element = (Element) selectItem.getData();
		if (fielName.toLowerCase().endsWith(".js")) {
			Element newele = element.appendElement("script");
			newele.attr("type", "text/javascript");
			newele.attr("src", fielName);
			TreeItem newitem = new TreeItem(selectItem, SWT.NONE);
			newitem.setText(ReadHTML.getText(newele));
			newitem.setImage(StudioPlugin.getIcon("brkp_obj.gif"));
			newitem.setData(newele);
		} else {
			Element newele = element.appendElement("link");
			newele.attr("type", "text/css");
			newele.attr("href", fielName);
			TreeItem newitem = new TreeItem(selectItem, SWT.NONE);
			newitem.setText(ReadHTML.getText(newele));
			newitem.setImage(StudioPlugin.getIcon("brkp_obj.gif"));
			newitem.setData(newele);
		}
		editor.getEditorpart().setSourcePageText(editor.getPageDom().html());
	}

}
