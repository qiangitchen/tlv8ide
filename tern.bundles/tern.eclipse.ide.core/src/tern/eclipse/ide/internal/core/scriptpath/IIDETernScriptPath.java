package tern.eclipse.ide.internal.core.scriptpath;

import org.eclipse.core.runtime.IPath;

import tern.ITernProject;
import tern.scriptpath.ITernScriptPathContainer;

/**
 * IDE tern script path.
 *
 */
public interface IIDETernScriptPath extends ITernScriptPathContainer {

	/**
	 * Returns true if the given path belongs to the given container and false
	 * otherwise.
	 * 
	 * @param path
	 *            the path to check
	 * @return true if the given path belongs to the given container and false
	 *         otherwise.
	 */
	boolean isBelongToContainer(IPath path);

	/**
	 * Returns true if the given path is in the scope of the script path and
	 * false otherwise.
	 * 
	 * @param path
	 * @param resourceType
	 * @return true if the given path is in the scope of the script path and
	 *         false otherwise.
	 */
	boolean isInScope(IPath path, int resourceType);

	/**
	 * Returns the owner tern project.
	 */
	ITernProject getOwnerProject();
}
