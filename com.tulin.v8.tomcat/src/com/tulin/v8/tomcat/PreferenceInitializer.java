package com.tulin.v8.tomcat;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void initializeDefaultPreferences() {
		System.out.println("=============tomcat plugin init=================");
		TomcatConfigInit.config(false);
	}

}
