package com.tulin.v8.editors.page;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Z implements Runnable {

	public Z() {
	}

	public void run() {
		IWorkbenchWindow localIWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage localIWorkbenchPage = localIWorkbenchWindow.getActivePage();
		IViewPart localIViewPart = localIWorkbenchPage.findView("org.eclipse.ui.views.ContentOutline");
		if (localIViewPart != null) {
			localIWorkbenchPage.activate(localIViewPart);
		}
	}
}