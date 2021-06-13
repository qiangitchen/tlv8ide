package com.tulin.v8.ide;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

class b implements IRunnableWithProgress {
	private boolean a;

	b(boolean paramBoolean) {
		a = paramBoolean;
	}

	public void run(IProgressMonitor paramIProgressMonitor) {
		paramIProgressMonitor = new NullProgressMonitor();
		StudioJsLibraryInit.a(paramIProgressMonitor, this.a);
		paramIProgressMonitor.done();
	}
}
