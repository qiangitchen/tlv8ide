package tern.eclipse.ide.core.utils;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

/**
 * {@link IPath} utilities.
 */
public class PathUtils {

	private static final String SLASH_STAR = "/*";

	private PathUtils() {
	}

	/**
	 * Returns true if the given path belongs to the given container and false
	 * otherwise.
	 * 
	 * @param path
	 *            the path to check
	 * @param containerPath
	 *            the container path
	 * @return true if the given path belongs to the given container and false
	 *         otherwise.
	 */
	public static boolean isBelongToContainer(IPath path, IPath containerPath) {
		int count = containerPath.matchingFirstSegments(path);
		return containerPath.segmentCount() == count;
	}

	/**
	 * Returns the relative path of the given path relative to the given
	 * container path. In the case of the resource is a folder, "/*" is added to
	 * the relative path.
	 * 
	 * @param the
	 *            path to check
	 * @param containerPath
	 *            the container path
	 * @param resourceType
	 *            the {@link IResource} resource type.
	 * @return the relative path of the given path relative to the given
	 *         container path. In the case of the resource is a folder, "/*" is
	 *         added to the relative path.
	 */
	public static IPath getRelativePath(IPath path, IPath containerPath, int resourceType) {
		IPath relativePath = path.makeRelativeTo(containerPath);
		if (resourceType == IResource.FOLDER) {
			relativePath = relativePath.append(SLASH_STAR);
		}
		return relativePath;
	}
	
	public static IPath getParentPath(IPath path) {
		if (path.isRoot()) {
			return null;
		}
		return path.removeFirstSegments(1);
	}

}
