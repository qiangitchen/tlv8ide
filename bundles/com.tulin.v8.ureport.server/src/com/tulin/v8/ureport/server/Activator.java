package com.tulin.v8.ureport.server;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "com.tulin.v8.ureport.server";
	private static Activator plugin;
	public static ApplicationContext applicationContext;

	public Activator() {
		try {
			applicationContext = new ClassPathXmlApplicationContext("ureport-console-context.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}
	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static Activator getDefault() {
		return plugin;
	}

}
