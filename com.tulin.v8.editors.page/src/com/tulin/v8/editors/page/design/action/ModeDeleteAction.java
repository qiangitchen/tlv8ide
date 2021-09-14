package com.tulin.v8.editors.page.design.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.editors.page.PageEditorInterface;

public class ModeDeleteAction extends Action {
	TreeViewer treeViewer;
	StructuredTextEditor editor;
	PageEditorInterface editorpart;

	public ModeDeleteAction(TreeViewer treeViewer, StructuredTextEditor editor, PageEditorInterface editorpart) {
		super();
		this.treeViewer = treeViewer;
		this.editor = editor;
		this.editorpart = editorpart;
		this.setText(Messages.getString("design.action.remove"));
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		this.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		this.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		this.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_DELETE);
		this.setId(IWorkbenchCommandConstants.EDIT_DELETE);
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("delete.gif")));
	}

	public void run() {
		TreeItem[] selection = treeViewer.getTree().getSelection();
		if (selection.length > 0) {
//			TreeItem selectItem = selection[0];
//			Object object = selectItem.getData();
//			if (object instanceof ElementStyleImpl) {
//				ElementStyleImpl element = (ElementStyleImpl) object;
//				element.getParentNode().removeChild(element);
//				selectItem.dispose();// 销毁节点
//			}
		}
	}
}
