package com.tulin.v8.editors.page.design.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.editors.page.design.dialog.PageResourse;
import com.tulin.v8.editors.page.design.dialog.ResourseDialog;

public class HeadAddResourseAction extends Action {
	WEBDesignEditorInterface editor;
	TreeViewer treeViewer;

	public HeadAddResourseAction(TreeViewer treeViewer, WEBDesignEditorInterface editor) {
		this.treeViewer = treeViewer;
		this.editor = editor;
		this.setText(Messages.getString("design.action.addsource"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("value.gif")));
		this.setEnabled(false);
	}

	public void run() {
		ISelection selection = treeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object[] Selections = ((IStructuredSelection) selection).toArray();
			if (Selections.length > 0) {
//				Object object = Selections[0];
			}
		}
	}

	void addResourse() {
		ResourseDialog dialog = new ResourseDialog(TuLinPlugin.getShell());
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
		if (fielName.toLowerCase().endsWith(".js")) {
//			ElementStyleImpl newnode = new ElementStyleImpl() {
//				@Override
//				public String getTagName() {
//					return "script";
//				}
//			};
//			selectItem.appendChild(newnode);
//			newnode.setAttribute("type", "text/javascript");
//			newnode.setAttribute("src", fielName);
		} else {
//			ElementStyleImpl newnode = new ElementStyleImpl() {
//				@Override
//				public String getTagName() {
//					return "link";
//				}
//			};
//			selectItem.appendChild(newnode);
//			newnode.setAttribute("type", "text/css");
//			newnode.setAttribute("href", fielName);
		}
	}
}
