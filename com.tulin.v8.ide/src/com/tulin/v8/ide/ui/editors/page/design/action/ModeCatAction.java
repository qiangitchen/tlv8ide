package com.tulin.v8.ide.ui.editors.page.design.action;

import java.awt.datatransfer.Clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import com.tulin.v8.ide.ui.editors.page.PageEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.selection.ModelSelection;

@SuppressWarnings("restriction")
public class ModeCatAction extends Action {
	TreeViewer treeViewer;
	PageEditorInterface editorpart;
	Clipboard clipbd;
	StructuredTextEditor editor;

	public ModeCatAction(TreeViewer treeViewer, PageEditorInterface editorpart, Clipboard clipbd,
			StructuredTextEditor editor) {
		super();
		this.treeViewer = treeViewer;
		this.editorpart = editorpart;
		this.clipbd = clipbd;
		this.editor = editor;
		this.setText(Messages.getString("design.action.cat"));
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		this.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		this.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		this.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_CUT);
		this.setId(IWorkbenchCommandConstants.EDIT_CUT);
	}

	public void run() {
		TreeItem[] selection = treeViewer.getTree().getSelection();
		if (selection.length > 0) {
			TreeItem selectItem = selection[0];
			Object object =  selectItem.getData();
			if (object instanceof ElementStyleImpl) {
				ElementStyleImpl element = (ElementStyleImpl) object;
				ModelSelection clipmodel = new ModelSelection(element);
				clipbd.setContents(clipmodel, null);
				element.getParentNode().removeChild(element);// 将内容加入剪切板后删除节点
			}
		}
	}
}
