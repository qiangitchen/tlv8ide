package tern.eclipse.ide.linter.core;

import java.io.IOException;

/**
 * Tern Linter configuration manager API.
 */
public interface ITernLinterConfigurationsManager {

	/**
	 * Create instance of tern linter config according the given tern module
	 * name which is a linter.
	 * 
	 * @param linterId
	 * @return
	 * @throws IOException
	 */
	ITernLinterConfig createLinterConfig(String linterId) throws IOException;

	/**
	 * Returns the file name of the given linter id (eg : '.jshintrc for
	 * JSHint).
	 * 
	 * @param linterId
	 * @return
	 */
	String getFilename(String linterId);

}
