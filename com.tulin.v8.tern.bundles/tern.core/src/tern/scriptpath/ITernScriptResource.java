package tern.scriptpath;

import tern.ITernFile;

/**
 * Script resource.
 * 
 */
public interface ITernScriptResource {

	/**
	 * The tern file containing scripts.
	 * 
	 * @return
	 */
	ITernFile getFile();

	/**
	 * The label to use when the script resource is displayed in the angular
	 * explorer view.
	 * 
	 * @return the label to use when the script resource is displayed in the
	 *         angular explorer view.
	 */
	String getLabel();

}
