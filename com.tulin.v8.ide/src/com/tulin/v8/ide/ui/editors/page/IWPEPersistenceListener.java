package com.tulin.v8.ide.ui.editors.page;

/**
 * Listens for persistence events fired from WPE instances, including save and
 * revert.
 */
public interface IWPEPersistenceListener {

	/**
	 * The type of the persistence event.
	 *
	 */
	public static enum PersistenceEventType {
		/**
		 * WPE is about to be saved. A listener may request that this operation
		 * be cancelled.
		 */
		BEFORE_SAVE,

		/**
		 * WPE was saved
		 */
		SAVED,

		/**
		 * WPE is about to be saved as. A listener may request that this
		 * operation be cancelled.
		 */
		BEFORE_SAVE_AS,

		/**
		 * WPE was saved as
		 */
		SAVED_AS,

		/**
		 * WPE is about to be reverted. A listener may request that this
		 * operation be cancelled.
		 */
		BEFORE_REVERT,

		/**
		 * WPE was reverted.
		 */
		REVERTED;
	}

	/**
	 * A persistence event.
	 * 
	 * <p>
	 * Not intended to be implemented by clients.
	 * </p>
	 */
	public static interface IPersistenceEvent {
		/**
		 * @return editor
		 */
		public WebPageEditor1 getWPEInstance();

		/**
		 * @return EventType
		 */
		public PersistenceEventType getEventType();

		/**
		 * A listener requests that all further processing of the operation be
		 * stopped after this event has first been sent to all listeners.
		 */
		public void cancelOperation();

		/**
		 * @return <code>true</code> if a listener has requested that the
		 *         operation be cancelled.
		 */
		public boolean isOperationCancelled();
	}

	/**
	 * A persistence event has occurred
	 * 
	 * @param event
	 */
	public void notify(IPersistenceEvent event);

}
