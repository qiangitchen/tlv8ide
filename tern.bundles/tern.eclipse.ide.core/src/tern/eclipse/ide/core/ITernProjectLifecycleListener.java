package tern.eclipse.ide.core;

/**
 * Tern project lifecycle listener API.
 *
 */
public interface ITernProjectLifecycleListener {

	/**
	 * Lifecycle event type.
	 *
	 */
	public enum LifecycleEventType {
		onLoadBefore, onLoadAfter, onSaveBefore, onSaveAfter, onDisposeBefore, onDisposeAfter, onLintersChanged;
	}

	/**
	 * Handle event type;
	 * 
	 * @param project
	 * @param state
	 */
	void handleEvent(IIDETernProject project, LifecycleEventType state);

}
