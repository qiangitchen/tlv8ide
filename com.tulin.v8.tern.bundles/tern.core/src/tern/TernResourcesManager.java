package tern;

import java.io.IOException;

import tern.internal.resources.InternalTernResourcesManager;

public class TernResourcesManager {

	private static final InternalTernResourcesManager INSTANCE = InternalTernResourcesManager
			.getInstance();

	private TernResourcesManager() {
	}

	/**
	 * Returns a tern project object associated with the specified resource. May
	 * return null if resource doesn't point at a valid tern project.
	 * 
	 * @param project
	 * @return a tern project object associated with the specified resource. May
	 *         return null if resource doesn't point at a valid tern project.
	 * @throws IOException
	 */
	public static ITernProject getTernProject(Object project) {
		try {
			return INSTANCE.getTernProject(project, false);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Returns a tern project object associated with the specified resource. May
	 * return null if resource doesn't point at a valid tern project.
	 * 
	 * @param project
	 * @param force
	 *            true if .tern-project must be created if it doesn't exists,
	 *            and false otherwise.
	 * 
	 * @return a tern project object associated with the specified resource. May
	 *         return null if resource doesn't point at a valid tern project.
	 * @throws IOException
	 */
	public static ITernProject getTernProject(Object project, boolean force)
			throws IOException {
		return INSTANCE.getTernProject(project, force);
	}

	public static ITernFile getTernFile(Object fileObject) {
		return INSTANCE.getTernFile(fileObject);
	}

	public static boolean isHTMLFile(Object fileObject) {
		return INSTANCE.isHTMLFile(fileObject);
	}

	public static boolean isJSFile(Object fileObject) {
		return INSTANCE.isJSFile(fileObject);
	}

}
