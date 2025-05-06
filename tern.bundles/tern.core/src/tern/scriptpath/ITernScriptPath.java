package tern.scriptpath;

import java.util.List;

import tern.ITernAdaptable;
import tern.ITernProject;

/**
 * API of tern script path used to retrieve a list of JS files to load by tern
 * server.
 * 
 */
public interface ITernScriptPath extends ITernAdaptable {

	public static final ITernScriptPath[] EMPTY_SCRIPT_PATHS = new ITernScriptPath[0];

	/**
	 * Path type.
	 * 
	 */
	public enum ScriptPathsType {
		FILE, FOLDER, PROJECT;

		public static ScriptPathsType getType(String type) {
			ScriptPathsType[] types = ScriptPathsType.values();
			ScriptPathsType t = null;
			for (int i = 0; i < types.length; i++) {
				t = types[i];
				if (t.name().equals(type)) {
					return t;
				}
			}
			return null;
		}
	}

	/**
	 * Returns the project where script path belongs to.
	 * 
	 * @return
	 */
	ITernProject getOwnerProject();

	/**
	 * Returns the path of the root resource.
	 * 
	 * @return
	 */
	String getPath();

	/**
	 * Returns label
	 */
	String getLabel();

	/**
	 * Returns the script path type.
	 */
	ScriptPathsType getType();

	/**
	 * Returns true if script path is external and false otherwise.
	 * 
	 * @return
	 */
	boolean isExternal();

	/**
	 * Returns the external label if the script path is external and null
	 * otherwise.
	 * 
	 * @return the external label if the script path is external and null
	 *         otherwise.
	 */
	String getExternalLabel();

	/**
	 * Returns list of scripts defined by the script path.
	 * 
	 * @return
	 */
	List<ITernScriptResource> getScriptResources();

}
