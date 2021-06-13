package com.tulin.v8.ide.ui.editors.process;

import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class AskUserApprover implements IOperationApprover {
	private IUndoContext undoContext;

	public AskUserApprover(IUndoContext context) {
		super();
		this.undoContext = context;
	}

	public IStatus proceedRedoing(IUndoableOperation operation, IOperationHistory history, IAdaptable info) {
		// 不需要询问
		// if (!operation.hasContext(undoContext))
		// return Status.CANCEL_STATUS;
		// boolean isOK =
		// MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		// "Confirm", "Proceed Redo?");
		// if (isOK)
		// return Status.OK_STATUS;
		// else
		// return Status.CANCEL_STATUS;
		return Status.OK_STATUS;
	}

	public IStatus proceedUndoing(IUndoableOperation operation, IOperationHistory history, IAdaptable info) {
		return Status.OK_STATUS;
	}
}
