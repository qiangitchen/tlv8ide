package com.tulin.v8.ide;

import org.eclipse.core.runtime.IPath;

public class XmlController {
	private IPath path;


	private PluginSettingsManager pluginSettingsManager;

	public synchronized PluginSettingsManager getPluginSettingsManager() {
		if (pluginSettingsManager == null) {
			pluginSettingsManager = new PluginSettingsManager(path);
		}
		return pluginSettingsManager;
	}

	public void save() {
		if (pluginSettingsManager != null) {
			pluginSettingsManager.save();
			pluginSettingsManager = null;
		}
	}

	public XmlController(IPath path) {
		this.path = path;
	}
}
