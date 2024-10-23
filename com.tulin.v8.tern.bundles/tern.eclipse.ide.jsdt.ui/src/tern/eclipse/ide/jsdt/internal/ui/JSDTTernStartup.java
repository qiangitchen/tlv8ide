package tern.eclipse.ide.jsdt.internal.ui;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class JSDTTernStartup implements IStartup {

	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Java Editor tracker
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					JavaEditorTracker.getInstance();
				}
			}
		});
	}
}
