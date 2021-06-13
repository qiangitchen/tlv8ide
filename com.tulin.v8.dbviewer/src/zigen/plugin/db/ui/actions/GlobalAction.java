/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.views.ISQLOperationTarget;

public class GlobalAction extends Action {

	private int operation;

	private TextViewer viewer;

	public GlobalAction(TextViewer viewer, int operation) {
		this.operation = operation;
		this.viewer = viewer;
		this.setImage(operation);
	}

	public void run() {
		if (viewer.canDoOperation(operation)) {
			viewer.doOperation(operation);
		} else {
			throw new IllegalAccessError("can't run Action.");//$NON-NLS-1$
		}
	}

	public void setImage(int operation) {
		String imageName = null;
		switch (operation) {
		case ITextOperationTarget.UNDO:
			imageName = ISharedImages.IMG_TOOL_UNDO;
			setText(Messages.getString("GlobalAction.1")); //$NON-NLS-1$
			setActionDefinitionId(IWorkbenchActionDefinitionIds.UNDO);
			// setAccelerator(SWT.CTRL | 'Z');
			break;
		case ITextOperationTarget.REDO:
			imageName = ISharedImages.IMG_TOOL_REDO;
			setText(Messages.getString("GlobalAction.2")); //$NON-NLS-1$
			setActionDefinitionId(IWorkbenchActionDefinitionIds.REDO);
			// setAccelerator(SWT.CTRL | 'Y');
			break;
		case ITextOperationTarget.CUT:
			imageName = ISharedImages.IMG_TOOL_CUT;
			setText(Messages.getString("GlobalAction.3")); //$NON-NLS-1$
			setActionDefinitionId(IWorkbenchActionDefinitionIds.CUT);
			// setAccelerator(SWT.CTRL | 'X');
			break;
		case ITextOperationTarget.COPY:
			imageName = ISharedImages.IMG_TOOL_COPY;
			setText(Messages.getString("GlobalAction.4")); //$NON-NLS-1$
			setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY);
			// setAccelerator(SWT.CTRL | 'C');
			break;
		case ITextOperationTarget.PASTE:
			imageName = ISharedImages.IMG_TOOL_PASTE;
			setText(Messages.getString("GlobalAction.5")); //$NON-NLS-1$
			setActionDefinitionId(IWorkbenchActionDefinitionIds.PASTE);
			// setAccelerator(SWT.CTRL | 'V');
			break;
		case ITextOperationTarget.DELETE:
			imageName = ISharedImages.IMG_TOOL_DELETE;
			setText(Messages.getString("GlobalAction.6")); //$NON-NLS-1$
			setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);
			break;
		case ITextOperationTarget.SELECT_ALL:
			setText(Messages.getString("GlobalAction.7")); //$NON-NLS-1$
			setActionDefinitionId(IWorkbenchActionDefinitionIds.SELECT_ALL);
			// setAccelerator(SWT.CTRL | 'A');
			imageName = null;
			break;

		case ISQLOperationTarget.ALL_EXECUTE:
			setText(Messages.getString("GlobalAction.8")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_EXECUTE));
			this.setActionDefinitionId("zigen.plugin.SQLExecuteActionCommand"); //$NON-NLS-1$
			imageName = null;
			return;
		case ISQLOperationTarget.CURRENT_EXECUTE:
			setText(Messages.getString("GlobalAction.10")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_EXECUTE));
			this.setActionDefinitionId("zigen.plugin.SQLCurrentExecuteActionCommand"); //$NON-NLS-1$
			imageName = null;
			return;
		case ISQLOperationTarget.SELECTED_EXECUTE:
			setText(Messages.getString("GlobalAction.12")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_EXECUTE));
			this.setActionDefinitionId("zigen.plugin.SQLSelectedExecuteActionCommand"); //$NON-NLS-1$
			imageName = null;
			return;

		case ISQLOperationTarget.FORMAT:
			setText(Messages.getString("GlobalAction.14")); //$NON-NLS-1$
			setAccelerator(SWT.CTRL | SWT.SHIFT | 'F');
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_FORMAT));
			imageName = null;
			// break;
			return;
		case ISQLOperationTarget.UNFORMAT:
			setText(Messages.getString("GlobalAction.15")); //$NON-NLS-1$
			setAccelerator(SWT.CTRL | SWT.SHIFT | 'U');
			// this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_FORMAT));
			imageName = null;
			// break;
			return;
		case ISQLOperationTarget.LINE_DEL:
			setText(Messages.getString("GlobalAction.16")); //$NON-NLS-1$
			setAccelerator(SWT.CTRL | 'D');
			return;

		case ISQLOperationTarget.COMMENT:
			setText(Messages.getString("GlobalAction.17")); //$NON-NLS-1$
			setAccelerator(SWT.CTRL | '/');
			return;

		case ISQLOperationTarget.NEXT_SQL:
			setText(Messages.getString("GlobalAction.18")); //$NON-NLS-1$
			setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_FORWARD));
			setActionDefinitionId("zigen.plugin.SQLNextCommand"); //$NON-NLS-1$
			return;

		case ISQLOperationTarget.BACK_SQL:
			setText(Messages.getString("GlobalAction.20")); //$NON-NLS-1$
			setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_BACK));
			setActionDefinitionId("zigen.plugin.SQLBackCommand"); //$NON-NLS-1$
			return;

		case ISQLOperationTarget.ALL_CLEAR:
			setText(Messages.getString("GlobalAction.22")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_CLEAR));
			return;

		case ISQLOperationTarget.COMMIT:
			setText(Messages.getString("GlobalAction.23")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_COMMIT));
			return;

		case ISQLOperationTarget.ROLLBACK:
			setText(Messages.getString("GlobalAction.24")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_ROLLBACK));
			return;


		case ISQLOperationTarget.SCRIPT_EXECUTE:
			setText(Messages.getString("GlobalAction.25")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_SCRIPT));
			return;

		default:
			break;
		}
		if (imageName != null) {
			setImageDescriptor(imageName);
		}

	}

	private void setImageDescriptor(String imageName) {
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(imageName));
	}

	public void setTextViewer(TextViewer viewer) {
		this.viewer = viewer;
	}

}
