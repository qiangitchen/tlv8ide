package com.tulin.v8.ide.ui.editors.process;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorPart;

public class DrowOperation extends AbstractOperation {
	FlowDesignEditor editor;
	String beforeJsons;
	String afterJsons;

	public DrowOperation(String label, FlowDesignEditor editor, String beforeJsons, String afterJsons) {
		super(label);
		this.editor = editor;
		this.beforeJsons = beforeJsons;
		this.afterJsons = afterJsons;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		editor.pfirePropertyChange(IEditorPart.PROP_DIRTY);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		editor.setSourceText(afterJsons);
		editor.pageDataInited();
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		editor.setSourceText(beforeJsons);
		editor.pageDataInited();
		return execute(monitor, info);
	}

}
