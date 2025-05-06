package tern.eclipse.ide.core;

import org.eclipse.core.resources.IProject;

/**
 * Listener to track preferences changes of tern server.
 * 
 */
public interface ITernServerPreferencesListener {

	/**
	 * Notify when tern server preferences has changed.
	 * 
	 * @param project
	 *            not null if preferences comes from a project and null when
	 *            it's a global preferences
	 */
	void serverPreferencesChanged(IProject project);
}
