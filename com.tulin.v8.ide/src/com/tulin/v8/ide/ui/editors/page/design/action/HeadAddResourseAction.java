package com.tulin.v8.ide.ui.editors.page.design.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.dialog.PageResourse;
import com.tulin.v8.ide.ui.editors.page.design.dialog.ResourseDialog;

@SuppressWarnings("restriction")
public class HeadAddResourseAction extends Action {
	WEBDesignEditorInterface editor;
	TreeViewer treeViewer;
	ElementStyleImpl selectItem;

	public HeadAddResourseAction(TreeViewer treeViewer, WEBDesignEditorInterface editor) {
		this.treeViewer = treeViewer;
		this.editor = editor;
		this.setText(Messages.getString("design.action.addsource"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("value.gif")));
		this.setEnabled(false);
	}
	
	public void run() {
		ISelection selection = treeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object[] Selections = ((IStructuredSelection) selection).toArray();
			if (Selections.length > 0) {
				Object object = Selections[0];
				if (object instanceof ElementStyleImpl) {
					selectItem = (ElementStyleImpl) object;
					if("head".equals(selectItem.getNodeName().toLowerCase())){
						addResourse();
					}
				}
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
		if (fielName.toLowerCase().endsWith(".js")) {
			ElementStyleImpl newnode = new ElementStyleImpl(){
				@Override
				public String getTagName() {
					return "script";
				}
			};
			selectItem.appendChild(newnode);
			newnode.setAttribute("type", "text/javascript");
			newnode.setAttribute("src", fielName);
		}else {
			ElementStyleImpl newnode = new ElementStyleImpl(){
				@Override
				public String getTagName() {
					return "link";
				}
			};
			selectItem.appendChild(newnode);
			newnode.setAttribute("type", "text/css");
			newnode.setAttribute("href", fielName);
		}
	}
}
