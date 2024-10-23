package tern.server.nodejs.process;

import java.io.File;
import java.util.List;

/**
 * Listener for node.js process.
 * 
 * @author azerr
 * 
 */
public interface INodejsProcessListener {

	/**
	 * Callback called when the given node.js process is created.
	 * 
	 * @param process
	 * @param commands
	 * @param projectDir
	 */
	void onCreate(INodejsProcess process, List<String> commands, File projectDir);

	/**
	 * Callback called when the given node.js process start.
	 * 
	 * @param process
	 */
	void onStart(INodejsProcess process);

	/**
	 * Callback called when the given node.js process send data.
	 * 
	 * @param process
	 * @param line
	 *            the data.
	 */
	void onData(INodejsProcess process, String line);

	/**
	 * Callback called when the given node.js process stop.
	 * 
	 * @param process
	 */
	void onStop(INodejsProcess process);

	/**
	 * Callback called when the given node.js throws error.
	 * 
	 * @param process
	 * @param line
	 *            the error.
	 */
	void onError(INodejsProcess process, String line);

}
