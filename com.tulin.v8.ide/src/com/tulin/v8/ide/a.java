package com.tulin.v8.ide;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

class a extends WorkspaceModifyOperation {
	private File[] jdField_a_of_type_ArrayOfJavaIoFile;

	a(ProjectManager paramProjectManager, File[] paramArrayOfFile) {
		this.jdField_a_of_type_ArrayOfJavaIoFile = paramArrayOfFile;
	}

	protected void execute(IProgressMonitor paramIProgressMonitor) {
		try {
			paramIProgressMonitor.beginTask("",
					this.jdField_a_of_type_ArrayOfJavaIoFile.length);
			if (paramIProgressMonitor.isCanceled())
				throw new OperationCanceledException();
		} finally {
			paramIProgressMonitor.done();
		}
	}
}