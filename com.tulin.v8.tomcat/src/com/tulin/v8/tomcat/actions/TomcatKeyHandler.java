package com.tulin.v8.tomcat.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;

import com.tulin.v8.tomcat.TomcatLauncherPlugin;
import com.tulin.v8.tomcat.VMLauncherUtility;

public class TomcatKeyHandler extends AbstractHandler {

	private static final String START_STOP_CMD_ID = "pl.szpinda.plugin.tomcat.commands.tomcatStartStop";

	public Object execute(ExecutionEvent arg) throws ExecutionException {
		boolean restart = !arg.getCommand().getId().equals(START_STOP_CMD_ID);
		boolean start = false;
		try {
			if (restart) {
				if (VMLauncherUtility.ILAUNCH == null || VMLauncherUtility.ILAUNCH.isTerminated()) {
					TomcatLauncherPlugin.getDefault().getTomcatBootstrap().start();
				} else {
					VMLauncherUtility.ILAUNCH.terminate();
					TomcatLauncherPlugin.getDefault().getTomcatBootstrap().start();
				}
			} else {
				if (VMLauncherUtility.ILAUNCH == null || VMLauncherUtility.ILAUNCH.isTerminated()) {
					start = true;
					TomcatLauncherPlugin.getDefault().getTomcatBootstrap().start();
				} else {
					VMLauncherUtility.ILAUNCH.terminate();
				}
			}
		} catch (CoreException ex) {
			String msg = null;
			if (restart) {
				msg = TomcatLauncherPlugin.getResourceString("msg.restart.failed");
			} else {
				if (start) {
					msg = TomcatLauncherPlugin.getResourceString("msg.start.failed");
				} else {
					msg = TomcatLauncherPlugin.getResourceString("msg.stop.failed");
				}
			}
			MessageDialog.openWarning(TomcatLauncherPlugin.getShell(), "Tomcat", msg);
			TomcatLauncherPlugin.log(msg + "/n");
			TomcatLauncherPlugin.log(ex);
		}
		return null;
	}

}
