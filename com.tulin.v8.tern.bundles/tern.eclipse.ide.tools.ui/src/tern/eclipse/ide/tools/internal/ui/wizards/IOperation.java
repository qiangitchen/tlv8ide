package tern.eclipse.ide.tools.internal.ui.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IOperation<T> {

	void init();

	void run(IProgressMonitor monitor, T model) throws CoreException;
	
	int getTotal();
	
}
