/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.ui.internal.Folder;

public class CopyNodeNameAction extends Action {

	DDLDiffEditor editor;

	StructuredViewer viewer = null;

	public CopyNodeNameAction() {
		setImage(ITextOperationTarget.COPY);
	}

	public void run() {
		try {
			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();
			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();

				if (obj instanceof Folder) {
					Folder folder = (Folder) obj;
					sb.append("[");
					sb.append(folder.getName());
					sb.append("]");
					sb.append(DbPluginConstant.LINE_SEP);

				} else if (obj instanceof DDLNode) {
					DDLNode node = (DDLNode) obj;
					if (index == 0) {
						sb.append(node.getName());
						sb.append(DbPluginConstant.LINE_SEP);
					} else {
						// sb.append(", " + node.getName());
						sb.append(node.getName());
						sb.append(DbPluginConstant.LINE_SEP);
					}
				}

				index++;
			}

			if (index > 0) {
				clipboard.setContents(new Object[] {sb.toString()}, new Transfer[] {TextTransfer.getInstance()});
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	protected IStructuredSelection selection;

	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (viewer == null) {
			setEnabled(false);
		} else {
			selection = (IStructuredSelection) viewer.getSelection();
			if (selection.size() > 0) {
				setEnabled(true);

			} else {
				setEnabled(false);
			}
		}
	}

	public void selectionChanged(ISelection _selection) {
		if (_selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) _selection;
		} else {
			this.selection = null;
		}
	}

	public void setActiveEditor(DDLDiffEditor target) {
		if (target != null) {
			editor = target;
			viewer = target.getTreeViewer();
		} else {
			editor = null;
			viewer = null;
		}
	}

	public void setImage(int operation) {
		String imageName = null;
		switch (operation) {
		case ITextOperationTarget.COPY:
			imageName = ISharedImages.IMG_TOOL_COPY;
			setText("COPY");
			setAccelerator(SWT.CTRL | 'C');
			break;
		default:
			break;
		}

		if (imageName != null) {
			setImageDescriptor(imageName);
		}

	}

	protected void setImageDescriptor(String imageName) {
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(imageName));
	}

}
