package tern.server;

/**
 * Tern server listener.
 * 
 */
public interface ITernServerListener {

	/**
	 * Method called when the given tern server starts.
	 * 
	 * @param server
	 */
	void onStart(ITernServer server);

	/**
	 * Method called when the given tern server stops.
	 * 
	 * @param server
	 */
	void onStop(ITernServer server);

}
