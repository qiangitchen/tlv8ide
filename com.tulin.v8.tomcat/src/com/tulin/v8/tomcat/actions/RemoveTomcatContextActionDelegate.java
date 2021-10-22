package com.tulin.v8.tomcat.actions;

import com.tulin.v8.tomcat.TomcatLauncherPlugin;
import com.tulin.v8.tomcat.TomcatProject;

/*
 * (c) Copyright Sysdeo SA 2001, 2002.
 * All Rights Reserved.
 */

public class RemoveTomcatContextActionDelegate extends TomcatProjectAbstractActionDelegate {

	public void doActionOn(TomcatProject prj) throws Exception {
		if (prj.getUpdateXml()) {
			prj.removeContext();
		} else {
			throw new TomcatActionException(TomcatLauncherPlugin.getResourceString("msg.action.updateServerXML.failed"));
		}
	}
}
