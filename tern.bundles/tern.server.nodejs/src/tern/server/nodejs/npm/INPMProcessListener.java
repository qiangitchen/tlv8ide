package tern.server.nodejs.npm;

import java.io.File;
import java.util.List;

/**
 * Listener for node.js process.
 * 
 * @author azerr
 * 
 */
public interface INPMProcessListener {

	/**
	 * Callback called when the given node.js process is created.
	 * 
	 * @param process
	 * @param commands
	 * @param projectDir
	 */
	void onCreate(NPMProcess process, List<String> commands, File projectDir);

	/**
	 * Callback called when the given node.js process start.
	 * 
	 * @param process
	 */
	void onStart(NPMProcess process);

	/**
	 * Callback called when the given node.js process send data.
	 * 
	 * @param process
	 * @param line
	 *            the data.
	 */
	void onData(NPMProcess process, String line);

	/**
	 * Callback called when the given node.js process stop.
	 * 
	 * @param process
	 */
	void onStop(NPMProcess process);

	/**
	 * Callback called when the given node.js throws error.
	 * 
	 * @param process
	 * @param line
	 *            the error.
	 */
	void onError(NPMProcess process, String line);

}
