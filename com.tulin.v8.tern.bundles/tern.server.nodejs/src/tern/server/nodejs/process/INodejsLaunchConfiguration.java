package tern.server.nodejs.process;

import java.util.List;

/**
 * API to create node arguments for the command.
 *
 */
public interface INodejsLaunchConfiguration {

	/**
	 * Returns a list of arguments for the node command.
	 * 
	 * @return a list of arguments for the node command.
	 */
	List<String> createNodeArgs();

	/**
	 * Returns the launch mode 'run', 'debug'.
	 * 
	 * @return the launch mode 'run', 'debug'.
	 */
	String getLaunchMode();

	/**
	 * 
	 * @return
	 */
	boolean isSaveLaunch();

	boolean isWaitOnPort();

	/**
	 * Generate a configuration name for launch
	 * 
	 * @return a configuration name for launch
	 */
	String generateLaunchConfigurationName();
}
