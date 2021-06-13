package com.tulin.v8.ide.ui.editors.page.design.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.page.design.WEBDesignEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.selection.ModelSelection;

@SuppressWarnings("restriction")
public class ModePasteAction extends Action {
	TreeViewer treeViewer;
	Clipboard clipbd;
	WEBDesignEditorInterface editor;

	public ModePasteAction(TreeViewer treeViewer, Clipboard clipbd, WEBDesignEditorInterface editor) {
		super();
		this.treeViewer = treeViewer;
		this.clipbd = clipbd;
		this.editor = editor;
		this.setText(Messages.getString("design.action.paste"));
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		this.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		this.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		this.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_PASTE);
		this.setId(IWorkbenchCommandConstants.EDIT_PASTE);
		this.setEnabled(false);
	}

	public void run() {
		try {
			Transferable clipT = clipbd.getContents(null);
			if (clipT != null) {
				if (clipT.isDataFlavorSupported(ModelSelection.rangeFlavor)) {
					final ElementStyleImpl element = (ElementStyleImpl) clipT
							.getTransferData(ModelSelection.rangeFlavor);
					TreeItem[] selection = treeViewer.getTree().getSelection();
					TreeItem selectItem = selection[0];
					Object object = selectItem.getData();
					if (object instanceof ElementStyleImpl) {
						ElementStyleImpl citem = (ElementStyleImpl)object;
						citem.appendChild(element.cloneNode(true));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Status status = new Status(IStatus.ERROR, StudioPlugin.getPluginId(), e.toString());
			ErrorDialog.openError(StudioPlugin.getShell(), Messages.getString("design.action.pasteerrtitle"),
					Messages.getString("design.action.pasteerrmsg"), status);
		}
	}

	public void updateState() {
		try {
			Transferable clipT = clipbd.getContents(null);
			if (clipT != null) {
				if (clipT.isDataFlavorSupported(ModelSelection.rangeFlavor)) {
					final ElementStyleImpl element = (ElementStyleImpl) clipT
							.getTransferData(ModelSelection.rangeFlavor);
					if (element != null) {
						setEnabled(true);
					} else {
						setEnabled(false);
					}
				}
			}
		} catch (Exception e) {
			setEnabled(false);
		}
	}
}
