package com.tulin.v8.ide.preferences;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

class s implements IRunnableWithProgress {
	JSLibraryPreferencePage paramJSLibraryPreferencePage;

	s(JSLibraryPreferencePage paramJSLibraryPreferencePage) {
		this.paramJSLibraryPreferencePage = paramJSLibraryPreferencePage;
	}

	public void run(IProgressMonitor paramIProgressMonitor) {
		paramIProgressMonitor = new NullProgressMonitor();
		this.paramJSLibraryPreferencePage.a(paramIProgressMonitor);
		paramIProgressMonitor.done();
	}
}
