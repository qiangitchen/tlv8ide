package tern.eclipse.ide.core.utils;

import org.eclipse.core.resources.IFile;

/**
 * Line offset provider.
 *
 */
public interface ILineOfOffsetProvider {

	/**
	 * Returns the line of offset of the given file with the given start offset
	 * and null otherwise.
	 * 
	 * @param start
	 * @param file
	 * @return the line of offset of the given file with the given start offset
	 *         and null otherwise.
	 */
	Integer getLineOfOffset(int start, IFile file);

}
