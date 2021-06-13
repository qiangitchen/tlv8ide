package com.tulin.v8.ide.ui.editors.page;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import com.tulin.v8.ide.StudioPlugin;

public class Z implements Runnable {

	public Z() {
	}

	public void run() {
		IWorkbenchWindow localIWorkbenchWindow = StudioPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage localIWorkbenchPage = localIWorkbenchWindow.getActivePage();
		IViewPart localIViewPart = localIWorkbenchPage.findView("org.eclipse.ui.views.ContentOutline");
		if (localIViewPart != null) {
			localIWorkbenchPage.activate(localIViewPart);
		}
	}
}