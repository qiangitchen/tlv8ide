package com.tulin.v8.tomcat;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;

/*
 * @Tomcat初始化配置
 */
public class TomcatConfigInit {

	public static void config(boolean chage) {
		IPreferenceStore perf = TomcatLauncherPlugin.getDefault().getPreferenceStore();
		if (!chage && TomcatLauncherPlugin.TOMCAT_MYSELFSET.equals(perf.getString(TomcatLauncherPlugin.TOMCAT_PREF_SET_KEY))) {
			return;
		}
		perf.setValue(TomcatLauncherPlugin.TOMCAT_PREF_SET_KEY, TomcatLauncherPlugin.TOMCAT_DEFAULTSET);
		perf.setValue(TomcatLauncherPlugin.TOMCAT_PREF_VERSION_KEY, TomcatLauncherPlugin.TOMCAT_VERSION9);
		String tomcatDir = getTomcatDefaultDir();
		perf.setValue(TomcatLauncherPlugin.TOMCAT_PREF_HOME_KEY, tomcatDir);
		perf.setValue(TomcatLauncherPlugin.TOMCAT_PREF_CONFMODE_KEY, TomcatLauncherPlugin.SERVERXML_MODE);
		String dps = CommonUtil.getPathdep();
		perf.setValue(TomcatLauncherPlugin.TOMCAT_PREF_CONFIGFILE_KEY, tomcatDir + dps + "conf" + dps + "server.xml");
	}

	public static String getTomcatDefaultDir() {
		String path = CommonUtil.getStudioPath();
		String par = new File(path).getParent();
		return par + CommonUtil.getPathdep() + "apache-tomcat";
	}
}
