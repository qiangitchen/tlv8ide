package tern.eclipse.ide.core;

import tern.ITernProject;
import tern.server.ITernServer;

/**
 * Tern server factory.
 */
public interface ITernServerFactory {

	/**
	 * Create an instance of tern server by using the given tern project.
	 * 
	 * @param project
	 *            tern project.
	 * @return an instance of tern server by using the given tern project.
	 * @throws Exception
	 */
	ITernServer create(ITernProject project) throws Exception;

}
