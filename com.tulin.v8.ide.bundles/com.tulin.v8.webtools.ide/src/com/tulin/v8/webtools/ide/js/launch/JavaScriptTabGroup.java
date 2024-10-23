package com.tulin.v8.webtools.ide.js.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class JavaScriptTabGroup extends AbstractLaunchConfigurationTabGroup {

	public JavaScriptTabGroup() {
		super();
	}

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {

		ILaunchConfigurationTab[] tabs = { new JavaScriptMainTab(), new CommonTab() };

		setTabs(tabs);
	}

}
