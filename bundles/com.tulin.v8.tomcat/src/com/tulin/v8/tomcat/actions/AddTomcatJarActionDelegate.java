package com.tulin.v8.tomcat.actions;

import com.tulin.v8.tomcat.TomcatProject;

/*
 * (c) Copyright Sysdeo SA 2001, 2002.
 * All Rights Reserved.
 */

public class AddTomcatJarActionDelegate extends
		TomcatProjectAbstractActionDelegate {

	public void doActionOn(TomcatProject prj) throws Exception {
		prj.addTomcatJarToProjectClasspath();
	}

}
