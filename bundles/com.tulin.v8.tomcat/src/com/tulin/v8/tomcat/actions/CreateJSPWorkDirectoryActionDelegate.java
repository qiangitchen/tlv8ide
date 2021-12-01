package com.tulin.v8.tomcat.actions;

/*
 * (c) Copyright Sysdeo SA 2001, 2002.
 * All Rights Reserved.
 */

import org.eclipse.core.runtime.CoreException;

import com.tulin.v8.tomcat.TomcatLauncherPlugin;
import com.tulin.v8.tomcat.TomcatProject;

public class CreateJSPWorkDirectoryActionDelegate extends TomcatProjectAbstractActionDelegate {

	public void doActionOn(TomcatProject prj) throws Exception {
		prj.createWorkFolder();
		try {
			prj.setWorkAsSourceFolder();
		} catch (CoreException ex) {
			// exception if work already set as source folder
		}

		if (prj.getUpdateXml()) {
			prj.updateContext();
		} else {
			throw new TomcatActionException(TomcatLauncherPlugin.getResourceString("msg.action.updateServerXML.failed"));
		}
	}
}
