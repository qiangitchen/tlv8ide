
package com.tulin.v8.editors.ini.editors.eclipse;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.jface.preference.IPreferenceStore;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class PreferencesAdapter implements IPreferenceStore {
	private ListenerList fListeners = new ListenerList(1);

	private PropertyChangeListener fListener = new PropertyChangeListener();
	private Preferences fPreferences;
	private boolean fSilent;

	public PreferencesAdapter() {
		this(new Preferences());
	}

	public PreferencesAdapter(Preferences preferences) {
		this.fPreferences = preferences;
	}

	@SuppressWarnings("unchecked")
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (this.fListeners.size() == 0)
			this.fPreferences.addPropertyChangeListener(this.fListener);
		this.fListeners.add(listener);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		this.fListeners.remove(listener);
		if (this.fListeners.size() == 0)
			this.fPreferences.removePropertyChangeListener(this.fListener);
	}

	public boolean contains(String name) {
		return this.fPreferences.contains(name);
	}

	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		if (!this.fSilent) {

		}
	}

	public boolean getBoolean(String name) {
		return this.fPreferences.getBoolean(name);
	}

	public boolean getDefaultBoolean(String name) {
		return this.fPreferences.getDefaultBoolean(name);
	}

	public double getDefaultDouble(String name) {
		return this.fPreferences.getDefaultDouble(name);
	}

	public float getDefaultFloat(String name) {
		return this.fPreferences.getDefaultFloat(name);
	}

	public int getDefaultInt(String name) {
		return this.fPreferences.getDefaultInt(name);
	}

	public long getDefaultLong(String name) {
		return this.fPreferences.getDefaultLong(name);
	}

	public String getDefaultString(String name) {
		return this.fPreferences.getDefaultString(name);
	}

	public double getDouble(String name) {
		return this.fPreferences.getDouble(name);
	}

	public float getFloat(String name) {
		return this.fPreferences.getFloat(name);
	}

	public int getInt(String name) {
		return this.fPreferences.getInt(name);
	}

	public long getLong(String name) {
		return this.fPreferences.getLong(name);
	}

	public String getString(String name) {
		return this.fPreferences.getString(name);
	}

	public boolean isDefault(String name) {
		return this.fPreferences.isDefault(name);
	}

	public boolean needsSaving() {
		return this.fPreferences.needsSaving();
	}

	public void putValue(String name, String value) {
		try {
			this.fSilent = true;
			this.fPreferences.setValue(name, value);
		} finally {
			this.fSilent = false;
		}
	}

	public void setDefault(String name, double value) {
		this.fPreferences.setDefault(name, value);
	}

	public void setDefault(String name, float value) {
		this.fPreferences.setDefault(name, value);
	}

	public void setDefault(String name, int value) {
		this.fPreferences.setDefault(name, value);
	}

	public void setDefault(String name, long value) {
		this.fPreferences.setDefault(name, value);
	}

	public void setDefault(String name, String defaultObject) {
		this.fPreferences.setDefault(name, defaultObject);
	}

	public void setDefault(String name, boolean value) {
		this.fPreferences.setDefault(name, value);
	}

	public void setToDefault(String name) {
		this.fPreferences.setToDefault(name);
	}

	public void setValue(String name, double value) {
		this.fPreferences.setValue(name, value);
	}

	public void setValue(String name, float value) {
		this.fPreferences.setValue(name, value);
	}

	public void setValue(String name, int value) {
		this.fPreferences.setValue(name, value);
	}

	public void setValue(String name, long value) {
		this.fPreferences.setValue(name, value);
	}

	public void setValue(String name, String value) {
		this.fPreferences.setValue(name, value);
	}

	public void setValue(String name, boolean value) {
		this.fPreferences.setValue(name, value);
	}

	private class PropertyChangeListener implements Preferences.IPropertyChangeListener {
		private PropertyChangeListener() {
		}

		public void propertyChange(Preferences.PropertyChangeEvent event) {
			PreferencesAdapter.this.firePropertyChangeEvent(event.getProperty(), event.getOldValue(),
					event.getNewValue());
		}
	}

	@Override
	public void addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener listener) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener listener) {
		// TODO 自动生成的方法存根

	}
}