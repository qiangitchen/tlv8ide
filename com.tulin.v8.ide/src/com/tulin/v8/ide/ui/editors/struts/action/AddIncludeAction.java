package com.tulin.v8.ide.ui.editors.struts.action;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.struts.Messages;
import com.tulin.v8.ide.ui.editors.struts.StrutsConfigEditor;
import com.tulin.v8.ide.ui.editors.struts.dialog.StrutsResourseDialog;

public class AddIncludeAction extends Action {
	StrutsConfigEditor editor;
	Tree tree;

	public AddIncludeAction(StrutsConfigEditor editor, Tree tree) {
		this.editor = editor;
		this.tree = tree;
		this.setText(Messages.getString("editors.StrutsConfigEditor.addref"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("addbtn.gif")));
	}

	@SuppressWarnings("deprecation")
	public void run() {
		Document doc = editor.getTableDocument();
		Element struts = doc.getRootElement();
		StrutsResourseDialog dialog = new StrutsResourseDialog(StudioPlugin.getShell(), editor.getTextEditor());
		int state = dialog.open();
		if (state == IDialogConstants.OK_ID) {
			List<String> result = dialog.selectlist;
			for (int i = 0; i < result.size(); i++) {
				Element newitem = struts.addElement("include");
				String filepath = result.get(i);
				newitem.setAttributeValue("file", filepath);
				TreeItem nitem = new TreeItem(tree, SWT.NONE);
				nitem.setText(filepath);
				nitem.setImage(StudioPlugin.getIcon("success_ovr.gif"));
				tree.setSelection(nitem);
			}
			editor.setDataText();
		}
	}
}
