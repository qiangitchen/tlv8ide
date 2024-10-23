package tern.eclipse.ide.core;

/**
 * Tern project lifecycle manager API.
 *
 */
public interface ITernProjectLifecycleManager {

	/**
	 * Add tern project lifecycle listener.
	 * 
	 * @param listener
	 *            the tern project lifecycle listener to add.
	 */
	void addTernProjectLifeCycleListener(ITernProjectLifecycleListener listener);

	/**
	 * Remove tern project lifecycle listener.
	 * 
	 * @param listener
	 *            the tern project lifecycle listener to remove.
	 */
	void removeTernProjectLifeCycleListener(
			ITernProjectLifecycleListener listener);
}
