package com.tulin.v8.tomcat;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Tomcat10Bootstrap extends Tomcat6Bootstrap {

	public String[] getClasspath() {
		ArrayList classpath = new ArrayList();
		classpath.add(getTomcatDir() + File.separator + "bin" + File.separator
				+ "bootstrap.jar");
		classpath.add(getTomcatDir() + File.separator + "bin" + File.separator
				+ "tomcat-juli.jar");
		String toolsJarLocation = VMLauncherUtility.getVMInstall()
				.getInstallLocation()
				+ File.separator
				+ "lib"
				+ File.separator
				+ "tools.jar";
		if (new File(toolsJarLocation).exists()) {
			classpath.add(toolsJarLocation);
		}
		return ((String[]) classpath.toArray(new String[0]));
	}

	public String getLabel() {
		return "Tomcat 10.x";
	}
}
