package tern.eclipse.ide.server.nodejs.core.debugger.launchConfigurations;

/**
 * Commons constants of launch configurations for run/debug cli file with tern
 * debugger (Grunt, Gulp, Protractor launch).
 *
 */
public interface INodejsCliFileLaunchConfigurationConstants {

	/**
	 * Client file to use to run/debug grunt, gulp, protractor, etc
	 */
	String ATTR_CLI_FILE = "cli_file";

	/**
	 * Tern debugger to use to run/debug grunt, gulp, protractor, etc
	 */
	String ATTR_DEBUGGER = "debugger";

	String ATTR_NODE_INSTALL = "nodeinstall";
	
	String ATTR_NODE_PATH = "nodepath";

}
