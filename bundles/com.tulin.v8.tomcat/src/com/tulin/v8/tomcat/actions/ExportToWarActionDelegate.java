package com.tulin.v8.tomcat.actions;

import com.tulin.v8.tomcat.TomcatLauncherPlugin;
import com.tulin.v8.tomcat.TomcatProject;

/*
 * (c) Copyright Sysdeo SA 2001, 2002.
 * All Rights Reserved.
 */

public class ExportToWarActionDelegate extends TomcatProjectAbstractActionDelegate {

	public void doActionOn(TomcatProject prj) throws Exception {
		if (!prj.getWarLocation().equals("")) {
			prj.exportToWar();
		} else {
			throw new TomcatActionException(TomcatLauncherPlugin.getResourceString("msg.action.exportWAR.failed"));
		}
	}
}
