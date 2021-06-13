package com.tulin.v8.ide.ui.editors.page.design.action;

import java.awt.datatransfer.Clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;

import com.tulin.v8.ide.ui.editors.page.design.selection.ModelSelection;

@SuppressWarnings("restriction")
public class ModeCopyAction extends Action {
	TreeViewer treeViewer;
	Clipboard clipbd;

	public ModeCopyAction(TreeViewer treeViewer, Clipboard clipbd) {
		super();
		this.treeViewer = treeViewer;
		this.clipbd = clipbd;
		this.setText(Messages.getString("design.action.copy"));
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		this.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		this.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		this.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);
		this.setId(IWorkbenchCommandConstants.EDIT_COPY);
	}

	public void run() {
		TreeItem[] selection = treeViewer.getTree().getSelection();
		if (selection.length > 0) {
			TreeItem selectItem = selection[0];
			Object object = selectItem.getData();
			if (object instanceof ElementStyleImpl) {
				ElementStyleImpl element = (ElementStyleImpl) object;
				ModelSelection clipmodel = new ModelSelection(element);
				clipbd.setContents(clipmodel, null);
			}
		}
	}
}
