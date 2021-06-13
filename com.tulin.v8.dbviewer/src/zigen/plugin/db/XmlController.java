/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import org.eclipse.core.runtime.IPath;

import zigen.plugin.db.core.ConditionManager;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.ui.bookmark.BookmarkManager;

public class XmlController {

	private IPath path;

	private SQLHistoryManager historyManager;

	private ConditionManager conditionManager;

	private BookmarkManager bookmarkManager;

	private PluginSettingsManager pluginSettingsManager;

	public synchronized SQLHistoryManager getHistoryManager() {
		if (historyManager == null) {
			historyManager = new SQLHistoryManager(path);
		}
		return historyManager;
	}

	public synchronized ConditionManager getConditionManager() {
		if (conditionManager == null) {
			conditionManager = new ConditionManager(path);
		}
		return conditionManager;
	}

	public synchronized BookmarkManager getBookmarkManager() {
		if (bookmarkManager == null) {
			bookmarkManager = new BookmarkManager(path);
		}
		return bookmarkManager;
	}

	public synchronized PluginSettingsManager getPluginSettingsManager() {
		if (pluginSettingsManager == null) {
			pluginSettingsManager = new PluginSettingsManager(path);
		}
		return pluginSettingsManager;
	}

	public void save() {
		if (conditionManager != null) {
			conditionManager.save();
			conditionManager = null;
		}

		if (bookmarkManager != null) {
			bookmarkManager.save();
			bookmarkManager = null;
		}

		if (historyManager != null) {
			historyManager.save();
			historyManager = null;
		}

		if (pluginSettingsManager != null) {
			pluginSettingsManager.save();
			pluginSettingsManager = null;
		}
	}

	public XmlController(IPath path) {
		this.path = path;
	}

}
