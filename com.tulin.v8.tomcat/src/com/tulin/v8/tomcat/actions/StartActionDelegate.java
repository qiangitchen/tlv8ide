package com.tulin.v8.tomcat.actions;

/*
 * (c) Copyright Sysdeo SA 2001, 2002.
 * All Rights Reserved.
 */

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.tulin.v8.tomcat.TomcatLauncherPlugin;

public class StartActionDelegate implements IWorkbenchWindowActionDelegate {
	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	/*
	 * @see IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
	}

	/*
	 * @see IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	/*
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (TomcatLauncherPlugin.checkTomcatSettingsAndWarn()) {
			try {
				TomcatLauncherPlugin.getDefault().getTomcatBootstrap().start();
			} catch (Exception ex) {
				String msg = TomcatLauncherPlugin
						.getResourceString("msg.start.failed");
				MessageDialog.openWarning(TomcatLauncherPlugin.getShell(),
						"Tomcat", msg);
				TomcatLauncherPlugin.log(msg + "/n");
				TomcatLauncherPlugin.logException(ex);
			}
		}
	}

	/*
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}

}
