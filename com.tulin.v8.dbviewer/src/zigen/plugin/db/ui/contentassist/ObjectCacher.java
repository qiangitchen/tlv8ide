/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.preference.IPreferenceStore;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.CodeAssistPreferencePage;

public class ObjectCacher {

	private static final Map map = new HashMap();

	public static synchronized ObjectCacher getInstance(String key) {
		ObjectCacher instance = (ObjectCacher) map.get(key);
		if (instance == null) {
			instance = new ObjectCacher();
			instance.key = key;
			map.put(key, instance);
		}
		return instance;
	}

	private int timeout = 60000;

	private String key = null;

	private Object obj = null;

	private IPreferenceStore preferenceStore;

	private ObjectCacher() {
		this.preferenceStore = DbPlugin.getDefault().getPreferenceStore();
	}

	public void put(Object obj) {
		this.obj = obj;
		int _time = preferenceStore.getInt(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_CACHE_TIME);
		this.timeout = _time * 1000;
		starttimer();
	}

	public Object get() {
		return obj;
	}

	private void starttimer() {
		new Timer(true).schedule(new TimerTask() {

			public void run() {
				map.remove(key);
				key = null;
				obj = null;
			}
		}, timeout);
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
